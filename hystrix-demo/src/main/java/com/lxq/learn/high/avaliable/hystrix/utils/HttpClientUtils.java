package com.lxq.learn.high.avaliable.hystrix.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author lxq
 * @create 2019/9/4 0:03
 */
public class HttpClientUtils {
    public static String doGet(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        try {
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                entity = response.getEntity();
                String content = EntityUtils.toString(entity, "UTF-8");
                return content;
            }
            throw new RuntimeException("响应异常");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (response != null) {
                    EntityUtils.consume(entity);
                    response.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
