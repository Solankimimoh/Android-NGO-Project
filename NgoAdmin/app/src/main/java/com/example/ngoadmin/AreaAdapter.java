package com.example.ngoadmin;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<AreaModel> areaModelArrayList;


    public AreaAdapter(Context context, ArrayList<AreaModel> areaModelArrayList) {
        this.context = context;
        this.areaModelArrayList = areaModelArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private AreaModel areaModel;
        private TextView areaNameTv;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            areaNameTv = itemView.findViewById(R.id.row_layout_area_name);

        }

        public void setData(AreaModel data) {
            this.areaModel = data;
            areaNameTv.setText(data.getAreaName());


        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_area, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        AreaModel areaModel = areaModelArrayList.get(i);
        myViewHolder.setData(areaModel);
    }


    @Override
    public int getItemCount() {
        return areaModelArrayList.size();
    }

}
