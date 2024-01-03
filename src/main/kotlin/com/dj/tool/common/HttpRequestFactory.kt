package com.dj.tool.common;

import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HttpRequestFactory {

    private static SSLContext sslcontext;
    private static NoopHostnameVerifier verifier;
    private static SSLConnectionSocketFactory sslConnectionSocketFactory;

    static {
        try {
            sslcontext = new SSLContextBuilder().loadTrustMaterial(null, (china, authType) -> true).build();
            verifier = NoopHostnameVerifier.INSTANCE;
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, verifier);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendDataToConf(String apiURL, String userName, String password, String title, String spaceKey, String parentId, String htmlBody, Consumer<String> successNotificationConsumer, Consumer<String> failNotificationConsumer) throws NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        Map<String, Object> map = buildParam(title, spaceKey, parentId, htmlBody);
        send(apiURL, map, Constants.CHARSET_UTF_8, userName, password, successNotificationConsumer, failNotificationConsumer);
    }

    private static HashMap<String, Object> buildParam(String title, String spaceKey, String parentId, String htmlBody) {
        HashMap<String, Object> param = new HashMap<>();

        HashMap<String, Object> space = new HashMap<>();
        space.put("key", spaceKey);

        HashMap<String, Object> ancestors = new HashMap<>();
        ancestors.put("id", parentId);

        HashMap<String, Object> storage = new HashMap<>();
        storage.put("representation", "storage");
        storage.put("value", htmlBody);

        HashMap<String, Object> body = new HashMap<>();
        body.put("storage", storage);

        param.put("title", title);
        param.put("type", "page");
        param.put("status", "current");
        param.put("space", space);
        param.put("ancestors", Lists.newArrayList(ancestors));
        param.put("body", body);
        return param;
    }

    public static String send(String url, Map<String, Object> map, String encoding, String userName, String password, Consumer<String> successNotificationConsumer, Consumer<String> failNotificationConsumer) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, KeyStoreException {
        String body = "";

        //绕过证书验证，处理https请求
//        SSLContext sslcontext = new SSLContextBuilder().loadTrustMaterial(null, (china, authType) -> true).build();
//        NoopHostnameVerifier verifier = NoopHostnameVerifier.INSTANCE;
//        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, verifier);

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(2);
        connManager.setDefaultMaxPerRoute(2);

        RequestConfig requestConfig = RequestConfig.custom()
                .setAuthenticationEnabled(true)
                .setConnectionRequestTimeout(Constants.CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(Constants.SOCKET_TIMEOUT)
                .setConnectTimeout(Constants.CONNECT_TIMEOUT)
                .setRedirectsEnabled(false)
                .setCookieSpec(CookieSpecs.STANDARD)
                .build();

        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();

        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(JsonUtils.toJson(map), Constants.MIME_TYPE_APPLICATION_JSON, encoding));

        String authorization = Constants.BASIC_AUTH_PREFIX +
                Base64.getUrlEncoder().encodeToString((userName + ":" + password).getBytes());
        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader(Constants.HEADER_NAME_CONTENT_TYPE, Constants.MIME_TYPE_APPLICATION_JSON);
        httpPost.setHeader(Constants.HEADER_NAME_AUTHORIZATION, authorization);

        try {
            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpPost);

            //获取结果实体
            HttpEntity entity = response.getEntity();

            boolean isSuccess = response.getStatusLine().getStatusCode() == 200 ||
                    response.getStatusLine().getStatusCode() == 201 ||
                    response.getStatusLine().getStatusCode() == 202;
            if (!isSuccess) {
                failNotificationConsumer.accept("sync to confluence fail!");
            }
            if (isSuccess) {
                successNotificationConsumer.accept("sync to confluence successful!");
            }
            EntityUtils.consume(entity);
            //释放链接
            response.close();
        } catch (Exception e) {
            failNotificationConsumer.accept("sync to confluence exception: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("execute network request error~");
        } finally {
            client.close();
            connManager.close();
        }
        return body;
    }

}
