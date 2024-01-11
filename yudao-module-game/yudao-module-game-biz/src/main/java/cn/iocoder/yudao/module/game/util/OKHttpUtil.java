package cn.iocoder.yudao.module.game.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.iocoder.yudao.module.game.common.consts.Constant;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by shuttle on 18/7/17.
 */
public class OKHttpUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(OKHttpUtil.class);

    public final static int CONNECT_TIMEOUT = 60 * 10;
    public final static int READ_TIMEOUT = 60 * 10;
    public final static int WRITE_TIMEOUT = 60 * 10;

    public final static int SHORT_CONNECT_TIMEOUT = 3;
    public final static int SHORT_READ_TIMEOUT = 3;
    public final static int SHORT_WRITE_TIMEOUT = 3;
    public static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    public static String sendGetTimeOutShort(String url, Map<String, String> header) {
        OkHttpClient client = getConnClientShort();
        FormBody.Builder builder = new FormBody.Builder();

        String headerName = "user-agent";
        String headeValue = "nodata";

        FormBody body = builder.build();
        Request request = null;
        Request.Builder builder2 = new Request.Builder().url(url).get();//build();
        if (null != header) {
            Set<String> keySet = header.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = header.get(key);
                builder2.addHeader(key, value);
            }
        }
        request = builder2.build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                LOGGER.info("请求响应异常,{}", response.toString());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Response sendGetSSL(String url, Map<String, String> header) {
        OkHttpClient client = getConnSSlClient();
        FormBody.Builder builder = new FormBody.Builder();

        String headerName = "user-agent";
        String headeValue = "nodata";

        FormBody body = builder.build();
        Request request = null;
        Request.Builder builder2 = new Request.Builder().url(url).get();//build();
        if (null != header) {
            Set<String> keySet = header.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = header.get(key);
                builder2.addHeader(key, value);
            }
        }
        request = builder2.build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() || response.code() == 302) {
                return response;
            } else {
                LOGGER.info("请求响应异常,{}", response.toString());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 不使用okhttp
    public static String sendGetObject(String url, Map<String, Object> param, Map<String, String> header) {
        Header[] headers = new Header[header.size()];
        int i = 0;
        for (String key : header.keySet()) {
            Header h = new BasicHeader(key, header.get(key));
            headers[i] = h;
            i++;
        }

        HttpEntityEnclosingRequestBase httpEntity = new HttpEntityEnclosingRequestBase() {
            @Override
            public String getMethod() {
                return "GET";
            }
        };
        httpEntity.setHeaders(headers);

        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;

        try {
            client = HttpClientBuilder.create().build();
            httpEntity.setURI(URI.create(url));
            httpEntity.setEntity(new StringEntity(JSONObject.toJSONString(param), ContentType.APPLICATION_JSON));

            response = client.execute(httpEntity);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                bufferedReader.close();
                return result.toString();
            }
        } catch (IOException e) {
            LOGGER.info("请求响应异常,{}", response.toString());
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String sendGet(String url, Map<String, String> param, Map<String, String> header) {
        OkHttpClient client = getConnClient();
        FormBody.Builder builder = new FormBody.Builder();

        String paramName = "";
        String paramValue = "";

        FormBody body = builder.build();
        Request request = null;
        if (null != param) {
            url += "?";
            for (Map.Entry<String, String> entry : param.entrySet()) {
                paramName = entry.getKey();
                paramValue = entry.getValue();
                url += paramName + "=" + paramValue + "&";
            }
        }
        Request.Builder builder2 = new Request.Builder().url(url).get();//build();
        if (null != header) {
            Set<String> keySet = header.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = header.get(key);
                builder2.addHeader(key, value);
            }
        }
        request = builder2.build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                LOGGER.info("请求响应异常,{}", response.toString());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendGet(String url, Map<String, String> param) {
        String rentId = MDC.get(Constant.HEADER_RENT_ID);
        Map<String, String> header = new HashMap<>();
        if (rentId != null && !rentId.trim().equals("")) {
            header.put(Constant.HEADER_RENT_ID, rentId);
        }
        return sendGet(url, param, header);
    }

    public static String sendGet(String url) {
        return sendGet(url, null);
    }


    public static String sendPost(String url, Map<String, String> paramMap) {
        return sendPost(url, paramMap, null);
    }

    public static String sendPostJsonObjectNoHeader(String url, Map<String, Object> paramMap) {
        OkHttpClient client = getConnClient();
        RequestBody body = RequestBody.create(JSON_TYPE, JSONObject.toJSONString(paramMap));
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                LOGGER.info("请求响应异常,{}", response.toString());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sendPostJsonObject(String url, Map<String, Object> paramMap, Map<String, String> header) {
        OkHttpClient client = getConnClient();
        RequestBody body = RequestBody.create(JSON_TYPE, JSONObject.toJSONString(paramMap));
        Headers.Builder headerBuilder = new Headers.Builder();
        String headerName = "user-agent";
        String headerValue = "nodata";
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                headerName = entry.getKey();
                headerValue = entry.getValue();
                headerBuilder.add(headerName, headerValue);
            }
        }
        Request request = new Request.Builder().url(url).headers(headerBuilder.build()).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                LOGGER.info("请求响应异常,{}", response.toString());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sendPostJson(String url, Map<String, String> paramMap, Map<String, String> header) {
        OkHttpClient client = getConnClient();
        RequestBody body = RequestBody.create(JSON_TYPE, JSONObject.toJSONString(paramMap));
        Headers.Builder headerBuilder = new Headers.Builder();
        String headerName = "user-agent";
        String headeValue = "nodata";
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                headerName = entry.getKey();
                headeValue = entry.getValue();
                headerBuilder.add(headerName, headeValue);
            }
        }
        Request request = new Request.Builder().url(url).headers(headerBuilder.build()).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                LOGGER.info("请求响应异常,{}", response.toString());
                return "服务异常";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "请求超时";
        }
    }

    public static String sendPost(String url, Map<String, String> paramMap, Map<String, String> header) {
        OkHttpClient client = getConnClient();
        FormBody.Builder builder = new FormBody.Builder();
        Headers.Builder headerBuilder = new Headers.Builder();
        if (paramMap != null) {
            String paramName = null;
            String paramValue = null;
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                paramName = entry.getKey();
                paramValue = entry.getValue();
                builder.add(paramName, paramValue);
            }
        }
        String headerName = "user-agent";
        String headeValue = "nodata";
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                headerName = entry.getKey();
                headeValue = entry.getValue();
                headerBuilder.add(headerName, headeValue);
            }
        }
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).headers(headerBuilder.build()).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                LOGGER.info("请求响应异常,{}", response.toString());
                return "服务异常";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return
     * @Description: 发送jso参数
     * 没有写请求header
     * @author: yqx
     * @date: 2016年12月24日 下午2:30:16
     */
    public static String sendPostJsonWithMap(String url, String jsonStr) {
        OkHttpClient client = getConnClient();
        RequestBody body = RequestBody.create(JSON_TYPE, jsonStr);
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                LOGGER.info("请求响应异常,{}", response.toString());
                return "服务异常";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "请求超时";
        }
    }

    /**
     * @return
     * @Description: 发送jso参数
     * 没有写请求header
     * @author: yqx
     * @date: 2016年12月24日 下午2:30:16
     */
    public static String sendPostJson(String url, Map<String, String> paramMap) {
        OkHttpClient client = getConnClient();
        String jsonString = JSONObject.toJSONString(paramMap);
        RequestBody body = RequestBody.create(JSON_TYPE, jsonString);
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                LOGGER.info("请求响应异常,{}", response.toString());
                return "服务异常";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "请求超时";
        }
    }

    /**
     * @return
     * @Description: 发送jso参数
     * 没有写请求header
     * @author: yqx
     * @date: 2016年12月24日 下午2:30:16
     */
    public static String sendPostJson(String url, String jsonString) {
        OkHttpClient client = getConnClient();
        RequestBody body = RequestBody.create(JSON_TYPE, jsonString);
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                LOGGER.info("请求响应异常,{}", response.toString());
                return "服务异常";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "请求超时";
        }
    }

    /**
     * @param url 请求url
     * @param e   传入参数
     * @param t   响应对象
     * @return
     * @Description: 传入的对象，返回对象
     * @author: yqx
     * @date: 2016年12月24日 下午3:58:56
     */
    public static <T, E> T sendPostJson(String url, E e, Class<T> t) {
        OkHttpClient client = getConnClient();
        T object = null;
        String paramJson = JSON.toJSONString(e);
        RequestBody body = RequestBody.create(JSON_TYPE, paramJson);
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String string = response.body().string();
                JSONObject parseObject = JSON.parseObject(string);
                object = JSON.toJavaObject(parseObject, t);
                return object;
            } else {
                LOGGER.info("请求响应异常,{}", response.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return object;
    }

    /**
     * @param url 请求url
     * @param t   传入参数
     * @return
     * @Description: postJson 参数是对象，返回string
     * @author: yqx
     * @date: 2016年12月26日 上午10:38:50
     */
    public static <T> String sendPostJson(String url, T t) {
        OkHttpClient client = getConnClient();
        String paramJson = JSON.toJSONString(t);
        RequestBody body = RequestBody.create(JSON_TYPE, paramJson);
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String string = response.body().string();
                return string;
            } else {
                LOGGER.info("请求响应异常,{}", response.toString());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", 500);
                jsonObject.put("message", "服务请求异常");
                return jsonObject.toString();
            }
        } catch (Exception ex) {
            addExpectionLog("sendPostJson请求异常", ex);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 500);
            jsonObject.put("message", "服务请求超时");
            return jsonObject.toString();
        }
    }

    private static void addExpectionLog(String exceptionDesc, Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        LOGGER.error(exceptionDesc + "--->" + sw.toString());
    }

    public static OkHttpClient getConnClient() {
        OkHttpClient client = getClient();
        return client;
    }

    public static OkHttpClient getConnClientShort() {
        OkHttpClient client = getClientShort();
        return client;
    }

    public static OkHttpClient getConnSSlClient() {
        OkHttpClient client = getSSLClient();
        return client;
    }

    private final static HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    private static final class InnerOKHttp {

        static Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 7590));
        private static OkHttpClient client = new OkHttpClient.Builder().readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .build();
        private static OkHttpClient windowsClient = new OkHttpClient.Builder().readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .proxy(proxy)
            .build();

        private static OkHttpClient clientShort = new OkHttpClient.Builder().readTimeout(SHORT_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(SHORT_WRITE_TIMEOUT, TimeUnit.SECONDS).connectTimeout(SHORT_CONNECT_TIMEOUT, TimeUnit.SECONDS).build();
        private static OkHttpClient sslClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
//            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
            .followRedirects(false)
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier()).cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                    cookieStore.put(httpUrl.host(), list);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                    List<Cookie> cookies = cookieStore.get(httpUrl.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })
            .build();
        private static OkHttpClient.Builder sslClientBuilder = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
//            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
            .followRedirects(false)
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier()).cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                    cookieStore.put(httpUrl.host(), list);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                    List<Cookie> cookies = cookieStore.get(httpUrl.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            });
    }

    public static OkHttpClient getClient() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            return InnerOKHttp.windowsClient;
        }
        return InnerOKHttp.client;
    }

    public static OkHttpClient getClientShort() {
        return InnerOKHttp.clientShort;
    }

    public static OkHttpClient getSSLClient() {
        return InnerOKHttp.sslClient;
    }

    public static OkHttpClient.Builder getSSLClientBuilder() {
        return InnerOKHttp.sslClientBuilder;
    }
}
