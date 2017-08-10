package com.hdl.m3u8.bean;

/**
 * 获取M3U8信息
 * Created by HDL on 2017/8/10.
 */

public interface OnM3U8InfoListener extends BaseListener {

    /**
     * 获取成功的时候回调
     */
    void onSuccess(M3U8 m3U8);
}
