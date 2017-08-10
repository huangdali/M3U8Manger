package com.hdl.m3u8;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hdl.m3u8.bean.M3U8;
import com.hdl.m3u8.bean.M3U8Ts;
import com.hdl.m3u8.bean.OnDownloadListener;
import com.hdl.m3u8.bean.OnM3U8InfoListener;
import com.hdl.m3u8.utils.MUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * M3U8下载管理器
 * Created by HDL on 2017/8/10.
 */

public class M3U8DownloadManger {
    private OnDownloadListener onDownloadListener;
    private static final int WHAT_ON_ERROR = 438;
    private static final int WHAT_ON_NEXT = 439;
    private static final int WHAT_ON_SUCCESS = 440;
    private String tempDir = Environment.getExternalStorageDirectory().getPath() + File.separator + "m3u8temp";
    private String saveFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "11m3u8";
    private int curTs = 0;
    private int totalTs = 0;
    private long itemFileSize = 0;
    private boolean isRunning = false;
    /**
     * 线程池最大线程数，默认为5
     */
    private int threadCount = 5;
    private ExecutorService executor;//线程池
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_ON_ERROR:
                    onDownloadListener.onError((Throwable) msg.obj);
                    break;
                case WHAT_ON_NEXT:
                    onDownloadListener.onDownloading((Long) msg.obj, msg.arg1, msg.arg2);
                    break;
                case WHAT_ON_SUCCESS:
                    onDownloadListener.onSuccess();
                    break;
            }
        }
    };

    /**
     * 开始下载
     *
     * @param url
     * @param onDownloadListener
     */
    public void download(String url, OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
        if (!isRunning()) {
            getM3U8Info(url);
        } else {
            handlerError(new Throwable("Task running"));
        }
    }

    /**
     * 获取任务是否正在执行
     *
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * 先获取m3u8信息
     *
     * @param url
     */
    private void getM3U8Info(String url) {
        M3U8InfoManger.getInstance().getM3U8Info(url, new OnM3U8InfoListener() {
            @Override
            public void onSuccess(M3U8 m3U8) {
                try {
                    Log.e("hdltag", "onSuccess(M3U8DownloadManger.java:74):" + m3U8);
                    startDownload(m3U8);
                    Log.e("hdltag", "onSuccess(M3U8DownloadManger.java:79):startDownload 之后了");
                    if (executor != null) {
                        executor.shutdown();//下载完成之后要关闭线程池
                    }
                    while (executor != null && !executor.isTerminated()) {
                        Thread.sleep(100);
                    }
                    String saveFileName = System.currentTimeMillis() + ".ts";
                    String tempSaveFile = tempDir + File.separator + saveFileName;//生成临时文件
                    MUtils.merge(m3U8, tempSaveFile, tempDir);//合并ts
                    //移动到指定的目录
                    MUtils.moveFile(tempSaveFile, saveFilePath + File.separator + saveFileName);//移动到指定文件夹
                    MUtils.clearDir(new File(tempDir));//清空一下临时文件
                    Log.e("hdltag", "onSuccess(M3U8DownloadManger.java:88):下载完成了");
                    mHandler.sendEmptyMessage(WHAT_ON_SUCCESS);
                    isRunning = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    handlerError(e);
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    handlerError(e);
                }
            }

            @Override
            public void onStart() {
                onDownloadListener.onStart();
                isRunning = true;
            }

            @Override
            public void onError(Throwable errorMsg) {
                handlerError(errorMsg);
            }
        });
    }

    /**
     * 开始下载
     *
     * @param m3U8
     */
    private void startDownload(final M3U8 m3U8) {
        //需要加上当前时间作为文件夹（由于合并时是根据文件夹来合并的，合并之后需要删除所有的ts文件，这里用到了多线程，所以需要按文件夹来存ts）
        tempDir += File.separator + System.currentTimeMillis();
        final File dir = new File(tempDir);
        //没有就创建
        if (!dir.exists()) {
            dir.mkdirs();
        } else {
            //有就清空内容
            MUtils.clearDir(dir);
        }
        totalTs = m3U8.getTsList().size();
        if (executor != null && executor.isTerminated()) {
            executor.shutdownNow();
            executor = null;
        }
        executor = Executors.newFixedThreadPool(threadCount);
        final String basePath = m3U8.getBasepath();
        for (final M3U8Ts m3U8Ts : m3U8.getTsList()) {//循环下载
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    File file = new File(dir + File.separator + m3U8Ts.getFile());
                    if (!file.exists()) {//下载过的就不管了
                        FileOutputStream fos = null;
                        InputStream inputStream = null;
                        try {
                            URL url = new URL(basePath + m3U8Ts.getFile());
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(10 * 1000);
                            conn.setReadTimeout(10 * 60 * 1000);
                            if (conn.getResponseCode() == 200) {
                                inputStream = conn.getInputStream();
                                fos = new FileOutputStream(file);//会自动创建文件
                                int len = 0;
                                byte[] buf = new byte[8 * 1024 * 1024];
                                while ((len = inputStream.read(buf)) != -1) {
                                    fos.write(buf, 0, len);//写入流中
                                }
                            } else {
                                handlerError(new Throwable(String.valueOf(conn.getResponseCode())));
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            handlerError(e);
                        } catch (IOException e) {
                            e.printStackTrace();
                            handlerError(e);
                        } finally {//关流
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        curTs++;
                        if (curTs == 3) {
                            itemFileSize = file.length();
                        }
                        Log.e("hdltag", "run(M3U8DownloadManger.java:181):下载进度： " + totalTs + "-" + curTs);
                        handlerNext(itemFileSize, totalTs, curTs);
                    }
                }
            });
        }
    }

    /**
     * 通知异常
     *
     * @param e
     */
    private void handlerError(Throwable e) {
        Message msg = mHandler.obtainMessage();
        msg.obj = e;
        msg.what = WHAT_ON_ERROR;
        mHandler.sendMessage(msg);
    }

    /**
     * 下载完成一次
     *
     * @param itemFileSize
     * @param totalTs
     * @param curTs
     */
    private void handlerNext(long itemFileSize, int totalTs, int curTs) {
        Message msg = mHandler.obtainMessage();
        msg.obj = itemFileSize;
        msg.arg1 = totalTs;
        msg.arg2 = curTs;
        msg.what = WHAT_ON_NEXT;
        mHandler.sendMessage(msg);
    }
}
