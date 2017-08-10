package com.hdl.m3u8.bean;

/**
 * 下载监听
 * Created by HDL on 2017/8/10.
 */

public interface OnDownloadListener extends BaseListener {
    /**
     * 下载
     *
     * @param itemFileSize 单个文件的大小
     * @param totalTs      ts总数
     * @param curTs        当前下载完成的ts个数
     */
    void onDownloading(long itemFileSize, int totalTs, int curTs);

    /**
     * 下载成功
     */
    void onSuccess();
}
