package com.example.raymond.weixin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class weixin_fragment extends Fragment implements View.OnClickListener{
    private List<Msg> msgList =new ArrayList<Msg>();

//    private View view;
    private Button btn_send;
    private EditText input_text;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private String user, name;//登录的user和用户名
    private Handler handler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weixin_fragment,container,false);

        btn_send = view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        input_text = view.findViewById(R.id.input_text);
        msgRecyclerView = view.findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        SharedPreferences qz = getContext().getSharedPreferences("data", MODE_PRIVATE);
        name = qz.getString("name", "");
        user = qz.getString("user", "");
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        Toast.makeText(getActivity(),"信息没发出去，请稍后再试！",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        msgRecyclerView.setAdapter(adapter);
                        msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                        input_text.setText(""); // 清空输入框中的内容
                        break;
                    default:
                        break;
                }
            }
        };
        return view;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                chatPOST();
                break;
        }
    }

    public static void liao(String user, String chat, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        //POST 表单创建
        RequestBody body = new FormBody.Builder()
                .add("user", user)
                .add("chat", chat)
                .build();
        //访问请求
        final Request request = new Request.Builder()
                .url("http://123.207.85.214/chat/chat1.php")
                //提交表单
                .post(body)
                .build();
            //网络异步回调
        client.newCall(request).enqueue(callback);
    }

    private void chatPOST() {
        final String content = input_text.getText().toString();
        if (content.equals("")) {
            Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        msgList.clear();
        liao(user, content, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String JSON = response.body().string();
                //gson解析
                Gson gson = new Gson();
                List<ChatBean> list = gson.fromJson(JSON, new TypeToken<List<ChatBean>>() {
                }.getType());
                //倒序
                Collections.reverse(list);
                for (ChatBean i : list) {
                    //根据返回的name 判断是否为自己账号所发
                    String tmp_name = i.getName();
                    if (!tmp_name.equals(name)) {
                        msgList.add(new Msg(i.getChat(), tmp_name, i.getTime(), Msg.TYPE_RECEIVED));
                    } else {
                        msgList.add(new Msg(i.getChat(), tmp_name, i.getTime(), Msg.TYPE_SENT));
                    }
                }
                //通知Handler执行刷新操作
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });
    }
}

