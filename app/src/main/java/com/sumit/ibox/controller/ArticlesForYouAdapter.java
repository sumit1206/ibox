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

import java.io.InputStream;
import java.util.List;

public class ArticlesForYouAdapter extends RecyclerView.Adapter<ArticlesForYouAdapter.ArticlesViewHolder> {

    private Context blogContext;
    private List <ArticlesForYouData> blogForYouData;

    public ArticlesForYouAdapter(Context blogContext, List<ArticlesForYouData> blogForYouData) {
        this.blogContext = blogContext;
        this.blogForYouData = blogForYouData;
    }

    @NonNull
    @Override
    public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blog_single_card, parent, false);
        return new ArticlesForYouAdapter.ArticlesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesViewHolder holder, int position) {
        ArticlesForYouData articlesForYouData = blogForYouData.get(position);
        holder.blogTitle.setText(articlesForYouData.getBlogTitle());
        holder.uploaderName.setText(articlesForYouData.getBlogUploaderName());
        holder.blogPostDate.setText(articlesForYouData.getBlogPostDate());
//        holder.blogThumbnail.setImageResource(articlesForYouData.getBlogThumbnail());
        FetchImage fetchImage = new FetchImage(holder.blogThumbnail, articlesForYouData.getUrl());
        fetchImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public int getItemCount()
    {
        return blogForYouData.size();
    }

    public class ArticlesViewHolder extends RecyclerView.ViewHolder {
        TextView blogTitle,uploaderName,blogPostDate;
        ImageView blogThumbnail;
        public ArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
            blogTitle = itemView.findViewById(R.id.blog_title);
            uploaderName = itemView.findViewById(R.id.blogUploaderName);
            blogPostDate = itemView.findViewById(R.id.blog_posted_date);
            blogThumbnail = itemView.findViewById(R.id.thumbnail_blog);
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
