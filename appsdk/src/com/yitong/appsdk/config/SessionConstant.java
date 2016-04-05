package com.yitong.appsdk.config;

/**
 * 客户端本地保存SESSION相关的变量
 * Created by zhuzengpeng on 2016/3/7.
 */
public class SessionConstant {

    //与SESSION绑定的AES加密密钥
    public static String aeskey = null;

    //返回JSON值中sessionId的KEY
    public static final String SESSIONID = "sessionId";

    //上次网络请求返回的sessionid(本次网络请求完都更新一下)
    public static String sessionid = null;

    //与SESSION绑定的AES加密密钥
    public static final String AESKEY = "aeskey";

    //当前SESSION是否是登陆成功(1:登陆成功  0:未登陆)
    public static final String LOGIN_FLAG = "loginFlag";
    //登陆成功标识
    public static final String LOGIN_FLAG_YES = "1";
    //登陆失败标识
    public static final String LOGIN_FLAG_NO = "0";

    //spring session请求头和响应头都通过此变量传递SESSIONID
    public static final String HEADERNAME_X_AUTH_TOKEN = "x-auth-token";



}