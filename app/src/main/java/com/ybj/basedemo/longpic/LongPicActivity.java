package com.ybj.basedemo.longpic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.ybj.basedemo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LongPicActivity extends AppCompatActivity {

    private ScrollView mScrollView;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_pic);

        mScrollView = (ScrollView)findViewById(R.id.scrollView);
        mBtn = (Button)findViewById(R.id.btn_save);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = createBitmapByScrollView(mScrollView);
                String path = Environment.getExternalStorageDirectory() + "/longPic.jpg";
                File file = new File(path);
                if (file.isDirectory()) {//如果是目录不允许保存
                    return;
                }
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                    outputStream.flush();
                    MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),path,null);
                    LongPicActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    if(outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    public static Bitmap createBitmapByScrollView(ScrollView scrollView){
        int hight = 0;
        Bitmap bitmap = null;
        hight += scrollView.getChildAt(0).getHeight();
        scrollView.getChildAt(0).setBackgroundColor(
                Color.parseColor("#ffffff"));
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), hight,
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

}
