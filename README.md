# QQBotJavaSDK
根据QQBot官方问题开发的Java版本的SDK，可以使用SDK提供的API快速创建一个QQBot应用
## 技术选型
1. Java版本： >= 1.8
2. 构建工具： Gradle
3. HTTP & WebSocket： OkHttp
4. JSON解析： FastJSON2

## 机器人配置文件
在配置机器人上选择使用`properties`文件进行，配置文件名称为： `qqbot.properties`。在运行时QQBot框架会按照如下的优先级来寻找配置文件：
1. 类路径，classpath
2. 运行时工作目录，参考`System.getProperty("user.dir")`

其中后者的优先级更高会**覆盖**前者的配置，二者也可以**互补配置**。
除了配置文件，也可以通过传递JVM系统参数的方式进行配置： `-Dqqbot.appId`，这和在代码中通过`System.setProperty`的效果一致，我们称
这种方式为**JVM系统参数配置**。除了系统参数配置，对于比较敏感的app-id、app-secret这样的配置项，你也可以通过设置环境变量的方式进行。
```shell
export QQ_BOT_APP_ID=xxxx
export QQ_BOT_SECRET=xxx
```
注意： 读取配置时SDK只会尝试从环境变量中读取QQ_BOT_APP_ID和QQ_BOT_SECRET两个配置项。并且这两个从环境变量中读取的配置项拥有最高的
优先级，它会覆盖之前配置的app-id和app-secret。

综上所述，配置项生效的优先级为（数字越大，优先级越高）：
1. 类路径下的`qqbot.properties`
2. 运行时工作目录下的`qqbot.properties`
3. JVM系统参数配置
4. 环境变量配置（仅配置app-id和app-secret）

## 机器人配置项
| 配置项                     | 类型   | 说明               | 缺省   |
|:------------------------|:-----|:-----------------|:-----|
| qqbot.appId             | 字符串  | QQ机器人的app-id     | 必填   |
| qqbot.appSecret         | 字符串  | QQ机器人的app-secret | 必填   |
| qqbot.message.pool.size | 数字   | 消息派发线程的数目        | 10   |
| qqbot.message.intents   | 数字   | 监听事件Intents      | 1    |
| qqbot.message.shared    | 数字数组 | 分片类型             | 0, 1 |

未完待续......