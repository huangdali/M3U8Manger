package com.hdl.m3u8.bean;

/**
 * @deprecated v2版本过时了，请用{@link OnM3U8InfoListener}
 * 加载监听器
 * Created by HDL on 2017/7/24.
 */

public interface DownLoadListener {
    /**
     * 开始的时候回调
     */
    void onStart();

    /**
     * 错误的时候回调
     *
     * @param errorMsg
     */
    void onError(Throwable errorMsg);

    /**
     * 下载完成的时候回调
     */
    void onCompleted();
}
