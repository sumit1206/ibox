package com.sumit.ibox.ui.gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.sumit.ibox.R;
import com.sumit.ibox.model.ZoomableImageView;

public class PictureActivity extends AppCompatActivity {

    ZoomableImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        imageView = findViewById(R.id.gallery_full_photo);
        Bitmap bitmap = getIntent().getParcelableExtra("img");
        imageView.setImageBitmap(bitmap);
    }
}
