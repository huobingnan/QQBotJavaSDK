package buddha.qqbot.openapi;

import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.function.Function;

/**
 * QQBot官方通用的一些请求枚举
 */
public enum CommonRequest implements IOkHttpRequestType {
    ACCESS_TOKEN((args) -> {
        if (args == null || args.length < 2) throw new IllegalArgumentException("构建ACCESS_TOKEN请求必须传递[appId, clientSecret]");
        final JSONObject payload = new JSONObject();
        payload.put("appId", args[0]);
        payload.put("clientSecret", args[1]);
        final RequestBody body = RequestBody.create(payload.toJSONString(), MediaType.get("application/json"));
        return new Request.Builder().url("https://bots.qq.com/app/getAppAccessToken")
                .post(body)
                .build();
    }),
    WSS_GATEWAY((config) -> {
        if (config == null || config.length < 3 || !(config[2] instanceof Boolean)) {
            throw new IllegalArgumentException(
                    "构建WSS_GATEWAY请求必须传递[accessToken(string), appId(string), isDev(boolean)]参数"
            );
        }
        final Boolean isDev = (Boolean) config[2];
        final String url = isDev == Boolean.TRUE ? "" : "https://sandbox.api.sgroup.qq.com/gateway";
        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }),
    WSS_GATEWAY_CONNECT(config -> {
        if (config == null || config.length < 1) {
            throw new IllegalArgumentException(
                    "构建WSS_GATEWAY_CONNECT必须传递[wssGatewayUri(string)]"
            );
        }
        return new Request.Builder()
                .url(config[0].toString())
                .build();
    })
    ;

    private final Function<Object[], Request> builder;

    CommonRequest(Function<Object[], Request> builder) {
        this.builder = builder;
    }

    public Request build(Object... args) {
        return builder.apply(args);
    }

    public Response send(final OkHttpClient client, Object... args) throws IOException {
        return client.newCall(builder.apply(args)).execute();
    }


}
