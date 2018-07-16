package com.ybj.createpdf;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Create pdf file step:
 * 1.创建一个文档对象
 * 2.使用文档对象的实例创建一个PdfWriter实例。需要Document对象和FileOutputStream对象。
 * 3.通过调用open（）方法打开文档
 * 4.使用add（）方法将内容写入PDF文件
 * 5.通过调用close（）方法关闭文档
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * 创建PDF文件
     *
     * @param view
     */
    public void createPdf(View view) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            pdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    pdf();
                } else {
                    Log.e("onRequestPermissions", "no permissions");
                }
                break;
        }
    }

    private void pdf() {
        EditText editText = (EditText) findViewById(R.id.txt_input);
        Document document = new Document();
        String outPath = Environment.getExternalStorageDirectory() + "/mypdf1.pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(outPath));
            document.open();
            document.add(new Paragraph(editText.getText().toString()));
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
