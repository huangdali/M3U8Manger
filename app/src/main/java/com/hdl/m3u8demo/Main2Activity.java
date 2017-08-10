package com.hdl.m3u8demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hdl.elog.ELog;
import com.hdl.m3u8.M3U8DownloadManger;
import com.hdl.m3u8.M3U8InfoManger;
import com.hdl.m3u8.bean.M3U8;
import com.hdl.m3u8.bean.OnDownloadListener;
import com.hdl.m3u8.bean.OnM3U8InfoListener;
import com.hdl.m3u8demo.runtimepermissions.PermissionsManager;
import com.hdl.m3u8demo.runtimepermissions.PermissionsResultAction;

public class Main2Activity extends AppCompatActivity {
    private String url = "http://gwell-oss-test1.oss-cn-shenzhen.aliyuncs.com/video-123yun/2017-07-20%2000%3A00%3A00.m3u8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        requestPermission();

    }

    private void requestPermission() {
        /*
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

    public void onGetInfo(View view) {
        M3U8InfoManger.getInstance().getM3U8Info(url, new OnM3U8InfoListener() {
            @Override
            public void onSuccess(M3U8 m3U8) {
                ELog.e("获取成功了" + m3U8);
            }

            @Override
            public void onStart() {
                ELog.e("开始获取信息");
            }

            @Override
            public void onError(Throwable errorMsg) {
                ELog.e("出错了" + errorMsg);
            }
        });
    }

    public void onDownload(View view) {
        M3U8DownloadManger manger = new M3U8DownloadManger();
        manger.download(url, new OnDownloadListener() {
            @Override
            public void onDownloading(long itemFileSize, int totalTs, int curTs) {
                ELog.e("下载中.....itemFileSize=" + itemFileSize + "\ttotalTs=" + totalTs + "\tcurTs=" + curTs);
            }

            @Override
            public void onStart() {
                ELog.e("开始下载了");
            }

            @Override
            public void onError(Throwable errorMsg) {
                ELog.e("出错了" + errorMsg);
            }
        });
    }
}
