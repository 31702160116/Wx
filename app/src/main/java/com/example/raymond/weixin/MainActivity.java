package com.example.raymond.weixin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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


public class MainActivity extends AppCompatActivity {
    private Button btn_send;
    private Button btn_zhuce;
    private EditText user;
    private EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.user);
        pwd = findViewById(R.id.pwd);

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_zhuce = (Button) findViewById(R.id.btn_zhuce);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passLiao();
            }
        });
        btn_zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passZhu();
            }
        });
    }
    public void passZhu(){
        //创建Intent对象,启动跳转到注册界面
        Intent intent = new Intent(this, zhuce.class);
        startActivity(intent);
    }

    public void passLiao() {
        final String zUser = user.getText().toString().trim();
        final String zPwd = pwd.getText().toString().trim();
        if (zUser.equals("")) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
        } else if (zPwd.equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        } else {
            denglupost(zUser, zPwd, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "登录失败，请检查账号密码是否正确", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    runOnUiThread(new Thread() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();

                            User user = gson.fromJson(json, User.class);
                            if (user.status.equals("登陆成功")){
                                SharedPreferences dl = getSharedPreferences("data",MODE_PRIVATE);
                                SharedPreferences.Editor edit = dl.edit();
                                edit.putString("user",user.user);
                                edit.putString("name",user.name);
                                edit.commit();
                                Toast.makeText(MainActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this,liaotian.class);
                                startActivity(intent);
                            }
                        }

                    });
                }
            });
        }



    }
    //User实体类
    public class User {
        private String status;
        private String user;
        private String name;

        User(String status, String user, String name) {
            this.status = status;
            this.user = user;
            this.name = name;
        }
    }

    private static void denglupost(String zUser, String zPwd, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("user",zUser)
                .add("password", zPwd)
                .build();
        final Request request = new Request.Builder()
                .url("http://123.207.85.214/chat/login.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
