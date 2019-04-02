package com.example.ngoadmin;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<UserModel> userModelArrayList;
    private UserItemClickListener userItemClickListener;


    public UserAdapter(Context context, ArrayList<UserModel> userModelArrayList, UserItemClickListener userItemClickListener) {
        this.context = context;
        this.userModelArrayList = userModelArrayList;
        this.userItemClickListener = userItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private UserModel userModel;
        private TextView name;
        private TextView email;
        private TextView mobile;
        private TextView address;
        private TextView password;
        private TextView userType;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.row_layout_user_name);
            email = itemView.findViewById(R.id.row_layout_user_email);
            mobile = itemView.findViewById(R.id.row_layout_user_mobile);
            address = itemView.findViewById(R.id.row_layout_user_address);
            password = itemView.findViewById(R.id.row_layout_user_password);
            userType = itemView.findViewById(R.id.row_layout_user_type);

            itemView.setOnClickListener(this);
        }

        public void setData(UserModel data) {
            this.userModel = data;
            name.setText(data.getName());
            email.setText(data.getEmail());
            password.setText(data.getPassword());
            mobile.setText(data.getMobile());
            address.setText(data.getAddress());

            if (data.isNgo()) {
                userType.setTextColor(Color.RED);
                userType.setText("NGO");
            } else {
                userType.setTextColor(Color.BLUE);
                userType.setText("USER");
            }

        }

        @Override
        public void onClick(View v) {
            if (userItemClickListener != null) {
                userItemClickListener.onUserItemClick(userModel);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_user, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        UserModel userModel= userModelArrayList.get(i);
        myViewHolder.setData(userModel);
    }


    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

}
