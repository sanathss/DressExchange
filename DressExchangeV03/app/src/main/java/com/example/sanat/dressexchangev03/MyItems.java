package com.example.sanat.dressexchangev03;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MyItems  extends Fragment
        implements RecycleAdapter.ItemClickListener {

    //Dress Variables
    private RecycleAdapter adapter;
    private RecyclerView recyclerView;
    private JSONArray dressList;

    //Login Variables
    String loginID = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            loginID = bundle.getString("login");
        }

        dressList = null;

        try {
            String response = new ServerComm().execute("mine", loginID).get();
            JSONObject refreshed = new JSONObject(response);
            dressList = new JSONArray(refreshed.getString("dresses"));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
    }

        View view = inflater.inflate(R.layout.activity_items_list, container, false);

        recyclerView = view.findViewById(R.id.list);

        int numberOfColumns = 2;

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        adapter = new RecycleAdapter();
        adapter.setType(2);
        adapter.setmData(dressList);
        adapter.setmInflater(LayoutInflater.from(getActivity()));
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

}
