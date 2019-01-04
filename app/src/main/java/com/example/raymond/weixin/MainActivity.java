package com.example.raymond.weixin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    public void passLiao(){


//        Intent intent = new Intent(this,liaotian.class);  //跳转到聊天室
//        startActivity(intent);
    }
    public void passZhu(){
        //创建Intent对象,启动跳转到注册界面
        Intent intent = new Intent(this, zhuce.class);
        startActivity(intent);
    }
}
