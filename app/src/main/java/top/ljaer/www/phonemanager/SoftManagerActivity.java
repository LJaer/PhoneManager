package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import top.ljaer.www.phonemanager.bean.AppInfo;
import top.ljaer.www.phonemanager.engine.AppEngine;
import top.ljaer.www.phonemanager.utils.MyAsynTaks;

/**
 * Created by LJaer on 16/10/7.
 */

public class SoftManagerActivity extends Activity {

    private ListView lv_softmanager_applicaiton;
    private ProgressBar loading;
    private List<AppInfo> list;
    //用户程序集合
    private List<AppInfo> userappinfo;
    //系统程序集合
    private List<AppInfo> systemappinfo;
    private TextView tv_softmanager_userorsystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmanager);
        //初始化控件
        lv_softmanager_applicaiton = (ListView) findViewById(R.id.lv_softmanager_applicaiton);
        loading = (ProgressBar) findViewById(R.id.loading);
        tv_softmanager_userorsystem = (TextView) findViewById(R.id.tv_softmanager_userorsystem);

        //加载数据
        fillData();

        listviewOnScroll();
    }

    /**
     * listview滑动监听事件
     */
    private void listviewOnScroll() {
        lv_softmanager_applicaiton.setOnScrollListener(new AbsListView.OnScrollListener() {
            //滑动状态改变的时候调用
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            //滑动的时候调用
            //view:listview
            //firstVisibleItem:界面第一个显示的条目
            //visibleItemCount:显示条目的总个数
            //totalItemCount:条目的总个数
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //为null的原因:listview在初始化的时候就会调用onscroll方法,此时还没有加载数据
                if (userappinfo!=null&&systemappinfo!=null){
                    if(firstVisibleItem>= userappinfo.size()+1){
                        tv_softmanager_userorsystem.setText("系统程序("+systemappinfo.size()+")");
                    }else{
                        tv_softmanager_userorsystem.setText("用户程序("+userappinfo.size()+")");
                    }
                }

            }
        });
    }

    /**
     * 加载数据
     */
    private void fillData() {
        new MyAsynTaks() {

            @Override
            public void preTask() {
                loading.setVisibility(ListView.VISIBLE);
            }

            @Override
            public void doinBack() {
                list = AppEngine.getAppInfos(getApplicationContext());
                userappinfo = new ArrayList<AppInfo>();
                systemappinfo = new ArrayList<AppInfo>();
                for (AppInfo appInfo :
                        list) {
                    //将数据分别存放到用户程序集合和系统程序集合中
                    if (appInfo.isUser()) {
                        userappinfo.add(appInfo);
                    } else {
                        systemappinfo.add(appInfo);
                    }
                }
            }

            @Override
            public void postTask() {
                lv_softmanager_applicaiton.setAdapter(new MyAdapter());
                loading.setVisibility(ListView.INVISIBLE);
            }
        }.execute();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            //list.size()=userappinfo.size()+systemappinfo.size()
            return userappinfo.size() + systemappinfo.size()+2;//+2是两个条目
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(position==0){
                //添加用户程序(...个)textview
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("用户程序("+userappinfo.size()+")");
                return textView;
            }else if(position==userappinfo.size()+1){
                //添加系统程序(...个)textview
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("系统程序("+systemappinfo.size()+")");
                return textView;
            }


            View view;
            ViewHolder viewHolder;
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(), R.layout.item_softmanager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_itemsoftmanager_icon = (ImageView) view.findViewById(R.id.iv_itemsoftmanager_icon);
                viewHolder.tv_itemsoftmanager_name = (TextView) view.findViewById(R.id.tv_itemsoftmanager_name);
                viewHolder.tv_itemsoftmanager_issd = (TextView) view.findViewById(R.id.tv_itemsoftmanager_issd);
                viewHolder.tv_itemsoftmanager_version = (TextView) view.findViewById(R.id.tv_itemsoftmanager_version);
                //将viewholder和view对象绑定
                view.setTag(viewHolder);
            }
            //1、获取应用程序的信息
            AppInfo appInfo;
            //数据就要从userappinfo和systemappinfo中获取
            if (position <= userappinfo.size()) {
                //用户程序
                appInfo = userappinfo.get(position-1);
            } else {
                //系统程序
                appInfo = systemappinfo.get(position - userappinfo.size()-2);
            }

            //2、设置显示数据
            viewHolder.iv_itemsoftmanager_icon.setImageDrawable(appInfo.getIcon());
            viewHolder.tv_itemsoftmanager_name.setText(appInfo.getName());
            boolean sd = appInfo.isSD();
            if (sd) {
                viewHolder.tv_itemsoftmanager_issd.setText("SD卡");
            } else {
                viewHolder.tv_itemsoftmanager_issd.setText("手机内存");
            }
            viewHolder.tv_itemsoftmanager_version.setText(appInfo.getVersionName());

            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_itemsoftmanager_icon;
        TextView tv_itemsoftmanager_name, tv_itemsoftmanager_issd, tv_itemsoftmanager_version;
    }
}
