package com.zhi.www.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {

    /*将图片下载到本地的地址*/
    private String imageuri = "http://img.ads.csdn.net/2016/201605271539572727.png";
    /*将图片显示到UI界面的地址*/
    private String imageuri1 = "http://img.ads.csdn.net/2016/201605271539572727.png";

    private Button mBtnDisplay, mBtnDownload;
    private ImageView mIvImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnDisplay = (Button) findViewById(R.id.btn_display);
        mBtnDownload = (Button) findViewById(R.id.btn_download);
        mIvImage = (ImageView) findViewById(R.id.iv_image);
        mBtnDownload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new MyTask().execute();
            }
        });

        mBtnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new showImage().execute();
            }
        });
    }

    public class showImage extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = getHttpBitmap(imageuri);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            mIvImage.setImageBitmap(result);

        }

        private Bitmap getHttpBitmap(String url) {
            Bitmap bitmap = null;
            try {
                URL httpUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    public class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(imageuri1);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputstream = connection.getInputStream();
                BufferedInputStream bufferedinputstreamis = new BufferedInputStream(inputstream);
                File file = createFile();
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(fileoutputstream);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = bufferedinputstreamis.read(buffer)) != -1) {
                    bufferedoutputstream.write(buffer, 0, len);
                }

                bufferedoutputstream.flush();
                bufferedinputstreamis.close();
                bufferedoutputstream.close();
                inputstream.close();
                fileoutputstream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
        }
    }

    private File createFile() {
        File path = Environment.getExternalStorageDirectory();
        File file = new File(path.getPath());
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File dir = new File(path, "dowmload.jpg");
        if (!dir.exists()) {
            try {
                dir.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dir;
    }
}