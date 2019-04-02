package com.example.ngouser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<CommentModel> commentModelArrayList;

    public CommentAdapter(Context context, ArrayList<CommentModel> commentModelArrayList) {
        this.context = context;
        this.commentModelArrayList = commentModelArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CommentModel commentModel;
        private TextView message;
        private TextView username;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.row_layout_comment_msg);
            username = itemView.findViewById(R.id.row_layout_comment_user);

        }

        public void setData(CommentModel data) {
            this.commentModel = data;
            message.setText(data.getMessage());
            username.setText(data.getName());


        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_comment, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        CommentModel commentModel= commentModelArrayList.get(i);
        myViewHolder.setData(commentModel);
    }


    @Override
    public int getItemCount() {
        return commentModelArrayList.size();
    }

}
