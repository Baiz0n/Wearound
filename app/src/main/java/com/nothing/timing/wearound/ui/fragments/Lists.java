package com.nothing.timing.wearound.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.SimpleCursorTreeAdapter;

import com.nothing.timing.wearound.R;
import com.nothing.timing.wearound.bloc.data.ClassData.ListsData;
import com.nothing.timing.wearound.bloc.data.ClassData.StaticData;
import com.nothing.timing.wearound.tools.PermissionProvider;
import com.nothing.timing.wearound.ui.fragments.inner_lists.AroundFrag;
import com.nothing.timing.wearound.ui.fragments.inner_lists.FavouritesFrag;
import com.nothing.timing.wearound.ui.fragments.inner_lists.HistoryFrag;

import java.util.ArrayList;


public class Lists extends Fragment implements GoogleMapFragment.OnLatLngFinnitoListener {

    private final String TAG = Lists.class.getSimpleName();

    private ListsData data;

    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager container;
    private HistoryFrag history;
    private FavouritesFrag favourites;
    private AroundFrag around;
    private Adapter adapter;

    private boolean isInitted = false;

    private double lat = 0;
    private double lng = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, viewGroup, false);

        data = new ListsData();
        tabs = view.findViewById(R.id.tab);
        toolbar = view.findViewById(R.id.toolbar);
        container = view.findViewById(R.id.container);

        try {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        } catch (NullPointerException e) {

            Log.e(TAG, "NullPointerException caught ==> " + e);
        }

        tabs.addTab(tabs.newTab().setText(data.AROUND_NAME));
        tabs.addTab(tabs.newTab().setText(data.SEARCHED_NAME));
        tabs.addTab(tabs.newTab().setText(data.FAVOURITES_NAME));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        tabs.addOnTabSelectedListener(tabListener);

        container.setOffscreenPageLimit(2);
        container.addOnPageChangeListener(pagerListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new Adapter(getChildFragmentManager());
        history = new HistoryFrag();
        favourites = new FavouritesFrag();
        around = new AroundFrag();

        if ( !PermissionProvider.isNetworkAvailable(getContext()) ||
                !StaticData.getIsFunctional()) {

            setUpContainerNoConnection();
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void initHistoryRecycler(Context context) {

        history.initRecycleView(context);
    }

    public void initFavouritesRecycler(Context context) {

        favourites.initRecyclerView(context);
    }

    private ViewPager.OnPageChangeListener pagerListener = new
            ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {


                }

                @Override
                public void onPageSelected(int i) {

                    tabs.setScrollPosition(i,0,true);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            };

    private TabLayout.OnTabSelectedListener tabListener = new
            TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Log.e(TAG, "tabListener: Current tab is => " + tab.getPosition());

                    container.setCurrentItem(tab.getPosition());

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            };

    @Override
    public void setOnLatLngFinnitoListener(double lat, double lng) {

        this.lat = lat;
        this.lng = lng;

        setupAdapter();
    }

    public void setupAdapter() {

        isInitted = true;

        Bundle bundle = new Bundle();
        bundle.putDouble(StaticData.LATITUDE, lat);
        bundle.putDouble(StaticData.LONGITUDE, lng);
        bundle.putBoolean(StaticData.NETWORK_CONNECTION, true);

        around.setArguments(bundle);
        history.setArguments(bundle);
        favourites.setArguments(bundle);

        adapter.addFragment(around);
        adapter.addFragment(history);
        adapter.addFragment(favourites);

        container.setAdapter(adapter);
    }

    private void setUpContainerNoConnection() {

        Bundle bundle = new Bundle();
        bundle.putBoolean("network_connection", false);

        around.setArguments(bundle);

        adapter.addFragment(around);
        adapter.addFragment(history);
        adapter.addFragment(favourites);

        container.setAdapter(adapter);
        container.setCurrentItem(1);
    }

    public void executeSearch(String query) {

        container.setCurrentItem(0);
        around.executeSearch(query);
    }

    public class Adapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();

        Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFragment(Fragment fragment) {

            fragments.add(fragment);
        }
    }


} // end class
