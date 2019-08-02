package com.nothing.timing.wearound.ui.fragments.inner_lists;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nothing.timing.wearound.R;
import com.nothing.timing.wearound.bloc.data.ClassData.AroundFragData;
import com.nothing.timing.wearound.bloc.data.ClassData.StaticData;
import com.nothing.timing.wearound.tools.DialogAndToast;
import com.nothing.timing.wearound.tools.NetworkManager;
import com.nothing.timing.wearound.bloc.data.extras.results.NearbyResult;
import com.nothing.timing.wearound.tools.PermissionProvider;
import com.nothing.timing.wearound.tools.WrapContentLinearLayoutManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class AroundFrag extends Fragment {

    public final static String TAG = "AroundFrag.TAG";

    public AroundFragData data;
    private RecyclerView recyclerView;
    private View view;

    private SwipeRefreshLayout refresh;
    private boolean isRefreshing = false;

    private double lat;
    private double lng;

    private String key;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            if (this.getArguments() != null && this.getArguments().getBoolean(StaticData.NETWORK_CONNECTION)) {

                view = inflater.inflate(R.layout.fragment_around, container, false);


                lat = getArguments().getDouble(StaticData.LATITUDE);
                lng = getArguments().getDouble(StaticData.LONGITUDE);
                key = getContext().getResources().getString(R.string.api_key);

                data = new AroundFragData(key, lat, lng);

                recyclerView = view.findViewById(R.id.recycler);

                Log.e(TAG, " onCreate URL is: "+ data.getUrl());

                AsyncHelper async = new AsyncHelper(this);

                async.execute();

            } else {

            view = inflater.inflate(R.layout.no_permission, container, false);

            TextView text = view.findViewById(R.id.no_text);
            text.setText("NO CONNECTION");
        }

        refresh = view.findViewById(R.id.refresh);

        Log.e(TAG, "refresh is " + refresh);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG, "refresh is " + refresh);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (StaticData.getIsFunctional()
                        && PermissionProvider.isNetworkAvailable(getContext())) {

                    if (recyclerView == null) {

                        getActivity().startActivity(
                                new Intent(getActivity(), getActivity().getClass())
                        );

                    } else {

                        isRefreshing = true;

                        data = new AroundFragData(key, lat, lng);

                        AsyncHelper async = new AsyncHelper(AroundFrag.this);

                        async.execute();
                    }

                } else {

                    refresh.setRefreshing(false);
                }
            }
        });
    }

    public void executeSearch(String query) {

            Log.e(TAG, "Query is "+ query);

            data.setSearchUrl(query);

            AsyncHelper async = new AsyncHelper(this);
            async.execute();
    }

    private static class AsyncHelper extends AsyncTask<Void,Void,View> {

        private WeakReference<AroundFrag> weakReference;

        private ProgressDialog dialog;

        AsyncHelper(AroundFrag frag) {

            weakReference = new WeakReference<>(frag);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            final AroundFrag ref = weakReference.get();

            if (ref.recyclerView == null ) {

                Log.e(TAG, "RecycleView DID NOT pass task stage");
            }

            if (!ref.isRefreshing) {

                dialog = ProgressDialog.show(ref.getContext(), "Loading", "Wait while loading...");
            }

        }

        @Override
        protected View doInBackground(Void... voids) {

            AroundFrag aroundFrag = weakReference.get();

            if (aroundFrag == null || aroundFrag.isRemoving() ) {
                return null;
            }
            NetworkManager networkManager = new NetworkManager();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            Log.e(TAG, " URL task is: "+ aroundFrag.data.getUrl());

            String response = networkManager.getResponse(aroundFrag.data.getUrl());
            Log.e(TAG, "RUNNING");
            NearbyResult nearbyResult = gson.fromJson(response, NearbyResult.class);

            if (nearbyResult != null ) {

                aroundFrag.data.setLists(nearbyResult);

            } else {

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(View view) {
            super.onPostExecute(view);

            final AroundFrag ref = weakReference.get();

            ArrayList<String> shopNames = ref.data.getShopNames();

            if (shopNames != null && !shopNames.isEmpty()) {

                ref.recyclerView.setVisibility(View.VISIBLE);

                ref.view.findViewById(R.id.something_wrong).setVisibility(View.GONE);

                RecyclerAdapter adapter = new RecyclerAdapter(null,
                        ref.data, null, ref.data.getDeviceLocation());

                ref.recyclerView.setAdapter(adapter);

                ref.recyclerView.setLayoutManager(
                        new WrapContentLinearLayoutManager(ref.getContext(),
                                LinearLayoutManager.VERTICAL, false));

            } else {

                ref.recyclerView.setVisibility(View.GONE);

                ref.view.findViewById(R.id.something_wrong).setVisibility(View.VISIBLE);
            }

            if (!ref.isRefreshing) {

                dialog.dismiss();

            } else {

                ref.refresh.setRefreshing(false);

                ref.isRefreshing = false;
            }
        }
    }

}
