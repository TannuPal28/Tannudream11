//package com.elevendreamer.Adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.annotation.DrawableRes;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.elevendreamer.Bean.Dummy;
//import com.elevendreamer.R; // ✅ Correct import
//
//import java.util.List;
//
//public class Image_Adapter extends RecyclerView.Adapter<Image_Adapter.ImageViewHolder> {
//
//    private Context context;
//    private List<Dummy> imageList;
//
//    public Image_Adapter(Context context, List<Dummy> imageList) {
//        this.context = context;
//        this.imageList = imageList;
//    }
//
//    @NonNull
//    @Override
//    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.items_recylerview1, parent, false);
//        return new ImageViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
//        holder.imageView.setImageResource(R.drawable.recylerview_img);
//    }
//
//    @Override
//    public int getItemCount() {
//        return imageList.size();
//    }
//
//    public static class ImageViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//
//        public ImageViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.imageView); // ✅ must match ID from item_image.xml
//        }
//    }
//}
package com.elevendreamer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elevendreamer.Bean.Dummy;
import com.elevendreamer.R;

import java.util.List;

public class Image_Adapter extends RecyclerView.Adapter<Image_Adapter.ImageViewHolder> {

    private Context context;
    private List<Dummy> imageList;

    public Image_Adapter(Context context, List<Dummy> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_recylerview1, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Dummy item = imageList.get(position);
        holder.imageView.setImageResource(item.getImg());
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            // ✅ Must match ID in image_item_layout.xml
        }
    }
}
