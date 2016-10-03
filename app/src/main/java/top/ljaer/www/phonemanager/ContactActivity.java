package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import top.ljaer.www.phonemanager.utils.MyAsynTaks;

/**
 * Created by LJaer on 16/10/2.
 */

public class ContactActivity extends Activity {
    @ViewInject(R.id.lv_contact_contacts)
    private ListView lv_contact_contacts;
    private List<HashMap<String, String>> list;
    //注解初始化控件,类似Spring,注解的形式生成一个javabean,内部:通过反射的方式执行了findViewById
    @ViewInject(R.id.loading)
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ViewUtils.inject(this);

        //异步加载框架
        new MyAsynTaks() {

            @Override
            public void preTask() {
                //在加载数据之前显示进度条,在子线程之前操作
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void doinBack() {
                //获取联系人,在子线程之中执行的操作
                list = ContactEngine.getAllContactInfo(getApplicationContext());
            }

            @Override
            public void postTask() {
                //在子线程之后执行操作
                lv_contact_contacts.setAdapter(new Myadapter());
                loading.setVisibility(View.INVISIBLE);//数据显示完成,隐藏进度条
            }
        }.execute();


        /*//参数:提高扩展性
        //参数1:在子线程执行所需的参数
        //参数2:执行的进度
        //参数3:子线程执行的结果
        //异步加载框架:面试必问,手写异步加载框架,百度面试题: 当他执行到多少个操作的时候就和new一个线程没区别了,5个
        new AsyncTask<String, Integer, String>() {
            //子线程之前执行的方法
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            //在子线程之中执行的方法
            @Override
            protected String doInBackground(String... params) {
                return null;
            }
            //在子线程之后执行的方法
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }
            //显示当前加载的进度
            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }
        };*/

        //lv_contact_contacts = (ListView) findViewById(R.id.lv_contact_contacts);
        lv_contact_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //将选择联系人的号码传递给设置安全号码界面
                Intent intent = new Intent();
                intent.putExtra("num", list.get(position).get("phone"));
                //将数据传递给设置安全号码界面
                //设置结果的方法,会将结果传递给调用当前activity的activity
                setResult(RESULT_OK, intent);
                //移除界面
                finish();
            }
        });
    }

    private class Myadapter extends BaseAdapter {
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
            View view = View.inflate(getApplicationContext(), R.layout.item_contact, null);
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
