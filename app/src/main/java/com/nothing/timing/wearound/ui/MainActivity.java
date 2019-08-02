package com.nothing.timing.wearound.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.nothing.timing.wearound.R;
import com.nothing.timing.wearound.bloc.data.ClassData.StaticData;
import com.nothing.timing.wearound.bloc.data.sql.SqlNames;
import com.nothing.timing.wearound.bloc.data.sql.WearoundDb;
import com.nothing.timing.wearound.tools.DialogAndToast;
import com.nothing.timing.wearound.tools.PermissionProvider;
import com.nothing.timing.wearound.tools.PowerConnectionReceiver;
import com.nothing.timing.wearound.ui.fragments.GoogleMapFragment;
import com.nothing.timing.wearound.ui.fragments.Lists;
import com.nothing.timing.wearound.ui.fragments.LockableViewPager;
import com.nothing.timing.wearound.ui.fragments.MainPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    public LockableViewPager container;

    private GoogleMapFragment googleMap;
    private Lists lists;

    public LatLng latLng;

    private WearoundDb hisDb;
    private WearoundDb favDb;

    private PowerConnectionReceiver receiver;

    private SharedPreferences sharedPref;

    private SearchView searchView;

    private boolean isPortrait;

    private double screenInches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUniversalData();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();

        display.getSize(size);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(size.x/dm.xdpi,2);
        double y = Math.pow(size.y/dm.ydpi,2);
        screenInches =  Math.sqrt(x+y);
        Log.e("debug","Screen inches : " + screenInches);

        int orientation = getResources().getConfiguration().orientation;
        isPortrait = orientation != Configuration.ORIENTATION_LANDSCAPE;

        hisDb = new WearoundDb(this, SqlNames.getHistoryTableName());
        favDb = new WearoundDb(this, SqlNames.getFavouritesTableName());

        receiver = new PowerConnectionReceiver();
        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(receiver, ifilter);

        if ( !(ContextCompat.checkSelfPermission(this,
                PermissionProvider.PERMISSION[0])
                == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        PermissionProvider.PERMISSION[1])
                        == PackageManager.PERMISSION_GRANTED) ) {

           ActivityCompat.requestPermissions(this,
                    PermissionProvider.PERMISSION, PermissionProvider.CODE);

        } else {

            doStuff();
        }
    }

    private void setUniversalData() {

        sharedPref = getSharedPreferences(StaticData.getPrefTitle(), MODE_PRIVATE);

        StaticData.setIsKm(sharedPref.getBoolean(StaticData.getPrefDistanceTypeKey(), true));
        StaticData.setRadius(sharedPref.getInt(StaticData.getPrefRadiusKey(), 5000));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            hisDb.delData();
            favDb.delData();
            doStuff();

        } else {

            setContentView(R.layout.no_permission);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }, 5000);
        }

    }

    private void doStuff() {

        if(!getIntent().hasExtra("")) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("", "");
            startActivity(intent);
            finish();

        } else {

            if (isPortrait || screenInches < 7) {
                setContentView(R.layout.activity_main);

                container = findViewById(R.id.container);
                container.setSwipeable(false);
                container.setOffscreenPageLimit(2);
                initContainer(container);

            } else {
                setContentView(R.layout.portrait_tablet);

                initFragments();
            }

        }
    }

    private void initContainer(ViewPager pager) {

        Log.e(TAG, "Setting up container");

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());

        googleMap = new GoogleMapFragment();
        lists = new Lists();

        googleMap.setLists(lists);

        adapter.addFragment(lists);
        adapter.addFragment(googleMap);
        pager.setAdapter(adapter);
    }

    private void initFragments() {

        lists = new Lists();
        googleMap = new GoogleMapFragment();

        googleMap.setLists(lists);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag1, lists)
                .replace(R.id.frag2, googleMap)
                .commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (screenInches > 6.9) {

            MainActivity.this.startActivity(
                    new Intent(MainActivity.this, MainActivity.class));
        }
    }

    public void setViewPager(int fragmentNum) {

        if (isPortrait || screenInches < 6.9) {

            container.setCurrentItem(fragmentNum);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lists_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        searchView = (SearchView) search.getActionView();

        ImageView icon = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        icon.setColorFilter(Color.WHITE);

        searchView.setOnQueryTextListener(searchListener);
        return true;
    }

    private SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {

            if (StaticData.getIsFunctional()) {

                if (PermissionProvider.isNetworkAvailable(getApplicationContext())) {

                    lists.executeSearch(query);

                } else {

                    DialogAndToast.shortToast(getApplicationContext(), "NO CONNECTION");
                }
            } else {

                DialogAndToast.shortToast(getApplicationContext(), "Cannot perform search");
            }

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {

            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.settings :

                Intent intent = new Intent( MainActivity.this, Settings.class);

                startActivity(intent);
                break;

            case R.id.clear_searched :

                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked

                                hisDb.delData();
                                initRecyclerInListsHistory();

                                DialogAndToast.shortToast(MainActivity.this,
                                        "All history removed");
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                DialogAndToast.executeDialog(MainActivity.this,
                        "deleting all viewed shops, are you sure?", dialog);

                break;

            case R.id.clear_favourites :

                DialogInterface.OnClickListener dialog2 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked

                                favDb.delData();
                                initRecyclerInListsFavourites();

                                DialogAndToast.shortToast(MainActivity.this,
                                        "All favourites removed");
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                DialogAndToast.executeDialog(MainActivity.this,
                        "Deleting all favourites, are you sure?", dialog2);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(container.getCurrentItem() == 1) {
            container.setCurrentItem(0, true);
        } else {
            super.onBackPressed(); // This will pop the Activity from the stack.
        }
    }

    public void initMap(double lat, double lng, String shopName) {

        latLng = new LatLng(lat,lng);


        googleMap.initShopLocation(latLng, shopName);
    }

    public void initRecyclerInListsHistory() {

        if ( StaticData.getIsFunctional() ) {

            lists.initHistoryRecycler(MainActivity.this);
        }
    }

    public void initRecyclerInListsFavourites() {

        if ( StaticData.getIsFunctional() ) {

            lists.initFavouritesRecycler(MainActivity.this);
        }
    }

}


