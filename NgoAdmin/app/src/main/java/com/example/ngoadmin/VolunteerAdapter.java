package com.example.ngoadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<VolunteerModel> volunteerModelArrayList;


    public VolunteerAdapter(Context context, ArrayList<VolunteerModel> volunteerModelArrayList) {
        this.context = context;
        this.volunteerModelArrayList = volunteerModelArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private VolunteerModel volunteerModel;
        private TextView name;
        private TextView email;
        private TextView mobile;
        private TextView address;
        private TextView password;
        private TextView area;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.row_layout_volunteer_name);
            email = itemView.findViewById(R.id.row_layout_volunteer_email);
            mobile = itemView.findViewById(R.id.row_layout_volunteer_mobile);
            address = itemView.findViewById(R.id.row_layout_volunteer_address);
            password = itemView.findViewById(R.id.row_layout_volunteer_password);
            area = itemView.findViewById(R.id.row_layout_volunteer_area);

        }

        public void setData(VolunteerModel data) {
            this.volunteerModel = data;
            name.setText(data.getName());
            email.setText(data.getEmail());
            password.setText(data.getPassword());
            mobile.setText(data.getMobile());
            address.setText(data.getAddress());
            area.setText(data.getArea());
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_volunteer, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        VolunteerModel volunteerModel = volunteerModelArrayList.get(i);
        myViewHolder.setData(volunteerModel);
    }


    @Override
    public int getItemCount() {
        return volunteerModelArrayList.size();
    }

}
