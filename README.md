## M3U8Manger (M3U8管理器)

- M3U8格式视频下载
- 获取M3U8格式信息
- 中途停止任务

适用性：适用一下格式的m3u8格式（ts切片的名字就是当前时间eg:1500480000074.ts）
```
#EXTM3U
#EXT-X-VERSION:3
#EXT-X-MEDIA-SEQUENCE:0
#EXT-X-TARGETDURATION:10
#EXTINF:9.960,
1500480000074.ts
#EXTINF:10.190,
1500480009999.ts
#EXTINF:9.960,
1500480020246.ts
#EXTINF:9.960,
1500480030189.ts
#EXTINF:10.030,
...
```


### 导入：

```java
compile 'com.jwkj:M3U8Manger:v1.0.6'
```

### 获取M3U8信息：

```java
 M3U8Manger.getInstance()
                .setUrl(url)
                .getM3U8(new M3U8Listener() {
                    @Override
                    public void onStart() {
                        Log.e("hdltag", "onStart(MainActivity.java:75):开始了" );
                    }

                    @Override
                    public void onError(Throwable errorMsg) {
                        Log.e("hdltag", "onStart(MainActivity.java:75):出错了"+errorMsg );
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("hdltag", "onStart(MainActivity.java:75):完成了" );
                    }

                    @Override
                    public void onM3U8Info(M3U8 m3U8) {
                        Log.e("hdltag", "onStart(MainActivity.java:75):拿到结果了"+m3U8 );
                        Log.e("hdltag", "onM3U8Info(MainActivity.java:91):" + m3U8.getTsList());
                    }
                });
```


### 下载M3U8格式的视频文件：

```java
M3U8Manger.getInstance()
                .setUrl(url)
                .setSaveFilePath("/sdcard/11/"+System.currentTimeMillis()+".ts")
                .download(new M3U8Listener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(MainActivity.this, "开始下载了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable errorMsg) {
                        Log.e("hdltag", "onError(MainActivity.java:28):下载出错了" + errorMsg);
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("hdltag", "onCompleted(MainActivity.java:33):下载完成了");
                    }
                });
```

### 停止任务：

```java
    M3U8Manger.getInstance().stop();
```

## 版本记录

v1.0.6
- 【修复】指定下载的区间时间不准问题

v1.0.5
- 【修复】下载成功走的是onStart
- 【新增】指定下载的区间

v1.0.4
- 【新增】获取M3U8基本信息方法
- 【新增】停止任务方法

v1.0.3
- 【修复】url失效时抛出异常
- 【新增】M3U8开始、结束时间