package com.yitong.appsdk.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.yitong.appsdk.config.AppConstant;
import com.yitong.appsdk.config.SessionConstant;
import com.yitong.appsdk.core.encrypt.AESHelper;
import com.yitong.appsdk.core.encrypt.Base64;
import com.yitong.appsdk.core.encrypt.RsaHelper;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * OKHttp网络请求工具类
 * Created by zhuzengpeng on 2016/3/7.
 */
public class OkHttpUtils {

    private static final String TAG = "OkHttpUtils";
    private static final OkHttpClient httpClient = new OkHttpClient();

    /**上下文对象*/
    private Context context;
    //网络请求回调
    protected HttpCallback httpCallback;

    public OkHttpUtils(Context context) {
        this.context = context;
    }
    /**
     * 主要接受子线程发送的数据， 并用此数据配合主线程更新UI。
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == 0) {
                Log.d(TAG, "handler--网络请求失败");
            } else if (msg.what == 1) {
                Log.d(TAG, "handler--网络请求成功");
                httpCallback.onSuccess(msg.obj);
            }
        }
    };

    public static OkHttpUtils build(Context context) {
            return new OkHttpUtils(context);
    }

    /**
     * 调用POST接口
     * @param url
     * @param params
     */
    public void post(String url, Map<String, Object> params, HttpCallback callback) {
        if(!StringUtils.isNotBlank(SessionConstant.aeskey)) {
            refreshSession();
        }
        this.httpCallback = callback;
        //把需要POST的参数转成JSON格式
        String paramsJson = JSON.toJSONString(params);
        Request.Builder builder = new Request.Builder().url(url);
        //构造请求头
        builder.addHeader(SessionConstant.HEADERNAME_X_AUTH_TOKEN, SessionConstant.sessionid);
        //构造请求体
        //参数需要AES加密
        byte[] paramsEncry = AESHelper.encrypt(paramsJson, SessionConstant.aeskey);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), Base64.encode(paramsEncry));
        Request request = builder.post(requestBody).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "客户端post请求失败!");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String respStr = response.body().string();
                    Log.d(TAG, "接口返回值1111：" + respStr);
                    dealReturnValue(respStr);
                } else {
                    Log.d(TAG, "网络请求失败");
                }
            }
        });

    }

    /**
     * 取得接口地址的完整链接(域名+接口地址)
     * @param relativeUrl 接口的相对地址
     * @return
     */
    public static String getAbsoluteUrl(String relativeUrl) {
        if (relativeUrl.startsWith("http://") || relativeUrl.startsWith("https://")) {
            return relativeUrl;
        } else {
            return AppConstant.SERVER_HOST + relativeUrl;
        }
    }

    /**
     * 与服务端握手取得SESSIONID和AESKEY
     */
    public boolean refreshSession() {
        FutureTask<Boolean> futureTask = new FutureTask<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                String clientAeskey = AESHelper.generateAeskey();
                Log.d(TAG, "客户端AESKEY:" + clientAeskey);
                try {
                    String clientAeskeyEncry = RsaHelper.encrypt(clientAeskey);
                    Log.d(TAG, "clientAeskeyEncry:" + clientAeskeyEncry);
                    //构造请求体
                    RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), clientAeskeyEncry);
                    Request request = new Request.Builder()
                            .url(getAbsoluteUrl(AppConstant.SESSION_REFRESH_URL))
                            .post(requestBody)
                            .build();
                    Response response = httpClient.newCall(request).execute();
                    String resultEncry = response.body().string();
                    String result = new String(AESHelper.decrypt(Base64.decode(resultEncry), clientAeskey));
                    Log.d(TAG, "解密后报文：" + result);
                    JSONObject jsonObject = JSON.parseObject(result);
                    SessionConstant.sessionid = jsonObject.getString(SessionConstant.SESSIONID);
                    SessionConstant.aeskey = jsonObject.getString(SessionConstant.AESKEY);
                    Log.d(TAG, "调完刷新SESSION接口后,本地保存sessionid：" + SessionConstant.sessionid + "-----aeskey：" + SessionConstant.aeskey);
                    return true;
                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
            }
        });
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(futureTask);
        try {
            return futureTask.get(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param value 服务端返回的未解密字符串
     * @param aeskey AES加密密钥
     */
    private void dealReturnValue(String value, String aeskey) {
        Message msg = handler.obtainMessage();
        Log.d(TAG, "加密前报文：" + value);
        try {
            String result = new String(AESHelper.decrypt(Base64.decode(value), aeskey));
            Log.d(TAG, "解密后报文：" + result);
            msg.obj = result;
        } catch (Exception e) {
            msg.what = 0;
        }
        //通知handler处理
        handler.sendMessage(msg);
    }

    /**
     *
     * @param value 服务端返回的未解密字符串
     */
    private void dealReturnValue(String value) {
        Log.d(TAG, "加密前报文：" + value);
        Message msg = handler.obtainMessage();
        try {
            byte[] valuee = Base64.decode(value);
            byte[] valueb = AESHelper.decrypt(valuee, SessionConstant.aeskey);
            String result = valueb == null? "" : new String(valueb);
            Log.d(TAG, "解密后报文：" + result);
            msg.what = 1;
            msg.obj = result;
        } catch (Exception e) {
            msg.what = 0;
        }
        //通知handler处理
        handler.sendMessage(msg);
    }

}
