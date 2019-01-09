package com.example.raymond.weixin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class contactFragment extends Fragment {
    private String name;
    private String user;
    private List<Newslnfo> newsInfos;
    private LinearLayout loading;
    private ListView lvNews;
    private TextView tv_name;
    private TextView tv_user;
    private Newslnfo mNewslnfo;
    private Handler thread;

    @SuppressLint("HandlerLeak")

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);

        //初始化控件
        loading = (LinearLayout) view.findViewById(R.id.loading);
        lvNews = (ListView) view.findViewById(R.id.tv_list);

        thread = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==1){
                    String json = (String)msg.obj;
                    //调用解析工具
                    newsInfos = JsonParse.getNewsInfo(json);

                    if (newsInfos == null){
                        Toast.makeText(getActivity(),"解析失败",Toast.LENGTH_SHORT).show();
                    } else {
                        //更新界面
                        loading.setVisibility(View.INVISIBLE);
                        lvNews.setAdapter(new NewsAdapter());
                    }
                }
            }
        };
        membe();
        return view;
    }




    public void membe(){
        new Thread(){
            @Override
            public void run() {
                lianxi(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message mag = new Message();
                        mag.what = 0;
                        thread.sendMessage(mag);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String json = response.body().string();
                        Message mag = new Message();
                        mag.what = 1;
                        mag.obj = json;
                        thread.sendMessage(mag);
                    }
                });
            }
        }.start();
    }




    //解析
    public static class JsonParse{
        public static List<Newslnfo> getNewsInfo(String json){
        //使用Gson库解析JSON数据
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Newslnfo>>(){}.getType();
        List<Newslnfo> mNewslnfos = gson.fromJson(json,listType);
        return mNewslnfos;
        }
    }

    //ListView适配器
    private class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsInfos.size();
        }
        public View getView(int position,View convertView,ViewGroup parent){
            View view = View.inflate(getActivity(),R.layout.list_item,null);
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_user = view.findViewById(R.id.tv_user);
            mNewslnfo = newsInfos.get(position);
            tv_name.setText(mNewslnfo.getName());
            tv_user.setText(mNewslnfo.getUser());
            return view;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
    public static void lianxi (Callback callback) {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url("http://123.207.85.214/chat/member.php")
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

}


