package com.example.ngouser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<CategoryModel> categoryModelArrayList;


    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModelArrayList) {
        this.context = context;
        this.categoryModelArrayList = categoryModelArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CategoryModel categoryModel;
        private TextView areaNameTv;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            areaNameTv = itemView.findViewById(R.id.row_layout_category_name);

        }

        public void setData(CategoryModel data) {
            this.categoryModel = data;
            areaNameTv.setText(data.getCategoryName());


        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_category, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        CategoryModel categoryModel = categoryModelArrayList.get(i);
        myViewHolder.setData(categoryModel);
    }


    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }

}
