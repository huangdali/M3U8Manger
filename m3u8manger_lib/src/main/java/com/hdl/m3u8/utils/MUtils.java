package com.hdl.m3u8.utils;

import com.hdl.m3u8.bean.M3U8;
import com.hdl.m3u8.bean.M3U8Ts;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * M3u8工具类
 * Created by HDL on 2017/7/24.
 */

public class MUtils {
    /**
     * 将Url转换为M3U8对象
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static M3U8 parseIndex(String url) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));

        String basepath = url.substring(0, url.lastIndexOf("/") + 1);

        M3U8 ret = new M3U8();
        ret.setBasepath(basepath);

        String line;
        float seconds = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                if (line.startsWith("#EXTINF:")) {
                    line = line.substring(8);
                    if (line.endsWith(",")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    seconds = Float.parseFloat(line);
                }
                continue;
            }
            if (line.endsWith("m3u8")) {
                return parseIndex(basepath + line);
            }
            ret.addTs(new M3U8Ts(line, seconds));
            seconds = 0;
        }
        reader.close();

        return ret;
    }

    /**
     * 将M3U8对象的所有ts切片合并为1个
     *
     * @param m3u8
     * @param tofile
     * @throws IOException
     */
    public static void merge(M3U8 m3u8, String tofile) throws IOException {
        File file = new File(tofile);
        FileOutputStream fos = new FileOutputStream(file);
        for (M3U8Ts ts : m3u8.getTsList()) {
            IOUtils.copyLarge(new FileInputStream(new File(file.getParentFile(), ts.getFile())), fos);
        }
        fos.close();
    }

    /**
     * 移动文件
     *
     * @param sFile
     * @param tFile
     */
    public static void moveFile(String sFile, String tFile) {
        try {
            FileUtils.copyFile(new File(sFile), new File(tFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空文件夹
     */
    public static void clearDir(File dir) {
        if (dir.exists()) {// 判断文件是否存在
            if (dir.isFile()) {// 判断是否是文件
                dir.delete();// 删除文件
            } else if (dir.isDirectory()) {// 否则如果它是一个目录
                File[] files = dir.listFiles();// 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) {// 遍历目录下所有的文件
                    clearDir(files[i]);// 把每个文件用这个方法进行迭代
                }
                dir.delete();// 删除文件夹
            }
        }
    }
}
