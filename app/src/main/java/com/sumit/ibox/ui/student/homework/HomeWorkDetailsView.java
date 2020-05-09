package com.sumit.ibox.ui.student.homework;

import com.sumit.ibox.model.AlertDialog;
import android.app.DownloadManager;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.model.StudentHomeWorkListViewData;

import java.io.InputStream;

public class HomeWorkDetailsView extends AppCompatActivity {

    Context context;
    StudentHomeWorkListViewData homeworkData;
    ImageView close, image;
    TextView hwDate, lastDate, teacher, subject, description;
    ProgressDialog loading;
    AlertDialog errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work_view);

        init();
        setupUi();
    }

    private void init() {
        context = this;
        homeworkData = (StudentHomeWorkListViewData) getIntent().getSerializableExtra(Constant.KEY_HOMEWORK_DATA);
        close = findViewById(R.id.homework_close);
        hwDate = findViewById(R.id.homework_hw_date);
        lastDate = findViewById(R.id.homework_last_date);
        teacher = findViewById(R.id.homework_teacher);
        subject = findViewById(R.id.homework_subject);
        description = findViewById(R.id.homework_description);
        image = findViewById(R.id.homework_image);
    }

    private void setupUi() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hwDate.setText(homeworkData.getHwDate());
        lastDate.setText(homeworkData.getSubmissionDate());
        teacher.setText(homeworkData.getTeacherId());
        subject.setText(homeworkData.getSubject());
        description.setText(homeworkData.getDescription());
        loading = new ProgressDialog(context);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        errorDialog = new AlertDialog.Builder(context)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        errorDialog.dismiss();
                    }
                })
                .create();
        new FetchImage(image).execute(Constant.ROOT+homeworkData.getImage_link());
    }

    public void downloadClick(View view) {
        Utils.showShortToast(context, getString(R.string.downloading));
        downloadImage(Constant.ROOT+homeworkData.getImage_link());
    }

    private void downloadImage(String url){
        try {
            DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(getString(R.string.homework));
            request.setDescription("Downloading");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            String session = String.valueOf(System.currentTimeMillis());
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, session + "homework.png");

            downloadmanager.enqueue(request);
            Utils.showShortToast(context, getString(R.string.downloaded));
        }catch (Exception e){
            Utils.logPrint(getClass(), "error downloading", Log.getStackTraceString(e));
            Utils.showShortToast(context, getString(R.string.download_failed));
        }
    }

    private class FetchImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public FetchImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.show();
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                errorDialog.setMessage(getString(R.string.image_loading_failed));
                errorDialog.show();
                Utils.logPrint(getClass(),"Image Loading error",Log.getStackTraceString(e));
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            loading.dismiss();
            bmImage.setImageBitmap(result);
        }
    }

}
