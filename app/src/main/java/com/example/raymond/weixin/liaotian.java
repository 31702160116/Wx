package com.example.raymond.weixin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
public class liaotian extends AppCompatActivity implements View.OnClickListener {
    //声明存储fragment的集合
    private ArrayList<Fragment> fragments;
    //声明两个导航对应fragment
    weixin_fragment weixinFragment;
    contactFragment contactListFragment;

    //声明ViewPager
    private ViewPager viewPager;
    FragmentManager fragmentManager;//声明fragment管理
    //声明导航栏中对应的布局
    private LinearLayout weixin, contact, find, self;
    //声明导航栏中包含的imageview和textview
    private ImageView weixin_img, contact_img, find_img, self_img;
    private TextView weixin_txt, contact_txt, find_txt, self_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liaotian);
        //初始化加载首页布局
        initView();
        //调用自定义initListener方法，为各个组件添加监听事件
        initListener();
        //设置默认选择的pager和导航栏的状态
        viewPager.setCurrentItem(0);
        weixin_img.setSelected(true);
        weixin_txt.setSelected(true);
    }

    private void initListener() {
        //为四大导航组件添加监听
        weixin.setOnClickListener(this);
        contact.setOnClickListener(this);

        //为viewpager添加页面变化的监听以及事件处理
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //根据位置直接决定显示哪个fragment
                viewPager.setCurrentItem(position);
                switch (position) {
                    case 0:
                        weixin_img.setSelected(true);
                        weixin_txt.setSelected(true);

                        contact_img.setSelected(false);
                        contact_txt.setSelected(false);
//                        find_img.setSelected(false);
//                        find_txt.setSelected(false);
//                        self_img.setSelected(false);
//                        self_txt.setSelected(false);

                        break;
                    case 1:
                        weixin_img.setSelected(false);
                        weixin_txt.setSelected(false);

                        contact_img.setSelected(true);
                        contact_txt.setSelected(true);
//                        find_img.setSelected(false);
//                        find_txt.setSelected(false);
//                        self_img.setSelected(false);
//                        self_txt.setSelected(false);

                        break;
//                    case 2:
//                        weixin_img.setSelected(false);
//                        weixin_txt.setSelected(false);
//
//                        contact_img.setSelected(false);
//                        contact_txt.setSelected(false);
//                        find_img.setSelected(true);
//                        find_txt.setSelected(true);
//                        self_img.setSelected(false);
//                        self_txt.setSelected(false);
//
//                        break;
//                    case 3:
//                        weixin_img.setSelected(false);
//                        weixin_txt.setSelected(false);
//
//                        contact_img.setSelected(false);
//                        contact_txt.setSelected(false);
//                        find_img.setSelected(false);
//                        find_txt.setSelected(false);
//                        self_img.setSelected(true);
//                        self_txt.setSelected(true);
//                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initView() {
        //在主布局中根据id找到ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //实例化所属四个fragment
        weixinFragment = new weixin_fragment();
        contactListFragment = new contactFragment();
        fragments = new ArrayList<>();
        //添加fragments到集合中
        fragments.add(weixinFragment);
        fragments.add(contactListFragment);
        fragmentManager = getSupportFragmentManager();
        //为ViewPager设置适配器用于部署fragments
        viewPager.setAdapter(new MyFragmentPagerAdapter(fragmentManager));


        weixin = (LinearLayout) findViewById(R.id.weixin);
        contact = (LinearLayout) findViewById(R.id.contact);

        weixin_img = (ImageView) findViewById(R.id.weixin_img);
        contact_img = (ImageView) findViewById(R.id.contact_img);

        weixin_txt = (TextView) findViewById(R.id.weixin_txt);
        contact_txt = (TextView) findViewById(R.id.contact_txt);
    }

    /**
     * 设置导航栏的点击事件并同步更新对应的ViewPager
     * 点击事件其实就是更改导航布局中对应的Text/ImageView
     * 的选中状态，配合drable中的selector更改图片以及文字变化
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weixin:
                viewPager.setCurrentItem(0);
                weixin_img.setSelected(true);
                weixin_txt.setSelected(true);

                contact_img.setSelected(false);
                contact_txt.setSelected(false);
                break;
            case R.id.contact:
                viewPager.setCurrentItem(1);
                weixin_img.setSelected(false);
                weixin_txt.setSelected(false);
                contact_img.setSelected(true);
                contact_txt.setSelected(true);

                break;
        }
    }

    //创建FragmentPagerAdapter
    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}

