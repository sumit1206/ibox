package com.sumit.ibox.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.R;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.model.GalleryData;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    TouchEvents touchEvents = null;
    ArrayList<GalleryData>galleryDataListData;
    Context context;

    public GalleryAdapter(ArrayList<GalleryData> galleryDataListData, Context context, TouchEvents touchEvents) {
        this.galleryDataListData = galleryDataListData;
        this.context = context;
        this.touchEvents = touchEvents;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_gallery_item, parent, false);
        return new GalleryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        GalleryData galleryData = galleryDataListData.get(position);
        holder.imageTitle.setText(galleryData.getTitle());
        FetchImage fetchImage = new FetchImage(galleryData, holder.galleryImage, galleryData.getImageUrl());
        fetchImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        holder.galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryData galleryData = galleryDataListData.get(position);
                touchEvents.onClick(galleryData.getBitmap());
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryDataListData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView galleryImage;
        TextView imageTitle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            galleryImage= itemView.findViewById(R.id.gallery_image_item);
            imageTitle = itemView.findViewById(R.id.image_title);
        }
    }

    private class FetchImage extends AsyncTask<Void, Void, Bitmap> {
        GalleryData galleryData;
        ImageView bmImage;
        String url;

        public FetchImage(GalleryData galleryData, ImageView bmImage, String url) {
            this.galleryData = galleryData;
            this.bmImage = bmImage;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            String urldisplay = url;
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                mIcon11 = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_cloud_off_black_24dp);
                Utils.logPrint(getClass(),"Image Loading error", Log.getStackTraceString(e));
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            galleryData.setBitmap(result);
            bmImage.setImageBitmap(result);
        }
    }

    public interface TouchEvents{
        void onClick(Bitmap bitmap);
    }

}
