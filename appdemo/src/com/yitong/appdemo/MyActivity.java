package com.yitong.appdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.yitong.appsdk.http.HttpCallback;
import com.yitong.appsdk.http.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

public class MyActivity extends Activity {

    private static final String TAG = "MainActivity";

    private EditText editText;
    private Button queryButton;
    private Button testButton;
    private Button goButton;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
    }

    private void initView() {
        editText = (EditText)findViewById(R.id.editText);
        queryButton = (Button)findViewById(R.id.button_query);
        testButton =  (Button)findViewById(R.id.button_test);
        goButton = (Button)findViewById(R.id.button_gologin);
        queryButton.setOnClickListener(queryClick);
        testButton.setOnClickListener(testClick);
        goButton.setOnClickListener(goLoginClick);
    }

    //刷新按钮事件
    View.OnClickListener queryClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String sessionId = editText.getText().toString();
            new Thread(){
                @Override
                public void run(){
                    OkHttpUtils.build(MyActivity.this).refreshSession();
                }
            }.start();
        }
    };


    View.OnClickListener testClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new Thread(){
                @Override
                public void run(){
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("username", "朱增鹏");
                    params.put("password", "zzzzzzzz1111");
                    OkHttpUtils.build(MyActivity.this).post("http://182.180.11.71:9060/ipf/redis/get", params, new HttpCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            Log.d(TAG, "接口调用成功：" + result);
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            Log.d(TAG, "接口调用失败");
                        }
                    });
                }
            }.start();

        }
    };

    //跳转至登陆界面
    View.OnClickListener goLoginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MyActivity.this, LoginActivity.class);
            MyActivity.this.startActivity(intent);
        }
    };
}
