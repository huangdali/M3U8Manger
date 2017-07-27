package com.hdl.m3u8demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hdl.m3u8.M3U8Manger;
import com.hdl.m3u8.bean.M3U8;
import com.hdl.m3u8.bean.M3U8Listener;
import com.hdl.m3u8demo.runtimepermissions.PermissionsManager;
import com.hdl.m3u8demo.runtimepermissions.PermissionsResultAction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 请求所有必要的权限----
         */
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDownload(View view) {
        String url = "http://gwell-oss-test1.oss-cn-shenzhen.aliyuncs.com/video-123yun/2017-07-20%2000%3A00%3A00.m3u8?Expires=1501208683&OSSAccessKeyId=LTAIAxqhixFoJsvp&Signature=C5FdEq8HHfefwBZhmw%2B8Lu7wnkw%3D";
        M3U8Manger.getInstance()
                .setUrl(url)
                .setSaveFilePath("/sdcard/11/" + System.currentTimeMillis() + ".ts")
                .download(new M3U8Listener() {
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

                    }
                });
    }
    public void onDownload1(View view) {
        String url = "http://gwell-oss-test1.oss-cn-shenzhen.aliyuncs.com/video-123yun/2017-07-20%2000%3A00%3A00.m3u8?Expires=1501208683&OSSAccessKeyId=LTAIAxqhixFoJsvp&Signature=C5FdEq8HHfefwBZhmw%2B8Lu7wnkw%3D";
        M3U8Manger.getInstance()
                .setUrl(url)
                .setSaveFilePath("/sdcard/11/" + System.currentTimeMillis() + ".ts")
                .download(1500480000074l,1500480080164l,new M3U8Listener() {
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

                    }
                });
    }

    public void onGetInfo(View view) {
        String url = "http://gwell-oss-test1.oss-cn-shenzhen.aliyuncs.com/video-123yun/2017-07-17%2017%3A00%3A00.m3u8?Expires=1501036405&OSSAccessKeyId=LTAIAxqhixFoJsvp&Signature=zfkPv4QtRNPETyL31BUzx4U%2FpHU%3D";
        M3U8Manger.getInstance()
                .setUrl(url)
                .getM3U8(new M3U8Listener() {
                    @Override
                    public void onStart() {
                        Log.e("hdltag", "onStart(MainActivity.java:75):开始了" );
                    }

                    @Override
                    public void onError(Throwable errorMsg) {
                        Log.e("hdltag", "onStart(MainActivity.java:75):出错了"+errorMsg );
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("hdltag", "onStart(MainActivity.java:75):完成了" );
                    }

                    @Override
                    public void onM3U8Info(M3U8 m3U8) {
                        Log.e("hdltag", "onStart(MainActivity.java:75):拿到结果了"+m3U8 );
                        Log.e("hdltag", "onM3U8Info(MainActivity.java:91):" + m3U8.getTsList());
                    }
                });
    }

    public void onStop(View view) {
        M3U8Manger.getInstance().stop();
    }

}
