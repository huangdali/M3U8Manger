package com.hdl.m3u8demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hdl.elog.ELog;
import com.hdl.m3u8.M3U8DownloadTask;
import com.hdl.m3u8.M3U8InfoManger;
import com.hdl.m3u8.M3U8LiveManger;
import com.hdl.m3u8.bean.M3U8;
import com.hdl.m3u8.bean.OnDownloadListener;
import com.hdl.m3u8.bean.OnM3U8InfoListener;
import com.hdl.m3u8.utils.NetSpeedUtils;

import hdl.com.lib.runtimepermissions.HPermissions;
import hdl.com.lib.runtimepermissions.PermissionsResultAction;

public class Main2Activity extends AppCompatActivity {
    //url随时可能失效
    private String url = "http://tvbilive7-i.akamaihd.net/hls/live/494651/CJHK4/CJHK4-06.m3u8";
//    private String url = "http://oss.cloudlinks.cn/07754326_7844998_1001/1520265609466.m3u8?Expires=1520414234&OSSAccessKeyId=LTAIAxqhixFoJsvp&Signature=d0iBMPf%2BeaVZfOUJ32EhesDpuWE%3D&x-oss-process=hls%2Fsign";

    private TextView tvSpeed1;
    private EditText etUrl;
    private TextView tvConsole;
    private TextView tvSaveFilePathTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        requestPermission();
        tvSpeed1 = (TextView) findViewById(R.id.tv_speed1);
        etUrl = (EditText) findViewById(R.id.et_url);
        etUrl.setText("http://tvbilive7-i.akamaihd.net/hls/live/494651/CJHK4/CJHK4-06.m3u8");
        tvConsole = (TextView) findViewById(R.id.tv_console);
        tvSaveFilePathTip= (TextView) findViewById(R.id.tv_savepath_tip);
    }

    private void requestPermission() {
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

    public void onGetInfo(View view) {
        M3U8InfoManger.getInstance().getM3U8Info(url, new OnM3U8InfoListener() {
            @Override
            public void onSuccess(M3U8 m3U8) {
                tvConsole.append("\n\n获取成功了" + m3U8);
                ELog.e("获取成功了" + m3U8);
            }

            @Override
            public void onStart() {
                tvConsole.append("\n\n开始获取信息");
                ELog.e("开始获取信息");
            }

            @Override
            public void onError(Throwable errorMsg) {
                tvConsole.append("\n\n出错了" + errorMsg);
                ELog.e("出错了" + errorMsg);
            }
        });
    }

    //上一秒的大小
    private long lastLength = 0;
    M3U8DownloadTask task1 = new M3U8DownloadTask("1001");

    public void onDownload(View view) {
//        url = etUrl.getText().toString();
        task1.setSaveFilePath("/sdcard/111/" + System.currentTimeMillis() + ".ts");
        tvSaveFilePathTip.setText("文件保存在：/sdcard/111/" + System.currentTimeMillis() + ".ts");
        task1.download(url, new OnDownloadListener() {
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
        M3U8LiveManger.getInstance().stop();
    }

    /**
     * 当前正在下载的视频
     */
    private int curTsIndex;

    public void onLiveDownload(View view) {
//        String url = "http://tvbilive7-i.akamaihd.net/hls/live/494651/CJHK4/CJHK4-06.m3u8";
        String url = etUrl.getText().toString().trim();
        String toFile="/sdcard/" + System.currentTimeMillis() + ".ts";
        tvSaveFilePathTip.setText("缓存目录在：/sdcard/11m3u8/\n最终导出的缓存文件在："+toFile);
        M3U8LiveManger.getInstance()
                .setTempDir("/sdcard/11m3u8/")
                .setSaveFile(toFile)//（设置导出缓存文件）必须以.ts结尾
                .caching(url, new OnDownloadListener() {
                    @Override
                    public void onDownloading(long itemFileSize, int totalTs, int curTs) {
                        curTsIndex = curTs;
                        tvConsole.append(String.format("\n\n下载中.....开始下载第 %s 个视频了", curTs));
//                        tvConsole.setText("第 " + curTs + " 个视频下载中\n\n" + tvConsole.getText().toString());
                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onProgress(long curLength) {
                        if (curLength - lastLength > 0) {
                            final String speed = NetSpeedUtils.getInstance().displayFileSize(curLength - lastLength) + "/s";
                            ELog.e(task1.getTaskId() + "speed = " + speed);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ELog.e("更新了");
                                    tvSpeed1.setText(speed + "( 第" + (curTsIndex + 1) + "个视频 )");
                                    ELog.e(tvSpeed1.getText().toString());
                                }
                            });
                            lastLength = curLength;
                        }
                    }

                    @Override
                    public void onStart() {
                        tvConsole.append("\n\n开始缓存");
                    }

                    @Override
                    public void onError(Throwable errorMsg) {
                        tvConsole.append("\n\n缓存出错了" + errorMsg);
                    }
                });
    }

    public void onGetLiveCache(View view) {
        String currentTs = M3U8LiveManger.getInstance().getCurrentTs();
        tvConsole.append("\n\n缓存完成了，已经存至：" + currentTs);
        Log.e("hdltag", "onGetLiveCache(Main2Activity.java:151): currentTs = " + currentTs);
    }
}
