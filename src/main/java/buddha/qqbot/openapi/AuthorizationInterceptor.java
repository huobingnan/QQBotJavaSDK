package buddha.qqbot.openapi;

import buddha.qqbot.QQBot;
import buddha.qqbot.QQBotProperties;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AuthorizationInterceptor implements Interceptor {

    private final QQBot instance;

    public AuthorizationInterceptor(final QQBot bot) {
        instance = bot;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        final Request reqWrapper = chain.request().newBuilder()
                .build();
        return chain.proceed(reqWrapper);
    }
}
