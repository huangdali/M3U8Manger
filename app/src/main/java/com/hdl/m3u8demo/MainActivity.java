package com.hdl.m3u8demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hdl.m3u8.M3U8Manger;
import com.hdl.m3u8.bean.M3U8;
import com.hdl.m3u8.bean.M3U8Listener;

import hdl.com.lib.runtimepermissions.HPermissions;
import hdl.com.lib.runtimepermissions.PermissionsResultAction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 请求所有必要的权限----
         */
        /*
         * 请求所有必要的权限----
         */
        HPermissions.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(String permission) {

            }
        });
    }

    public void onDownload(View view) {
        String url = "xxx";
        M3U8Manger.getInstance()
                .setUrl(url)
                .setSaveFilePath("/sdcard/11/" + System.currentTimeMillis() + ".ts")
                .download(new M3U8Listener() {
                    @Override
                    public void onStart() {
                        Log.e("hdltag", "onStart(MainActivity.java:44):开始下载了");
                        Toast.makeText(MainActivity.this, "开始下载了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable errorMsg) {
                        Log.e("hdltag", "onError(MainActivity.java:51):下载出错了" + errorMsg);
                        Toast.makeText(MainActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("hdltag", "onCompleted(MainActivity.java:57):下载完成了");
                        Toast.makeText(MainActivity.this, "完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoadFileSizeForItem(long fileSize) {
                        Log.e("hdltag", "onLoadFileSizeForItem(MainActivity.java:63):fileSize=" + fileSize + " b");
                    }
                });
    }

    public void onDownload1(View view) {
        String url = "http://gwell-oss-test1.oss-cn-shenzhen.aliyuncs.com/video-123yun/1500541892-1500542032.m3u8";
        M3U8Manger.getInstance()
                .setUrl(url)
                .setSaveFilePath("/sdcard/11/" + System.currentTimeMillis() + ".ts")
                .download(1500480050183l, 1500480150040l, new M3U8Listener() {
                    @Override
                    public void onStart() {
                        Log.e("hdltag", "onStart(MainActivity.java:23):开始下载了");
                        Toast.makeText(MainActivity.this, "开始下载了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable errorMsg) {
                        Log.e("hdltag", "onError(MainActivity.java:28):下载出错了" + errorMsg);
                        Toast.makeText(MainActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("hdltag", "onCompleted(MainActivity.java:33):下载完成了");
                        Toast.makeText(MainActivity.this, "完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onM3U8Info(M3U8 m3U8) {
                        Log.e("hdltag", "onM3U8Info(MainActivity.java:93):" + m3U8);
                    }

                    @Override
                    public void onDownloadingProgress(int total, int progress) {
                        Log.e("hdltag", "onDownloadingProgress(MainActivity.java:126):" + total + " ------ " + progress);
                    }
                });
    }

    public void onGetInfo(View view) {
        String url = "http://gwell-oss-test1.oss-cn-shenzhen.aliyuncs.com/video-123yun/2017-07-20%2017%3A00%3A00.m3u8?Expires=1501502790&OSSAccessKeyId=LTAIAxqhixFoJsvp&Signature=MfjA%2FuD7pWmoVXUgO3Uwn0YiOQU%3D";
        M3U8Manger.getInstance()
                .setUrl(url)
                .getM3U8(new M3U8Listener() {
                    @Override
                    public void onStart() {
                        Log.e("hdltag", "onStart(MainActivity.java:108):开始了");
                    }

                    @Override
                    public void onError(Throwable errorMsg) {
                        Log.e("hdltag", "onStart(MainActivity.java:113):出错了" + errorMsg);
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("hdltag", "onStart(MainActivity.java:119):完成了");
                    }

                    @Override
                    public void onM3U8Info(M3U8 m3U8) {
                        Log.e("hdltag", "onStart(MainActivity.java:124):拿到结果了" + m3U8);
                        Log.e("hdltag", "onM3U8Info(MainActivity.java:125):" + m3U8.getTsList());
                    }

                    @Override
                    public void onDownloadingProgress(int total, int progress) {
                        Log.e("hdltag", "onDownloadingProgress(MainActivity.java:130):" + total + " ------ " + progress);
                    }
                });
    }

    public void onStop(View view) {
        M3U8Manger.getInstance().stop();
    }

}
