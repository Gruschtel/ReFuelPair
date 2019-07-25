package de.gruschtelapps.fh_maa_refuelpair.utils.helper;

import java.io.IOException;

import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create by Eric Werner
 */
public class HttpHelper {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String TOKEN = "token";

    // ===========================================================
    // Fields
    // ===========================================================
    private OkHttpClient client;

    // ===========================================================
    // Constructors
    // ===========================================================
    public HttpHelper() {
        client = new OkHttpClient();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Standard Http Get Anfrage
     * @param url
     * @param token
     * @return
     */
    public String httpGet(String url, String token) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter(TOKEN, token);
        String url2 = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url2)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ConstError.ERROR_STRING;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
