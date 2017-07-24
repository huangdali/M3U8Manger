## M3U8Manger (M3U8管理器)

- M3U8格式视频下载


导入：

```java
compile 'com.jwkj:M3U8Manger:v1.0.2'
```

使用：

```java
 M3U8Manger.getInstance()
                .setUrl(url)
                .setSaveFilePath("/sdcard/11/"+System.currentTimeMillis()+".ts")//文件类型目前只支持ts
                .download(new DownLoadListener() {
                    @Override
                    public void onStart() {//开始下载
                    }

                    @Override
                    public void onError(Throwable errorMsg) {//下载出错
                    }

                    @Override
                    public void onCompleted() {//下载完成
                    }
                });
```
