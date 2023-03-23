OkHttp 组件

## 支持功能清单

- 基础的请求（GET,POST）
- 上传下载文件
- 请求超时控制
- 请求重试控制
- 异步执行回调（实现中）

## 快速开始

- Get请求

```
  
  // 发起请求
  Response execute = OkHttpUtils
                .get(mockBasePath() + "/get")
                .build()
                .execute();

  // 获取结果
  ResponseBody body = execute.body();
  
```

- 请求失败重试

```

 Response execute = OkHttpUtils
                .get(mockBasePath() + "/get")
                .build()
                .enableRetry(true)          // 开启请求失败重试
                .retryAttemptNumber(3)      // 重试次数
                .retrySleepTime(100L)       // 重试间隔
                .execute();

 ResponseBody body = execute.body();

```

- 上传文件

```

File fileDemo1 = FileUtil.file("fileDemo1.text");

Response execute = OkHttpUtils
        .post()
        .url(mockBasePath() + "/upload")
        .addParams("file", fileDemo1)
        .build()
        .execute();

```

- 下载文件

```

// 请求参数
String filename = "downloadTest.text";

// 获取到下载文件夹
final File download = FileUtil.file("download");

// 下载文件
final boolean downloadBool = OkHttpUtils.downloadFile(mockBasePath() + "/download?filename=" + filename, download);

```
