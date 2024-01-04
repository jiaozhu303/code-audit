package com.dj.tool.common

import com.google.common.collect.Lists
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpPost
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.ssl.SSLContextBuilder
import org.apache.http.util.EntityUtils
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import java.util.*
import java.util.function.Consumer
import javax.net.ssl.SSLContext

object HttpRequestFactory {
    private var sslcontext: SSLContext? = null
    private var verifier: NoopHostnameVerifier? = null
    private var sslConnectionSocketFactory: SSLConnectionSocketFactory? = null

    init {
        try {
            sslcontext =
                SSLContextBuilder().loadTrustMaterial(null) { china: Array<X509Certificate?>?, authType: String? -> true }
                    .build()
            verifier = NoopHostnameVerifier.INSTANCE
            sslConnectionSocketFactory = SSLConnectionSocketFactory(sslcontext, verifier)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: KeyManagementException) {
            throw RuntimeException(e)
        } catch (e: KeyStoreException) {
            throw RuntimeException(e)
        }
    }

    @Throws(
        NoSuchAlgorithmException::class,
        IOException::class,
        KeyManagementException::class,
        KeyStoreException::class
    )
    fun sendDataToConf(
        apiURL: String?,
        userName: String,
        password: String,
        title: String,
        spaceKey: String,
        parentId: String,
        htmlBody: String,
        successNotificationConsumer: Consumer<String?>,
        failNotificationConsumer: Consumer<String?>
    ) {
        val map: Map<String, Any> = buildParam(title, spaceKey, parentId, htmlBody)
        send(
            apiURL,
            map,
            Constants.CHARSET_UTF_8,
            userName,
            password,
            successNotificationConsumer,
            failNotificationConsumer
        )
    }

    private fun buildParam(title: String, spaceKey: String, parentId: String, htmlBody: String): HashMap<String, Any> {
        val param = HashMap<String, Any>()

        val space = HashMap<String, Any>()
        space["key"] = spaceKey

        val ancestors = HashMap<String, Any>()
        ancestors["id"] = parentId

        val storage = HashMap<String, Any>()
        storage["representation"] = "storage"
        storage["value"] = htmlBody

        val body = HashMap<String, Any>()
        body["storage"] = storage

        param["title"] = title
        param["type"] = "page"
        param["status"] = "current"
        param["space"] = space
        param["ancestors"] = Lists.newArrayList(ancestors)
        param["body"] = body
        return param
    }

    @Throws(
        KeyManagementException::class,
        NoSuchAlgorithmException::class,
        ClientProtocolException::class,
        IOException::class,
        KeyStoreException::class
    )
    fun send(
        url: String?,
        map: Map<String, Any>?,
        encoding: String?,
        userName: String,
        password: String,
        successNotificationConsumer: Consumer<String?>,
        failNotificationConsumer: Consumer<String?>
    ): String {
        val body = ""

        //绕过证书验证，处理https请求
//        SSLContext sslcontext = new SSLContextBuilder().loadTrustMaterial(null, (china, authType) -> true).build();
//        NoopHostnameVerifier verifier = NoopHostnameVerifier.INSTANCE;
//        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, verifier);
        val connManager = PoolingHttpClientConnectionManager()
        connManager.maxTotal = 2
        connManager.defaultMaxPerRoute = 2

        val requestConfig = RequestConfig.custom()
            .setAuthenticationEnabled(true)
            .setConnectionRequestTimeout(Constants.CONNECTION_REQUEST_TIMEOUT)
            .setSocketTimeout(Constants.SOCKET_TIMEOUT)
            .setConnectTimeout(Constants.CONNECT_TIMEOUT)
            .setRedirectsEnabled(false)
            .setCookieSpec(CookieSpecs.STANDARD)
            .build()

        val client = HttpClients.custom()
            .setConnectionManager(connManager)
            .setDefaultRequestConfig(requestConfig)
            .setSSLSocketFactory(sslConnectionSocketFactory)
            .build()

        //创建post方式请求对象
        val httpPost = HttpPost(url)

        httpPost.entity = StringEntity(JsonUtils.toJson(map), Constants.MIME_TYPE_APPLICATION_JSON, encoding)

        val authorization = Constants.BASIC_AUTH_PREFIX +
                Base64.getUrlEncoder().encodeToString("$userName:$password".toByteArray())
        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader(Constants.HEADER_NAME_CONTENT_TYPE, Constants.MIME_TYPE_APPLICATION_JSON)
        httpPost.setHeader(Constants.HEADER_NAME_AUTHORIZATION, authorization)

        try {
            //执行请求操作，并拿到结果（同步阻塞）
            val response = client.execute(httpPost)

            //获取结果实体
            val entity = response.entity

            val isSuccess =
                response.statusLine.statusCode == 200 || response.statusLine.statusCode == 201 || response.statusLine.statusCode == 202
            if (!isSuccess) {
                failNotificationConsumer.accept("sync to confluence fail!")
            }
            if (isSuccess) {
                successNotificationConsumer.accept("sync to confluence successful!")
            }
            EntityUtils.consume(entity)
            //释放链接
            response.close()
        } catch (e: Exception) {
            failNotificationConsumer.accept("sync to confluence exception - execute network request error :" + e.message)
            e.printStackTrace()
//            failNotificationConsumer.accept("execute network request error~")
        } finally {
            client.close()
            connManager.close()
        }
        return body
    }
}
