Netty组件

#### 使用说明

- 推送消息
```
  @Resource
  private DefaultMessagePusher defaultMessagePusher;
```

- 模拟登录
```
  @Resource
  private AccessTokenService accessTokenService;
```

- 核心类
    - AccessTokenService: 管理用户的登录信息和权限验证
    - APNsService: apn 连接管理
    - SessionService：统一会话管理
    - DefaultHandshakePredicate：WS链接握手鉴权验证
 

- 配置信息
```
cim:
  
  apns:
    app-id: com.xxx.xxx.ios
    debug: false
    p12:
      file: /apns/app.p12
      password: 123

  app:
    enable: true
    max-pong-timeout: 3
    port: 23456
    read-idle: 60s
    write-idle: 45s

  websocket:
    enable: true
    max-pong-timeout: 3
    path: /
    port: 34567
    protocol: protobuf
    read-idle: 60s
    write-idle: 45s


server:
  port: 8080

spring:
  profiles:
    active: dev
  redis:
    database: 7
    host: localhost
    password: 123qweASD
    lettuce:
      pool:
        max-active: 10
        max-idle: 5
        max-wait: 10s
        min-idle: 1
    port: 6379
    timeout: 10s

```

