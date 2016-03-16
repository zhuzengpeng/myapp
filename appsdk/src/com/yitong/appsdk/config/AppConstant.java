package com.yitong.appsdk.config;

/**
 * 配置一些客户端需要的配置
 * Created by zhuzengpeng on 2016/3/7.
 */
public class AppConstant {

    //服务端接口地址
    public static final String SERVER_HOST = "http://182.180.11.71:9060/ipf";

    //客户端首次与服务端连接时调用此接口
    public static final String SESSION_REFRESH_URL = "/session/refreshSession";

    //登陆接口
    public static final String CLIENT_LOGIN_URL = "/login/clientLogin";
}