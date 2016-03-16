package com.yitong.appdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.yitong.appsdk.http.HttpCallback;
import com.yitong.appsdk.http.OkHttpUtils;

/**
 * Created by zhuzengpeng on 2016/3/14.
 */
public class LoginActivity extends Activity {

    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        loginButton = (Button)findViewById(R.id.button_login);
        loginButton.setOnClickListener(loginClick);
    }

    //登陆按钮事件
    View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean flag = OkHttpUtils.build(LoginActivity.this).refreshSession();
            if(flag) {
                Toast.makeText(LoginActivity.this, "与服务端握手成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                LoginActivity.this.startActivity(intent);
            }else {
                Toast.makeText(LoginActivity.this, "与服务端握手失败", Toast.LENGTH_SHORT).show();
            }

        }
    };
}