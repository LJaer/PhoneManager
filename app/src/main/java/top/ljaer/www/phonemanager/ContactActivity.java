package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;

import top.ljaer.www.phonemanager.engine.ContactEngine;

/**
 * Created by LJaer on 16/10/2.
 */

public class ContactActivity extends Activity {
    @ViewInject(R.id.lv_contact_contacts)
    private ListView lv_contact_contacts;
    private List<HashMap<String,String>> list;
    //注解初始化控件,类似Spring,注解的形式生成一个javabean,内部:通过反射的方式执行了findViewById
    @ViewInject(R.id.loading)
    private ProgressBar loading;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            lv_contact_contacts.setAdapter(new Myadapter());
            loading.setVisibility(View.INVISIBLE);//数据显示完成,隐藏进度条
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ViewUtils.inject(this);

        //在加载数据之前显示进度条
        loading.setVisibility(View.VISIBLE);

        new Thread(){
            @Override
            public void run() {
                //获取联系人
                list = ContactEngine.getAllContactInfo(getApplicationContext());
                //获取完联系人给handler发送一个消息,在handler中去setadapter
                handler.sendEmptyMessage(0);
            }
        }.start();
        //lv_contact_contacts = (ListView) findViewById(R.id.lv_contact_contacts);
        lv_contact_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //将选择联系人的号码传递给设置安全号码界面
                Intent intent = new Intent();
                intent.putExtra("num",list.get(position).get("phone"));
                //将数据传递给设置安全号码界面
                //设置结果的方法,会将结果传递给调用当前activity的activity
                setResult(RESULT_OK,intent);
                //移除界面
                finish();
            }
        });
    }

    private class Myadapter extends BaseAdapter{
        //获取条目的个数
        @Override
        public int getCount() {
            return list.size();
        }
        //获取条目对应的数据
        @Override
        public Object getItem(int position) {
            return null;
        }
        //获取条目的id
        @Override
        public long getItemId(int position) {
            return 0;
        }
        //设置条目的样式
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),R.layout.item_contact,null);
            //初始化控件
            //view.findViewById是从item_contact中找控件,findViewById是从activity_contacts中找控件
            TextView tv_itemcontact_name = (TextView) view.findViewById(R.id.tv_itemcontact_name);
            TextView tv_itemcontact_phone = (TextView) view.findViewById(R.id.tv_itemcontact_phone);
            //设置控件的值
            tv_itemcontact_name.setText(list.get(position).get("name"));//根据条目的位置从list集合中获取相应的数据
            tv_itemcontact_phone.setText(list.get(position).get("phone"));
            return view;
        }
    }
}
