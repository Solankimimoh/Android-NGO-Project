package com.example.ngoadmin;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<PostModel> postModelArrayList;
    private PostItemClickListener postItemClickListener;

    public PostAdapter(Context context, ArrayList<PostModel> postModelArrayList, PostItemClickListener postItemClickListener) {
        this.context = context;
        this.postModelArrayList = postModelArrayList;
        this.postItemClickListener = postItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PostModel postModel;
        private TextView title;
        private TextView description;
        private TextView username;
        private TextView category;
        private TextView area;
        private TextView date;
        private ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.row_post_title);
            description = itemView.findViewById(R.id.row_post_description);
            username = itemView.findViewById(R.id.row_post_user);
            category = itemView.findViewById(R.id.row_post_category);
            area = itemView.findViewById(R.id.row_post_area);
            date = itemView.findViewById(R.id.row_post_date);
            img = itemView.findViewById(R.id.row_post_img);

            itemView.setOnClickListener(this);
        }

        public void setData(PostModel data) {
            this.postModel = data;
            title.setText(data.getTitle());
            description.setText(data.getDescription());
            date.setText(data.getDate());
            username.setText(data.getUserUuid());
            category.setText(data.getCategory());
            area.setText(data.getArea());
            date.setText(data.getDate());

            Glide.with(context)
                    .load(data.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(img);
        }

        @Override
        public void onClick(View v) {
            if (postItemClickListener != null) {
                postItemClickListener.onPostItemClickListener(postModel);
            }
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_post, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        PostModel postModel = postModelArrayList.get(i);
        myViewHolder.setData(postModel);
    }


    @Override
    public int getItemCount() {
        return postModelArrayList.size();
    }

}
