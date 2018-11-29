/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import sk.vilten.vauth.client.ApplicationDescription;

/**
 *
 * @author vt
 */
public class ViltenHttpClient {
    private final CloseableHttpClient httpClient;
    private final CookieStore cookieStore;

    public final CookieStore getCookieStore() {
        return cookieStore;
    }

    /**
     * constructor
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws KeyManagementException
     * @throws ParseException
     */
    public ViltenHttpClient() throws NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException, ParseException
    {
        // nastavi aby sa neoveroval ssl
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();
        // Allow TLSv1
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null, (String string, SSLSession ssls) -> true);
        
        //cookie store
        cookieStore = new BasicCookieStore();

        final SocketConfig socketConfig = SocketConfig.custom()
            .setTcpNoDelay(true)
            .setSoTimeout(5000)
            .build();        
        //pooling http
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultSocketConfig(socketConfig);
        connectionManager.setMaxTotal(1000);
        connectionManager.setValidateAfterInactivity(10000);
        //vytvori samotneho klienta
        httpClient = HttpClients
                .custom()
                .setUserAgent(ApplicationDescription.app_full_name)
                .disableRedirectHandling()
                .setConnectionManager(connectionManager)
                //.setSSLSocketFactory(sslsf)
                .setDefaultCookieStore(cookieStore)
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                //vypnute nova verzia
                //.setMaxConnTotal(1000)
                .evictExpiredConnections()
                .evictIdleConnections(10,TimeUnit.SECONDS)
                .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                .build();
    }
    
    /**
     * funkcia pre get
     * @param get
     * @return
     * @throws IOException
     */
    public CloseableHttpResponse get(HttpGet get) throws IOException
    {
        RequestConfig config = RequestConfig
                .custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        get.setConfig(config);
        CloseableHttpResponse response = httpClient.execute(get);
        //httpClient.close();
        return response;
    }
    
    /**
     * vrati post
     * @param post
     * @return
     * @throws IOException
     */
    public CloseableHttpResponse post(HttpPost post) throws IOException
    {
        RequestConfig config = RequestConfig
                .custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        post.setConfig(config);
        CloseableHttpResponse response = httpClient.execute(post);
        //httpClient.close();
        return response;
    }

    /**
     * delete funkcia
     * @param delete
     * @return
     * @throws IOException
     */
    public CloseableHttpResponse delete(HttpDelete delete) throws IOException
    {
        RequestConfig config = RequestConfig
                .custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        delete.setConfig(config);
        CloseableHttpResponse response = httpClient.execute(delete);
        //httpClient.close();
        return response;
    }
    
    /**
     * put funkcia
     * @param put
     * @return
     * @throws IOException
     */
    public CloseableHttpResponse put(HttpPut put) throws IOException
    {
        RequestConfig config = RequestConfig
                .custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        put.setConfig(config);
        CloseableHttpResponse response = httpClient.execute(put);
        //httpClient.close();
        return response;
    }
    
    /**
     * pouzije sa custom metoda
     * @param request
     * @return
     * @throws IOException
     */
    public CloseableHttpResponse custom(HttpUriRequest request) throws IOException
    {
        CloseableHttpResponse response = httpClient.execute(request);
        //httpClient.close();
        return response;
    }
    
    /**
     * vrati z entity string
     * @param entity
     * @return
     * @throws IOException
     */
    public static String getEntityString(HttpEntity entity) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(entity.getContent()))) {
            String response = buffer.lines().collect(Collectors.joining("\n"));
            entity.getContent().close();
            return response;
        }
    }
}
