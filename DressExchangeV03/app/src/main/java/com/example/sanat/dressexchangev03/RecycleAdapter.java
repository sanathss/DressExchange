package com.example.sanat.dressexchangev03;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>{

    private JSONArray mData;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private TextView title;
    private TextView size;
    private TextView condition;
    private TextView suburb;

    private int type = 0;

    // data is passed into the constructor
    public void RecyclerAdapter(Context context, JSONArray data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setmData(JSONArray mData) {
        this.mData = mData;
    }

    public void setmInflater(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("Get View", "Start");
        View view  = null;
        if(view == null){
            Log.d("Get View", "View Inflator");
            if(type == 1) {
                view = mInflater.inflate(R.layout.item_card, null);
            }else if (type ==2){
                view = mInflater.inflate(R.layout.my_items, null);
            }
        }
        return  new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            String sTitle = mData.getJSONObject(position).getString("title");
            String sSize = "Size " + mData.getJSONObject(position).getString("size");
            String sCondition = mData.getJSONObject(position).getString("condition");
            String sSuburb = mData.getJSONObject(position).getString("suburb");
            title.setText(sTitle);
            size.setText(sSize);
            condition.setText(sCondition);
            suburb.setText(sSuburb);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            size = itemView.findViewById(R.id.size);
            condition = itemView.findViewById(R.id.condition);
            suburb = itemView.findViewById(R.id.suburb);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    JSONObject getItem(int id) {
        try {
            return mData.getJSONObject(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}