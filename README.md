## M3U8Manger (M3U8管理器)

- M3U8格式视频下载


导入：

```java
compile 'com.jwkj:M3U8Manger:v1.0.3'
```

使用：

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
## 版本记录
v1.0.3
- 【修复】url失效时抛出异常
- 【新增】M3U8开始、结束时间