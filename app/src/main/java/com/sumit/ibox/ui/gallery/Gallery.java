package com.sumit.ibox.ui.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.model.AlertDialog;
import android.app.Dialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.GalleryAdapter;
import com.sumit.ibox.model.GalleryData;
import com.sumit.ibox.model.ZoomableImageView;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Gallery extends AppCompatActivity {

    Context context;
    RecyclerView galleryRecyclerView;
    ArrayList<GalleryData> galleryDataList = new ArrayList<>();
    GalleryAdapter galleryAdapter;
    Toolbar galleryToolbar;
    ProgressDialog loading;
    AlertDialog errorDialog;

    VolleyCallback galleryCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            try {
                GalleryData galleryData;
                JSONObject jsonObject = (JSONObject) result;
                int success = jsonObject.getInt("success");
                if(success == 1){
                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        "sl_no":"1","title":"Title","description":"Description","image_link":"https:\/\/i
                        String serialNo = jsonObject1.getString("sl_no");
                        String title = jsonObject1.getString("title");
                        String description = jsonObject1.getString("description");
                        String imageLink = jsonObject1.getString("image_link");
                        galleryData = new GalleryData(serialNo, title, description, imageLink);
                        galleryDataList.add(galleryData);
                    }
                }
                galleryAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Utils.errorLog(getClass(),"Error parsing json",e);
                errorDialog.setMessage(getString(R.string.error_occurred));
                errorDialog.show();
            }
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.no_photos));
            errorDialog.show();
        }

        @Override
        public void onCatch(JSONException e) {
            loading.dismiss();
            Utils.logPrint(getClass(),"response error2", Log.getStackTraceString(e));
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
        }

        @Override
        public void onError(VolleyError e) {
            loading.dismiss();
            Utils.logPrint(getClass(),"no response error", Log.getStackTraceString(e));
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
        }
    };

    GalleryAdapter.TouchEvents touchEvents = new GalleryAdapter.TouchEvents() {
        @Override
        public void onClick(Bitmap bitmap) {
            showPopup(bitmap);
        }
    };

    private void showPopup(Bitmap bitmap){
        Dialog alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.gallery_image_popup);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageView = alertDialog.findViewById(R.id.gallery_popup_image);
        imageView.setImageBitmap(bitmap);
        alertDialog.show();

//        Intent intent = new Intent(context, PictureActivity.class);
//        intent.putExtra("img",bitmap);
//        startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        init();
        toolbarSetUp();
        setUpRecyclerView();
        fetchGalleryData();
    }

    private void init() {
        context = this;
        loading = new ProgressDialog(context);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        errorDialog = new AlertDialog.Builder(context)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        errorDialog.cancel();
                    }
                })
                .create();
    }

    private void toolbarSetUp(){
        galleryToolbar = findViewById(R.id.gallery_toolbar);
        galleryToolbar.setTitle(R.string.gallery);
        galleryToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        galleryToolbar.setNavigationIcon(R.drawable.arrow_left);
        galleryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRecyclerView(){
        galleryRecyclerView = findViewById(R.id.gallery_recycler_view);
        galleryAdapter = new GalleryAdapter(galleryDataList, this, touchEvents);
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        galleryRecyclerView.setLayoutManager(layoutManager);
        galleryRecyclerView.setAdapter(galleryAdapter);
    }

    private void fetchGalleryData(){
        loading.show();
        Perform.fetchGallery(context, galleryCallback);
    }

    public class AutoFitGridLayoutManager extends GridLayoutManager {
        private int columnWidth;
        private boolean columnWidthChanged = true;

        public AutoFitGridLayoutManager(Context context, int columnWidth) {
            super(context, 1);

            setColumnWidth(columnWidth);
        }

        public void setColumnWidth(int newColumnWidth) {
            if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
                columnWidth = newColumnWidth;
                columnWidthChanged = true;
            }
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            if (columnWidthChanged && columnWidth > 0) {
                int totalSpace;
                if (getOrientation() == VERTICAL) {
                    totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
                } else {
                    totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
                }
                int spanCount = Math.max(1, totalSpace / columnWidth);
                setSpanCount(spanCount);
                columnWidthChanged = false;
            }
            super.onLayoutChildren(recycler, state);
        }
    }
}
