package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmanager);
        //初始化控件
        lv_softmanager_applicaiton = (ListView) findViewById(R.id.lv_softmanager_applicaiton);
        loading = (ProgressBar) findViewById(R.id.loading);
        //加载数据
        fillData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
            }

            @Override
            public void postTask() {
                lv_softmanager_applicaiton.setAdapter(new MyAdapter());
                loading.setVisibility(ListView.INVISIBLE);
            }
        }.execute();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SoftManager Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
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
            View view;
            ViewHolder viewHolder;
            if (convertView != null) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(), R.layout.item_softmanager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_itemsoftmanager_icon= (ImageView) view.findViewById(R.id.iv_itemsoftmanager_icon);
                viewHolder.tv_itemsoftmanager_name= (TextView) view.findViewById(R.id.tv_itemsoftmanager_name);
                viewHolder.tv_itemsoftmanager_issd= (TextView) view.findViewById(R.id.tv_itemsoftmanager_issd);
                viewHolder.tv_itemsoftmanager_version= (TextView) view.findViewById(R.id.tv_itemsoftmanager_version);
                //将viewholder和view对象绑定
                view.setTag(viewHolder);
            }
            //1、获取应用程序的信息
            AppInfo appInfo = list.get(position);
            //2、设置显示数据
            viewHolder.iv_itemsoftmanager_icon.setImageDrawable(appInfo.getIcon());
            viewHolder.tv_itemsoftmanager_name.setText(appInfo.getName());
            boolean sd = appInfo.isSD();
            if(sd){
                viewHolder.tv_itemsoftmanager_issd.setText("SD卡");
            }else{
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
