package com.example.marvelcomicsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<ComicModel> allComicsList;

    public RecycleViewAdapter(Context context, ArrayList<ComicModel> allComicsList) {

        this.context = context;
        this.allComicsList = allComicsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout (giving look to rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item, parent, false);
        return new RecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // assigning values to the views we created in recycler_view_item, based on the position the recycler view
        holder.title.setText(allComicsList.get(position).getTitle());
        holder.description.setText(allComicsList.get(position).getDescription());
        Picasso.get().load(allComicsList.get(position).getThumbnail()).resize(300,450).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        //number of items to display
        return allComicsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title, description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.comic_image);
            title = itemView.findViewById(R.id.title_txt);
            description = itemView.findViewById(R.id.description_txt);
        }
    }
}
