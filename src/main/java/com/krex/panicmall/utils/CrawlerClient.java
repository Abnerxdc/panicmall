
package com.krex.panicmall.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 爬虫代理
 *
 * @author cz__wp (Ailk No.)
 * @version 1.0
 * @category com.ailk.iot.caught.httpclient
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * @since 2018年7月27日
 */
public class CrawlerClient {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerClient.class);
    // httpclient
    private CloseableHttpClient httpclient = null;
    // 不重试
    private HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

        @Override
        public boolean retryRequest(IOException exception, int executionCount,
                                    HttpContext context) {
            return false;
        }
    };
    // 连接超时时间
    private static final int CONN_TIMEOUT = 20000;
    // 代理的IP、端口
    private String proxy_ip = "-1";
    private int proxy_port = -1;
    private static RequestConfig config;

    /**
     * 需要使用代理的httpClient初始化
     *
     * @param ip
     * @param port
     */
    public CrawlerClient(String ip, int port) {
        this.proxy_ip = ip;
        this.proxy_port = port;
        // 设置代理
        HttpHost proxy = new HttpHost(ip, port);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        config = RequestConfig.custom().setSocketTimeout(CONN_TIMEOUT)
                .setConnectTimeout(CONN_TIMEOUT).setConnectionRequestTimeout(CONN_TIMEOUT)
                .build();
        httpclient = HttpClients.custom().setDefaultRequestConfig(config)
                .setRoutePlanner(routePlanner).setRetryHandler(myRetryHandler).build();
    }

    /**
     * 不使用代理的http client
     */
    public CrawlerClient() {
        config = RequestConfig.custom().setSocketTimeout(CONN_TIMEOUT)
                .setConnectTimeout(CONN_TIMEOUT).setConnectionRequestTimeout(CONN_TIMEOUT)
                .build();
        httpclient = HttpClients.custom().setDefaultRequestConfig(config)
                .setRetryHandler(myRetryHandler).build();
    }

    /**
     * 爬虫，返回Map key为： httpcode(http状态码) value为： html(爬虫页面内容)
     *
     * @param url           爬虫URL
     * @param UA            访问UA
     * @param timeout       设置超时时间
     * @param allowRedirect 是否允许重定向
     * @param charset       字符集
     * @return
     */
    public Map<String, String> getHtml(String url, String UA, int timeout,
                                       boolean allowRedirect, String charset) {
        Map<String, String> resultMap = new HashMap<String, String>();
        String ret = "";
        if (url == null || "".equals(url)) {
            logger.error("url为空");
            return resultMap;
        }
        // 执行get请求
        CloseableHttpResponse response = null;
        HttpGet httpget = null;
        try {
            logger.info("开始请求 url:{} ip:{} port:{}", url, this.proxy_ip, this.proxy_port);
            httpget = new HttpGet(url);
            /**
             * 设置超时参数、跳转参数、字符集参数
             */
            RequestConfig reqconf = RequestConfig.custom().setSocketTimeout(timeout)
                    .setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .setRedirectsEnabled(allowRedirect).build();
            httpget.setConfig(reqconf);
//			httpget.setHeader("User-Agent", UA);
//			httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
//			httpget.setHeader("Accept-Encoding", "gzip, deflate");
//			httpget.setHeader("accept",
//					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//

            httpget.setHeader("User-Agent", UA);
            httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpget.setHeader("Accept-Encoding", "gzip, deflate");
            httpget.setHeader("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            httpget.setHeader("Cookie", "UM_distinctid=16d7bbe6e1f2a5-0b594c50f5b587-67e1b3f-100200-16d7bbe6e20262; qHistory=aHR0cDovL2ljcC5jaGluYXouY29tLyvnvZHnq5nlpIfmoYg=; CNZZDATA5082706=cnzz_eid%3D741735631-1570785653-http%253A%252F%252Ficp.chinaz.com%252F%26ntime%3D1570785653");
            httpget.setHeader("Connection", "keep-alive");
            response = httpclient.execute(httpget);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            int status = response.getStatusLine().getStatusCode();
            logger.info("url:{}  ip:{}  port:{} status:{}", url, this.proxy_ip,
                    this.proxy_port, status);
            if (status == 200) {
                // 返回响应内容
                if (charset != null) {
                    ret = EntityUtils.toString(entity, charset);
                } else {
                    ret = EntityUtils.toString(entity, "utf-8");
                }
            }
            resultMap.put("httpcode", String.valueOf(status));
            resultMap.put("html", ret);
        } catch (Exception e) {
            logger.error("url:{} ip:{}  port:{} 爬虫异常", url, this.proxy_ip,
                    this.proxy_port, e);
        } finally {
            if (null != httpget) {
                try {
                    httpget.abort();
                } catch (Exception e) {
                }
                try {
                    httpget.completed();
                } catch (Exception e) {
                }
            }
            if (null != response) {
                try {
                    response.close();
                } catch (Exception e) {
                }
            }
        }
        return resultMap;
    }

    /**
     * 爬虫，返回Map key为： httpcode(http状态码) value为： html(爬虫页面内容)
     *
     * @param url           爬虫URL
     * @param UA            访问UA
     * @param timeout       设置超时时间
     * @param allowRedirect 是否允许重定向
     * @param charset       字符集
     * @return
     */
    public Map<String, String> getHtml(String url, String UA, String cookie, int timeout,
                                       boolean allowRedirect, String charset) {
        Map<String, String> resultMap = new HashMap<String, String>();
        String ret = "";
        if (url == null || "".equals(url)) {
            logger.error("url为空");
            return resultMap;
        }
        // 执行get请求
        CloseableHttpResponse response = null;
        HttpGet httpget = null;
        try {
            logger.info("开始请求 url:{} ", url);
            httpget = new HttpGet(url);
            /**
             * 设置超时参数、跳转参数、字符集参数
             */
            RequestConfig reqconf = RequestConfig.custom().setSocketTimeout(timeout)
                    .setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .setRedirectsEnabled(allowRedirect).build();
            httpget.setConfig(reqconf);
//			httpget.setHeader("User-Agent", UA);
//			httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
//			httpget.setHeader("Accept-Encoding", "gzip, deflate");
//			httpget.setHeader("accept",
//					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//

            httpget.setHeader("User-Agent", UA);
            httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            httpget.setHeader("Accept-Encoding", "gzip, deflate");
            httpget.setHeader("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            httpget.setHeader("Cookie", cookie);
            httpget.setHeader("Connection", "keep-alive");
            response = httpclient.execute(httpget);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            int status = response.getStatusLine().getStatusCode();
            logger.info("url:{}  status:{}", url, status);
            if (status == 200) {
                // 返回响应内容
                if (charset != null) {
                    ret = EntityUtils.toString(entity, charset);
                } else {
                    ret = EntityUtils.toString(entity, "utf-8");
                }
            }
            resultMap.put("httpcode", String.valueOf(status));
            resultMap.put("html", ret);
        } catch (Exception e) {
            logger.error("url:{} ip:{}  port:{} 爬虫异常", url, this.proxy_ip,
                    this.proxy_port, e);
        } finally {
            if (null != httpget) {
                try {
                    httpget.abort();
                } catch (Exception e) {
                }
                try {
                    httpget.completed();
                } catch (Exception e) {
                }
            }
            if (null != response) {
                try {
                    response.close();
                } catch (Exception e) {
                }
            }
        }
        return resultMap;
    }

    /**
     * 代理测试连通性使用
     *
     * @param url
     * @param UA
     * @return
     */
    public boolean testWithClose(String url, String UA, int con_timeout) {
        boolean result = false;
        // 执行get请求
        CloseableHttpResponse response = null;
        HttpGet httpget = null;
        try {
            logger.info("连通测试开始 ip:{} port:{}", this.proxy_ip, this.proxy_port);
            httpget = new HttpGet(url);
            /**
             * 设置超时参数、跳转参数、字符集参数
             */
            if (con_timeout <= 0) {
                con_timeout = CONN_TIMEOUT;
            }
            RequestConfig reqconf = RequestConfig.custom().setSocketTimeout(con_timeout)
                    .setConnectTimeout(con_timeout)
                    .setConnectionRequestTimeout(con_timeout)
                    .setCookieSpec(CookieSpecs.STANDARD).build();
            httpget.setConfig(reqconf);
            httpget.setHeader("User-Agent", UA);
            response = httpclient.execute(httpget);
            int status = response.getStatusLine().getStatusCode();
            logger.info("连通测试 结束ip:{}  port:{} status:{}", this.proxy_ip, this.proxy_port,
                    status);
            if (status == 200) {
                result = true;
            }
        } catch (Exception e) {
            logger.error("ip:{}  port:{} 连通测试异常", this.proxy_ip, this.proxy_port);
        } finally {
            if (null != httpget) {
                try {
                    httpget.abort();
                } catch (Exception e) {
                }

                try {
                    httpget.completed();
                } catch (Exception e) {
                }
            }
            if (null != response) {
                try {
                    response.close();
                } catch (Exception e) {
                }
            }
            if (null != httpclient) {
                try {
                    httpclient.close();
                } catch (Exception e) {
                }
            }
        }

        return result;
    }

    /**
     * 抓取页面内容，不允许跳转
     *
     * @param url
     * @param UA
     * @return 页面内容
     */
    public String getHtmlForbidRedirect(String url, String UA) {
        String html = "";
        Map<String, String> resultMap = getHtml(url, UA, CONN_TIMEOUT,
                PhoneStart.ForbidRedirect, null);
        if (!resultMap.isEmpty()) {
            html = resultMap.get("html");
            int statusCode = Integer.parseInt(resultMap.get("httpcode"));
            if (!isTianyanchaSuccess(html, statusCode, url)) {
                html = "";
                logger.info("url:{} ip:{}  port:{}  代理被禁", url, this.proxy_ip,
                        this.proxy_port);
            }
        }
        return html;
    }

    /**
     * 抓取页面内容，不允许跳转
     *
     * @param url
     * @param UA
     * @return 页面内容
     */
    public String getHtmlForbidRedirect(String url, String UA, String cookie) {
        String html = "";
        Map<String, String> resultMap = getHtml(url, UA, cookie, CONN_TIMEOUT,
                PhoneStart.ForbidRedirect, null);
        if (!resultMap.isEmpty()) {
            html = resultMap.get("html");
            int statusCode = Integer.parseInt(resultMap.get("httpcode"));
            if (!isTianyanchaSuccess(html, statusCode, url)) {
                html = "";
                logger.info("");
            }
        }

        return html;
    }

    /**
     * 抓取页面内容，并指定字符进行解析
     *
     * @param url     页面URL
     * @param UA
     * @param charset 字符集
     * @return
     */
    public String getHtmlWithCharset(String url, String UA, String charset) {
        Map<String, String> resultMap = getHtml(url, UA, CONN_TIMEOUT,
                PhoneStart.AllowedRedirect, charset);
        if (resultMap.containsKey("httpcode")) {
            try {
                int statusCode = Integer.parseInt(resultMap.get("httpcode"));
                if (statusCode == 200 && resultMap.containsKey("html")) {
                    return resultMap.get("html");
                }
            } catch (Exception e) {
                logger.error("httpcode：{}状态异常", resultMap.get("httpcode"), e);
            }
        }
        return "";
    }

    /**
     * 默认使用UTF-8字符集解析爬虫数据
     *
     * @param url
     * @param UA
     * @return
     */
    public String getHtml(String url, String UA) {
        return getHtmlWithCharset(url, UA, "utf-8");
    }

    /**
     * 判断天眼查是否被禁
     *
     * @param html   html
     * @param status httpcode
     * @param url    url
     * @return
     */
    private boolean isTianyanchaSuccess(String html, int status, String url) {
        boolean result = true;
        if (url.contains("tianyancha")) {
            if (status == 200 && html.contains("请输入手机号")) {
                result = false;
            }
            if (status == 302 || status == 301) {
                result = false;
            }
        }
        return result;
    }

    /**
     * 关闭
     */
    public void close() {
        try {
            if (httpclient != null) {
                httpclient.close();
            }
        } catch (Exception e) {
            logger.error("关闭httpClient 异常", e);
        }
    }
}
