package com.hdl.m3u8.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * M3U8实体类
 * Created by HDL on 2017/7/24.
 */

public class M3U8 {
    private String basepath;
    private List<M3U8Ts> tsList = new ArrayList<M3U8Ts>();
    private long startTime;//开始时间
    private long endTime;//结束时间

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

    /**
     * 获取开始时间
     *
     * @return
     */
    public long getStartTime() {
        Collections.sort(tsList);
        startTime = tsList.get(0).getLongDate();
        return startTime;
    }

    /**
     * 获取结束时间(加上了最后一段时间的持续时间)
     *
     * @return
     */
    public long getEndTime() {
        M3U8Ts m3U8Ts = tsList.get(tsList.size() - 1);
        endTime = m3U8Ts.getLongDate() + (long) (m3U8Ts.getSeconds() * 1000);
        return endTime;
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
