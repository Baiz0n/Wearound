package com.nothing.timing.wearound.ui.fragments.inner_lists;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nothing.timing.wearound.R;
import com.nothing.timing.wearound.bloc.data.ClassData.AroundFragData;
import com.nothing.timing.wearound.bloc.data.ClassData.RecyclerData;
import com.nothing.timing.wearound.bloc.data.ClassData.StaticData;
import com.nothing.timing.wearound.bloc.data.extras.DBController;
import com.nothing.timing.wearound.bloc.data.sql.SqlNames;
import com.nothing.timing.wearound.bloc.data.sql.WearoundDb;
import com.nothing.timing.wearound.tools.DialogAndToast;
import com.nothing.timing.wearound.tools.ImageConverter;
import com.nothing.timing.wearound.tools.NetworkManager;
import com.nothing.timing.wearound.tools.PermissionProvider;
import com.nothing.timing.wearound.bloc.data.extras.results.PlaceIdResult;
import com.nothing.timing.wearound.bloc.data.extras.results.ResultCursor;
import com.nothing.timing.wearound.ui.MainActivity;

import java.lang.ref.WeakReference;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = RecyclerAdapter.class.getSimpleName();

    private RecyclerData data;
    private boolean networkAvailable;

    private boolean isAround;

    private WearoundDb hisDb;
    private WearoundDb favDb;
    private String dbTableName = "";

    private boolean isFirstOnBind = true;

    RecyclerAdapter(DBController controller, AroundFragData data, String dbTableName, LatLng deviceLocation) {

        this.data = new RecyclerData(controller);

        if (controller != null) {

            isAround = false;
            this.dbTableName = dbTableName;

        } else {

            this.data.setShopNames(data.getShopNames());
            this.data.setDistances(data.getDistances());
            this.data.setLatitudes(data.getLatitudes());
            this.data.setLongitudes(data.getLongitudes());
            this.data.setOpens(data.getOpens());
            this.data.setShopIdUrls(data.getShopUrls());
            this.data.setImgUrls(data.getImgUrls());
            this.data.setImages(data.getImages());

            isAround = true;
        }

        this.data.setDeviceLocation(deviceLocation);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        Log.e(TAG, "Called onBindViewHolder");

        if (isFirstOnBind) {

            isFirstOnBind = false;

            data.setContext(holder.parent.getContext());
            hisDb = new WearoundDb(this.data.getContext(), SqlNames.getHistoryTableName());
            favDb = new WearoundDb(this.data.getContext(), SqlNames.getFavouritesTableName());

           networkAvailable = PermissionProvider.isNetworkAvailable(data.getContext());
        }

        if ( !networkAvailable ) {

            holder.progressOpen.setVisibility(View.GONE);
            holder.progressDistance.setVisibility(View.INVISIBLE);
        }

        final String shopName = data.getShopNames().get(i);
        final double latitude = data.getLatitudes().get(i);
        final double longitude = data.getLongitudes().get(i);
        final Bitmap image = data.getImages().get(i);

        try {

            holder.shopNameView.setText(shopName);
            holder.icon.setImageBitmap(image);

            if ( (data.getRecursionSwitch(i) && isAround) || !isAround ) {

                if (data.getAddresses(isAround).size() > i ) {

                    holder.addressView.setText( data.getAddresses(isAround).get(i) );
                }
            }

            if (data.getRecursionSwitch(i) || isAround) {

                holder.distanceView.setText( data.getDistances().get(i) );
                holder.openView.setText( data.getOpens().get(i) );

                holder.progressOpen.setVisibility(View.GONE);
                holder.progressDistance.setVisibility(View.GONE);

                holder.distanceView.setVisibility(View.VISIBLE);
                holder.distanceIcon.setVisibility(View.VISIBLE);

                if ( holder.openView.getText().equals(StaticData.OPEN) ||
                        holder.openView.getText().equals(StaticData.CLOSED)) {

                    holder.openView.setVisibility(View.VISIBLE);
                    holder.openIcon.setVisibility(View.VISIBLE);

                } else {

                    holder.openView.setVisibility(View.GONE);
                    holder.openIcon.setVisibility(View.GONE);
                }
            }
        } catch (IndexOutOfBoundsException e ) {

            Log.e(TAG, "Exception caught ===> " + e);
        }



        final int position = holder.getAdapterPosition();

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.distanceView.getVisibility() != View.GONE && networkAvailable) {

                    Log.e(TAG, "inserting to history");

                    if (data.getContext() instanceof MainActivity) {

                        ((MainActivity) data.getContext()).setViewPager(1);
                        ((MainActivity) data.getContext()).initMap(latitude, longitude, shopName);
                    }

                    if (isAround && data.getRecursionSwitch(position)) {

                        if ( hisDb.recordExists(shopName) ) {

                            hisDb.delRow(shopName);
                        }

                        try {

                            hisDb.insertRow(shopName, data.getAddresses(isAround).get(position), String.valueOf(latitude), String.valueOf(longitude),
                                    data.getShopIdUrls("").get(position), data.getImgUrls("").get(position),
                                    ImageConverter.getByteFromBitmap(data.getImages().get(position)));

                            ((MainActivity)data.getContext()).initRecyclerInListsHistory();

                            DBController control = new DBController(hisDb);

                            if (control.getStringColumn(SqlNames.SHOP_NAME).size() + 1 > 20) {

                                hisDb.delRow(data.getShopNames().get(data.getShopNames().size()));
                                data.removeListsPosition(position);
                                notifyDataSetChanged();
                            }

                        } catch (IndexOutOfBoundsException e) {

                            Log.e(TAG, "Exception caught ==> " + e);
                        }

                        ((MainActivity) data.getContext()).initRecyclerInListsHistory();
                    }
                } else {

                    DialogAndToast.shortToast(data.getContext(),"No internet connection");
                }
            }
        });

        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final PopupMenu menu = new PopupMenu(data.getContext(), holder.parent);

                final String favString = SqlNames.getFavouritesTableName();
                final String hisString = SqlNames.getHistoryTableName();

                if (dbTableName.equals(favString)) {

                    menu.getMenu().add(0,2,2,"Remove From Favourites");

                } else if (dbTableName.equals(hisString)) {

                    menu.getMenu().add(0, 1, 1, "Add To Favourites");
                    menu.getMenu().add(0,2,2,"Remove From Viewed");

                } else {

                    menu.getMenu().add(0, 1, 1, "Add To Favourites");
                }
                menu.show();

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        if ( dbTableName.equals(favString) ) {

                            favDb.delRow(shopName);
                            data.removeListsPosition(position);
                            notifyDataSetChanged();

                        } else if ( dbTableName.equals(hisString) ) {

                            switch (item.getItemId()) {

                                case 1 :
                                    insertRowToFavourites();
                                    break;

                                case 2 :

                                    if (hisDb.delRow(shopName)) {

                                        data.removeListsPosition(position);
                                        notifyDataSetChanged();
                                        break;
                                    }
                            }

                        } else {

                            insertRowToFavourites();
                        }

                        return false;
                    }

                    void insertRowToFavourites() {

                        if (!favDb.recordExists(shopName)) {

                            favDb.insertRow(shopName, data.getAddresses(isAround).get(position),
                                    String.valueOf(latitude),
                                    String.valueOf(longitude),
                                    data.getShopIdUrls("").get(position),
                                    data.getImgUrls("").get(position),
                                    ImageConverter.getByteFromBitmap(data.getImages().get(position)));

                            ((MainActivity)data.getContext()).initRecyclerInListsFavourites();

                            DialogAndToast.shortToast(data.getContext(), "Added " + shopName + " to favourites!");

                        } else {

                            DialogAndToast.shortToast(data.getContext(), "Shop already in favourites");
                        }
                    }

                });

                return false;
            }
        });

        if ( networkAvailable && !data.getRecursionSwitch(i)
                && data.getOpens().size() <= i
                || data.getAddresses(isAround).size() <= i ) {

            data.setRecursionSwitch(i);

            AsyncHelper task = new AsyncHelper(this);
            task.execute( "", i+"" );
        }
    }

    @Override
    public int getItemCount() {
        return data.getShopNames().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout parent;
        TextView shopNameView;
        TextView addressView;
        TextView distanceView;
        TextView openView;
        ImageView icon;
        ImageView openIcon;
        ProgressBar progressOpen;
        ProgressBar progressDistance;
        ImageView distanceIcon;


        ViewHolder(@NonNull View view) {
            super(view);

            parent = view.findViewById(R.id.parent);
            shopNameView = view.findViewById(R.id.shop_name);
            addressView = view.findViewById(R.id.address);
            distanceView = view.findViewById(R.id.distance);
            openView = view.findViewById(R.id.open);
            icon = view.findViewById(R.id.icon);
            openIcon = view.findViewById(R.id.open_icon);
            progressOpen = view.findViewById(R.id.progress_open);
            progressDistance = view.findViewById(R.id.progress_distance);
            distanceIcon = view.findViewById(R.id.distance_icon);
        }
    }

    private static class AsyncHelper extends AsyncTask<String,Void,String> {

        private WeakReference<RecyclerAdapter> weakReference;

        private int position;

        AsyncHelper(RecyclerAdapter adapter) {

            weakReference = new WeakReference<>(adapter);
        }

        @Override
        protected String doInBackground(String... params) {

            final RecyclerAdapter ref = weakReference.get();

            if ( ref == null ) {
                return null;
            }

            String key;

            if (ref.isAround) {

                key = "";

            } else {

                key = params[0];
            }

                position = Integer.valueOf(params[1]);



            NetworkManager networkManager = new NetworkManager();

            String response = networkManager.getResponse(
                    ref.data.getShopIdUrls(key).get(position));

            //Log.e(TAG, response);

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                PlaceIdResult placeIdResult = gson.fromJson(response, PlaceIdResult.class);

                ResultCursor resultCursor = new ResultCursor(placeIdResult,
                        ref.data.getDeviceLocation());


                if (!ref.isAround) {

                    try {

                        ref.data.addOpen(position,resultCursor.isOpen());
                        ref.data.addDistance(position,resultCursor.getDistance());

                    } catch (IndexOutOfBoundsException e) {

                        Log.e(TAG, "Exception caught ===> " + e);
                    }


                } else {

                    ref.data.addAddress(position,resultCursor.getAddress());
                }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            final RecyclerAdapter ref = weakReference.get();

            if ( ref == null ) {
                return;
            }

            ref.notifyItemChanged(position);
        }
    }

}
