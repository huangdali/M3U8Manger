package com.hdl.m3u8.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * M3U8实体类
 * Created by HDL on 2017/7/24.
 */

public class M3U8 {
    private String basepath;
    private List<M3U8Ts> tsList = new ArrayList<M3U8Ts>();

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    public List<M3U8Ts> getTsList() {
        return tsList;
    }

    public void setTsList(List<M3U8Ts> tsList) {
        this.tsList = tsList;
    }

    public void addTs(M3U8Ts ts) {
        this.tsList.add(ts);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("basepath: " + basepath);
        for (M3U8Ts ts : tsList) {
            sb.append("\nts: " + ts);
        }

        return sb.toString();
    }
}
