package com.hdl.m3u8demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hdl.elog.ELog;
import com.hdl.m3u8.M3U8DownloadTask;
import com.hdl.m3u8.M3U8InfoManger;
import com.hdl.m3u8.bean.M3U8;
import com.hdl.m3u8.bean.OnDownloadListener;
import com.hdl.m3u8.bean.OnM3U8InfoListener;
import com.hdl.m3u8.utils.NetSpeedUtils;
import com.hdl.m3u8demo.runtimepermissions.PermissionsManager;
import com.hdl.m3u8demo.runtimepermissions.PermissionsResultAction;

public class Main2Activity extends AppCompatActivity {
    private String url = "http://gwell-oss-test1.oss-cn-shenzhen.aliyuncs.com/video-123yun/2017-07-20%2000%3A00%3A00.m3u8";
    private TextView tvSpeed1;
    private EditText etUrl;
    private TextView tvConsole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        requestPermission();
        tvSpeed1 = (TextView) findViewById(R.id.tv_speed1);
        etUrl = (EditText) findViewById(R.id.et_url);
        tvConsole = (TextView) findViewById(R.id.tv_console);
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

    //上一秒的大小
    private long lastLength = 0;
    M3U8DownloadTask task1 = new M3U8DownloadTask("1001");

    public void onDownload(View view) {
        url = etUrl.getText().toString();
        task1.setSaveFilePath("/sdcard/111/" + System.currentTimeMillis() + ".ts");
        task1.download(this.url, new OnDownloadListener() {
            @Override
            public void onDownloading(final long itemFileSize, final int totalTs, final int curTs) {
                ELog.e(task1.getTaskId() + "下载中.....itemFileSize=" + itemFileSize + "\ttotalTs=" + totalTs + "\tcurTs=" + curTs);
                tvConsole.append("\n\n下载中....." + itemFileSize + "\t" + totalTs + "\t" + curTs);
            }

            /**
             * 下载成功
             */
            @Override
            public void onSuccess() {
                ELog.e(task1.getTaskId() + "下载完成了");
                tvConsole.append("\n\n下载完成");
            }

            /**
             * 当前的进度回调
             *
             * @param curLenght
             */
            @Override
            public void onProgress(final long curLenght) {
                if (curLenght - lastLength > 0) {
                    final String speed = NetSpeedUtils.getInstance().displayFileSize(curLenght - lastLength) + "/s";
                    ELog.e(task1.getTaskId() + "speed = " + speed);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ELog.e("更新了");
                            tvSpeed1.setText(speed);
                            ELog.e(tvSpeed1.getText().toString());
                        }
                    });
                    lastLength = curLenght;

                }
            }

            @Override
            public void onStart() {
                ELog.e(task1.getTaskId() + "开始下载了");
                tvConsole.append("\n\n开始下载");
            }

            @Override
            public void onError(Throwable errorMsg) {
                tvConsole.append("\n\n出错了" + errorMsg);
                ELog.e(task1.getTaskId() + "出错了" + errorMsg);
            }
        });
    }

    public void onStopTask1(View view) {
        task1.stop();
    }
}
