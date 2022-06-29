package com.dj.tool.common;

import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.collect.Lists;
import org.apache.http.Header;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestFactory {

    private static final String url = "https://km.xpaas.lenovo.com/rest/api/content";

    public static void sendDataToConf(String userName, String password, String title, String spaceKey, String parentId, String htmlBody) throws HttpProcessException {

        String authorization = "Basic " +
            Base64.getUrlEncoder().encodeToString((userName + ":" + password).getBytes());

        Header[] headers = HttpHeader.custom()
            .authorization(authorization)
            .other("Content-Type", "application/json")
            .build();

        Map<String, Object> map = buildParam(title, spaceKey, parentId, htmlBody);

        HttpConfig config = HttpConfig.custom()
            .headers(headers)
            .url(url)
            .json(JsonUtils.toJson(map))
            .encoding("utf-8");

        String post = HttpClientUtil.post(config);
        System.out.println("post：" + post);
    }

    @NotNull
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

    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

}
