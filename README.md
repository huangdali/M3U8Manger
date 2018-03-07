## M3U8Manger (M3U8管理器)

如果帮助到你，麻烦来个star，更欢迎你的fork

- M3U8格式视频下载
- 获取M3U8格式信息
- 中途停止任务
- 获取下载速度
- 支持多任务下载
- 支持url重定向处理过的m3u8
- 支持直播缓存
> **温馨提示：** 项目会一直维护， 请尽量通过issue渠道提bug和改进建议（绑定过邮箱，会第一时间回复）

## 体验app
### 扫描二维码：

![](https://github.com/huangdali/M3U8Manger/blob/master/image6.png)

### 或打开下面的地址
https://fir.im/2g5c


适用性：适用以下格式的m3u8格式(如果名字不是时间毫秒值的话，调用获取m3u8信息的方法将得不到开始时间，但不影响下载)，当然有鉴权信息也是允许的

```java
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
compile 'com.jwkj:M3U8Manger:v2.3.0'
```

### 获取M3U8信息：

```java
   M3U8InfoManger.getInstance().getM3U8Info(url, new OnM3U8InfoListener() {
            @Override
            public void onSuccess(M3U8 m3U8) {
                ELog.e("获取成功了" + m3U8);
            }

            @Override
            public void onStart() {
                ELog.e("开始获取信息");
            }

            @Override
            public void onError(Throwable errorMsg) {
                ELog.e("出错了" + errorMsg);
            }
        });
```

## 点播下载
### 下载M3U8格式的视频文件：

```java
 M3U8DownloadTask task1 = new M3U8DownloadTask("1001");
//下载按钮监听事件
 public void onDownload(View view) {
        task1.download(url, new OnDownloadListener() {
            @Override
            public void onDownloading(final long itemFileSize, final int totalTs, final int curTs) {
                ELog.e(task1.getTaskId() + "下载中.....itemFileSize=" + itemFileSize + "\ttotalTs=" + totalTs + "\tcurTs=" + curTs);
            }

            /**
             * 下载成功
             */
            @Override
            public void onSuccess() {
                ELog.e(task1.getTaskId() + "下载完成了");
            }

            /**
             * 当前的进度回调
             *
             * @param curLenght
             */
            @Override
            public void onProgress(final long curLenght) {
                if (curLenght - lastLength > 0) {
                    final String speed = NetSpeedUtils.getInstance().displayFileSize(curLenght - lastLength) + "/s";
                    ELog.e(task1.getTaskId() + "speed = " + speed);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ELog.e("更新了");
                            tvSpeed1.setText(speed);
                            ELog.e(tvSpeed1.getText().toString());
                        }
                    });
                    lastLength = curLenght;

                }
            }

            @Override
            public void onStart() {
                ELog.e(task1.getTaskId() + "开始下载了");
            }

            @Override
            public void onError(Throwable errorMsg) {
                ELog.e(task1.getTaskId() + "出错了" + errorMsg);
            }
        });
    }

```

### 停止任务：

```java
      task1.stop();
```


## 直播缓存

### 开启缓存

```java
 String toFile="/sdcard/" + System.currentTimeMillis() + ".ts";
        tvSaveFilePathTip.setText("缓存目录在：/sdcard/11m3u8/\n最终导出的缓存文件在："+toFile);
        M3U8LiveManger.getInstance()
                .setTempDir("/sdcard/11m3u8/")
                .setSaveFile(toFile)//（设置导出缓存文件）必须以.ts结尾
                .caching(url, new OnDownloadListener() {
                    @Override
                    public void onDownloading(long itemFileSize, int totalTs, int curTs) {
                      //此回调只有curTs有意义，表示开始缓存第几个ts
                    }

                    @Override
                    public void onSuccess() {
                        //此回调没有什么意义
                    }

                    @Override
                    public void onProgress(long curLength) {
                        if (curLength - lastLength > 0) {
                        //计算缓存速度
                            final String speed = NetSpeedUtils.getInstance().displayFileSize(curLength - lastLength) + "/s";
                            lastLength = curLength;
                        }
                    }

                    @Override
                    public void onStart() {
                        //开始缓存
                    }

                    @Override
                    public void onError(Throwable errorMsg) {
                        //缓存出错了
                    }
                });
```

### 获取开始缓存到当前时间的ts文件
```java
 String filePath = M3U8LiveManger.getInstance().getCurrentTs();
```

> 温馨提示：此方法会自动合并当次任务所下载的所有ts文件，如果你不需要此方法，也可以使用com.hdl.m3u8.utils.MUtils.merge(java.util.List<java.io.File>, java.lang.String)方法来合并，当然自己写合并方法也是可以的

### 停止缓存
```java
 M3U8LiveManger.getInstance().stop();
```


## 版本记录

### v2.x


v2.3.0([2018.03.07]())
- 【新增】支持缓存直播类型的m3u8

v2.2.3([2018.03.06]())
- 【优化】支持更多格式的ts命名

v2.1.9([2017.11.21]())
- 【优化】支持url重定向处理过的m3u8

v2.1.8([2017.11.21]())
- 【新增】支持m3u8中的ts名字为非时间毫秒值的格式（此时ts的开始时间为0）

v2.1.7([2017.09.12]())
- 【新增】设置下载线程数方法（默认3个，建议不要设置超过5个）

v2.1.6([2017.08.15]())
- 【新增】设置连接超时时间、读取超时时间

v2.1.4、v2.1.5([2017.08.14]())
- 【优化】延迟删除临时文件（因为存取速度比较低的设备可能存在未移动完成而被删除情况）

v2.1.3([2017.08.14]())
- 【优化】网络不稳定时的数据抖动问题

v2.1.2([2017.08.14]())
- 【新增】设置是否清除临时目录的方法

v2.1.1([2017.08.14]())
- 【修复】文件生成与设置的文件名字不一致问题

v2.0.8([2017.08.14]())
- 【修复】设置文件保存路径时，会多出文件夹

v2.0.7([2017.08.14]())
- 【修复】设置文件保存路径方法被隐藏

v2.0.6([2017.08.14]())
- 【新增】断点续传
- 【新增】支持多任务
- 【优化】下载速度不准确问题

### V1.x

v1.0.8([2017.08.08]())
- 【新增】onLoadFileSizeForItem方法（获取单个ts文件的平均大小）
    - 可根据这个平均值x总的切片个数（total）=总文件的大小

v1.0.7([2017.07.31]())
- 【修复】获取M3U8信息不走onStart的bug

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
