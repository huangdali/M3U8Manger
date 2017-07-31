package com.hdl.m3u8.bean;

/**
 * 监听器
 * Created by HDL on 2017/7/25.
 */

public abstract class M3U8Listener implements DownLoadListener {
    public void onM3U8Info(M3U8 m3U8) {
    }

    public void onDownloadingProgress(int total, int progress) {
    }
}
