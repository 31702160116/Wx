package com.example.raymond.weixin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class zhuce extends AppCompatActivity {
    private EditText name;
    private EditText user;
    private EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);

        //初始化控件
        name = findViewById(R.id.name);
        user = findViewById(R.id.user);
        pwd = findViewById(R.id.pwd);

    }
    public void click(View view){
        final String zUser = user.getText().toString().trim();
        final String zPwd = pwd.getText().toString().trim();
        final String zName = name.getText().toString().trim();
        if (zName.equals("")) {
            Toast.makeText(this,"请输入姓名",Toast.LENGTH_SHORT).show();
        }else if (zUser.equals("")) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
        }else if (zPwd.equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        }else
            zhuce(zName, zUser, zPwd, new Callback() {
                //请求失败
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(zhuce.this, "注册失败,请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //请求成功
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            status status = gson.fromJson(json,status.class);
                            if (status.status.equals("注册成功")){
                                Toast.makeText(zhuce.this,"注册成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(zhuce.this,MainActivity.class);
                                startActivity(intent);
                            }
                            if (status.status.equals("用户名重复")){
                                Toast.makeText(zhuce.this,"账号重复",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
    }

    //status实体类
    public class status {
        private String status;

        status(String status) {
            this.status = status;
        }
    }


    public static void zhuce(String zName, String zUser, String zPwd,Callback callback) {
        OkHttpClient client = new OkHttpClient();
        //post 表单创建
        RequestBody body = new FormBody.Builder()
                .add("name",zName)
                .add("user",zUser)
                .add("password",zPwd)
                .build();
        //访问请求
        final Request request = new Request.Builder()
                .url("http://123.207.85.214/chat/register.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
