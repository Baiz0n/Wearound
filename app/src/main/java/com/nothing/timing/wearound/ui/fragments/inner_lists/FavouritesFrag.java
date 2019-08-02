package com.nothing.timing.wearound.ui.fragments.inner_lists;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;
import com.nothing.timing.wearound.R;
import com.nothing.timing.wearound.bloc.data.ClassData.StaticData;
import com.nothing.timing.wearound.bloc.data.extras.DBController;
import com.nothing.timing.wearound.bloc.data.sql.SqlNames;
import com.nothing.timing.wearound.bloc.data.sql.WearoundDb;


public class FavouritesFrag extends Fragment {

    private RecyclerView recyclerView;
    private DBController controller;

    private LatLng deviceLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inner_list, container, false);

        Bundle bundle = getArguments();

        double lat = 0;
        double lng = 0;

        if (bundle != null) {

            lat = this.getArguments().getDouble(StaticData.LATITUDE);
            lng = this.getArguments().getDouble(StaticData.LONGITUDE);
        }

        deviceLocation = new LatLng(lat,lng);

        recyclerView = view.findViewById(R.id.recycler);

        initRecyclerView(getContext());

        return view;
    }

    public void initRecyclerView(Context context) {

        WearoundDb db = new WearoundDb(context, SqlNames.getFavouritesTableName());
        controller = new DBController(db);

            RecyclerAdapter adapter = new RecyclerAdapter(controller,
                    null, SqlNames.getFavouritesTableName(), deviceLocation);

            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);

    }
}
