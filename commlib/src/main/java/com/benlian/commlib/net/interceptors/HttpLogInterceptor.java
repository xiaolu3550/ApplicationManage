package com.benlian.commlib.net.interceptors;

import android.text.TextUtils;

import com.benlian.commlib.log.LogUtil;
import com.benlian.commlib.utils.GsonUtils;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class HttpLogInterceptor implements Interceptor {
    public HttpLogInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //添加到责任链中
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    /**
     * 打印响应日志
     *
     * @param response
     * @return
     */
    private Response logForResponse(Response response) {
        LogUtil.i("********响应日志开始********");
        Response.Builder builder = response.newBuilder();
        Response clone = builder.build();
        LogUtil.i("url:" + clone.request().url());
        LogUtil.i("code:" + clone.code());
        if (!TextUtils.isEmpty(clone.message())) {
            LogUtil.i("message:" + clone.message());
        }
        ResponseBody body = clone.body();
        if (body != null) {
            MediaType mediaType = body.contentType();
            if (mediaType != null) {
                if (isText(mediaType)) {
                    String resp = null;
                    try {
                        resp = body.string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LogUtil.jsonI("响应:");
                    LogUtil.jsonI("url : " + clone.request().url() + "****" + GsonUtils.toJson(resp));
                    LogUtil.i("********响应日志结束********");
                    body = ResponseBody.create(mediaType, resp);
                    return response.newBuilder().body(body).build();
                } else {
                    LogUtil.i("响应内容 : " + "发生错误-非文本类型");
                }
            }
        }
        LogUtil.i("********响应日志结束********");
        return response;
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json")
                    || mediaType.subtype().equals("xml")
                    || mediaType.subtype().equals("html")
                    || mediaType.subtype().equals("webviewhtml")
                    || mediaType.subtype().equals("x-www-form-urlencoded")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 打印请求日志
     *
     * @param request
     */
    private void logForRequest(Request request) {
        String url = request.url().toString();
        LogUtil.i("========请求日志开始=======");
        LogUtil.i("请求方式 : " + request.method());
        LogUtil.i("url : " + url);
        LogUtil.i("Head : " + request.headers().toString());
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            MediaType mediaType = requestBody.contentType();
            if (mediaType != null) {
                LogUtil.i("请求内容类别 : " + mediaType.toString());
                if (isText(mediaType)) {
                    LogUtil.i("请求内容 : " + bodyToString(request));
                } else {
                    LogUtil.i("请求内容 : " + " 无法识别。");
                }
            }
        }
        LogUtil.i("========请求日志结束=======");
    }

    private String bodyToString(Request request) {
        Request req = request.newBuilder().build();
        String urlSub = null;
        Buffer buffer = new Buffer();
        try {
            req.body().writeTo(buffer);
            String message = buffer.readUtf8();
            urlSub = URLDecoder.decode(message, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "在解析请求内容时候发生了异常-非字符串";
        }
        return urlSub;
    }
}
