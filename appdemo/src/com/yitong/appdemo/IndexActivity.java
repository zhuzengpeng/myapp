package com.yitong.appdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.yitong.appsdk.http.HttpCallback;
import com.yitong.appsdk.http.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuzengpeng on 2016/3/15.
 */
public class IndexActivity extends Activity {

    private Button queryButton;
    private Button loginoutButton;
    private static final String TAG = "IndexActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
    }

    private void initView() {
        queryButton = (Button)findViewById(R.id.button_query);
        loginoutButton = (Button)findViewById(R.id.button_loginout);
        queryButton.setOnClickListener(queryClick);
        loginoutButton.setOnClickListener(loginoutClick);
    }

    /***查询按钮事件***/
    View.OnClickListener queryClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("username", "朱增鹏");
            params.put("password", "zzzzzzzz1111");
            OkHttpUtils.build(IndexActivity.this).post("http://182.180.11.71:9060/ipf/redis/get", params, new HttpCallback() {
                @Override
                public void onSuccess(Object result) {
                    Log.d(TAG, "接口调用成功：" + result);
                    Toast.makeText(IndexActivity.this, "调用服务端接口成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail(int errorCode, String errorMsg) {
                    Log.d(TAG, "接口调用失败");
                    Toast.makeText(IndexActivity.this, "调用服务端接口失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    /***退出登陆按钮事件***/
    View.OnClickListener loginoutClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

        }
    };
}