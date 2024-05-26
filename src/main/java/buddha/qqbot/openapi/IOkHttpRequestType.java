package buddha.qqbot.openapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public interface IOkHttpRequestType {

    Request build(final Object... args);

    Response send(final OkHttpClient client, final Object... args) throws IOException;

}
