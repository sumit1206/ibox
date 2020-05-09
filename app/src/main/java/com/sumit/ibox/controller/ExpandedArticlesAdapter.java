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
import com.sumit.ibox.model.ArticlesForYouData;
import com.sumit.ibox.model.GalleryData;

import java.io.InputStream;
import java.util.List;

public class ExpandedArticlesAdapter extends RecyclerView.Adapter<ExpandedArticlesAdapter.MyViewHolder> {

    private Context blogContext;
    private List<ArticlesForYouData> blogForYouData;

    public ExpandedArticlesAdapter(Context blogContext, List<ArticlesForYouData> blogForYouData) {
        this.blogContext = blogContext;
        this.blogForYouData = blogForYouData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blog_expended_view, parent, false);
        return new ExpandedArticlesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ArticlesForYouData articlesForYouData = blogForYouData.get(position);
        holder.blogTitle.setText(articlesForYouData.getBlogTitle());
        holder.blogUploaderName.setText(articlesForYouData.getBlogUploaderName());
        holder.blogUploadDate.setText(articlesForYouData.getBlogPostDate());
        holder.blogUploaderThumb.setImageResource(articlesForYouData.getUploaderThumb());

        FetchImage fetchImage = new FetchImage(holder.blogImage, articlesForYouData.getUrl());
        fetchImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public int getItemCount() {
        return blogForYouData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView blogTitle,blogUploaderName,blogUploadDate;
        ImageView blogImage,blogUploaderThumb;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            blogTitle = itemView.findViewById(R.id.blogTitleExpanded);
            blogUploaderName = itemView.findViewById(R.id.blogUploaderName);
            blogUploadDate = itemView.findViewById(R.id.blogUploadDate);
            blogImage = itemView.findViewById(R.id.blogImage);
            blogUploaderThumb = itemView.findViewById(R.id.blogUploaderThumb);
        }
    }

    private class FetchImage extends AsyncTask<Void, Void, Bitmap> {
        ImageView bmImage;
        String url;

        public FetchImage(ImageView bmImage, String url) {
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
                mIcon11 = BitmapFactory.decodeResource(blogContext.getResources(),
                        R.drawable.ic_cloud_off_black_24dp);
                Utils.logPrint(getClass(),"Image Loading error", Log.getStackTraceString(e));
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
