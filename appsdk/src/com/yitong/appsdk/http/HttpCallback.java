package com.yitong.appsdk.http;

public abstract class HttpCallback<T>{
    /**
     * 请求成功回调
     * @param result
     */
    public abstract void onSuccess(T result);

    /**
     * 请求失败回调
     * @param errorCode
     * @param errorMsg
     */
    public abstract void onFail(int errorCode,String errorMsg);

}
