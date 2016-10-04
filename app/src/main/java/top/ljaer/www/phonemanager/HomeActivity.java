package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import top.ljaer.www.phonemanager.utils.MD5Util;

/**
 * Created by LJaer on 16/9/20.
 */
public class HomeActivity extends Activity {
    private GridView gv_home_gridview;
    private Dialog dialog;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载布局文件
        setContentView(R.layout.activity_home);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        gv_home_gridview = (GridView) findViewById(R.id.gv_home_gridview);
        gv_home_gridview.setAdapter(new MyAdapter());
        gv_home_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positon, long id) {
                //position:条目的位置 0-8
                //根据条目的位置判断用户点击哪个条目
                switch (positon) {
                    case 0://手机防盗
                        //跳转到手机防盗模块
                        //判断用户是一次点击的话,设置密码,设置成功,再次点击输入密码,密码正确才能进去手机防盗模块
                        //判断用户是否设置过密码
                        if (TextUtils.isEmpty(sp.getString("password", ""))) {
                            //设置密码
                            showSetPassWordDialog();
                        } else {
                            //输入密码
                            showEnterPasswordDialog();
                        }
                        break;
                    case 7://高级工具
                        Intent intent7 = new Intent(HomeActivity.this,AToolsActivity.class);
                        startActivity(intent7);
                        break;
                    case 8://设置中心
                        Intent intent8 = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent8);
                        break;
                }
            }
        });
    }

    int count = 0;

    /**
     * 输入密码对话框
     */
    private void showEnterPasswordDialog() {
        //第一步:复制布局
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框不能取消
        builder.setCancelable(false);
        //将布局文件转化成View对象
        View view = View.inflate(getApplicationContext(), R.layout.dialog_enterpassword, null);
        //第三步:复制初始化控件及功能实现
        final EditText et_setpassword_password = (EditText) view.findViewById(R.id.et_setpassword_password);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
        ImageView iv_enterpassword_hide = (ImageView) view.findViewById(R.id.iv_enterpassword_hide);
        iv_enterpassword_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐藏显示密码
                if (count % 2 == 0) {
                    //显示密码
                    et_setpassword_password.setInputType(0);
                } else {
                    //隐藏密码
                    et_setpassword_password.setInputType(129);  //代码设置输入框输入类型
                }
                count++;
            }
        });
        //设置确定,取消按钮的点击事件
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断密码输入是否正确
                //1、获取输入的密码
                String password = et_setpassword_password.getText().toString().trim();
                //2、判断密码是否为空
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //3、获取保存的密码
                String sp_password = sp.getString("password", "");
                //4、判断两个密码是否一致
                if (MD5Util.passwordMD5(password).equals(sp_password)) {
                    //跳转到手机防盗界面
                    Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                    startActivity(intent);
                    //隐藏对话框
                    dialog.dismiss();
                    //提醒用户
                    Toast.makeText(getApplicationContext(), "密码正确", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐藏对话框
                dialog.dismiss();
            }
        });
        //第二步:复制显示
        builder.setView(view);
        //显示对话框
        //builder.show();
        dialog = builder.create();
        dialog.show();
    }

    /**
     * 设置密码对话框
     */
    private void showSetPassWordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框不能取消
        builder.setCancelable(false);
        //将布局文件转化成View对象
        View view = View.inflate(getApplicationContext(), R.layout.dialog_setpassword, null);
        final EditText et_setpassword_password = (EditText) view.findViewById(R.id.et_setpassword_password);
        final EditText et_setpassword_confirm = (EditText) view.findViewById(R.id.et_setpassword_confirm);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
        //设置确定,取消按钮的点击事件
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置密码
                //1、获取密码输入框的输入内容
                String password = et_setpassword_password.getText().toString().trim();
                //2、判断密码是否为空
                if (TextUtils.isEmpty(password)) {//null://没有内存 "":有内存地址但是没有内容
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //3、获取确认密码
                String confirm_password = et_setpassword_confirm.getText().toString().trim();
                //4、判断两次密码是否一致
                if (password.equals(confirm_password)) {
                    //保存密码,sp
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("password", MD5Util.passwordMD5(password));
                    edit.commit();
                    //隐藏对话框
                    dialog.dismiss();
                    //提醒用户
                    Toast.makeText(getApplicationContext(), "密码设置成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐藏对话框
                dialog.dismiss();
            }
        });
        builder.setView(view);
        //显示对话框
        //builder.show();
        dialog = builder.create();
        dialog.show();
    }

    private class MyAdapter extends BaseAdapter {
        int[] imageId = {R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
                R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
                R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings};
        String[] names = {"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理",
                "高级工具", "设置中心"};

        //设置条目个数
        @Override
        public int getCount() {
            return 9;
        }

        //获取条目对应的数据
        @Override
        public Object getItem(int i) {
            return null;
        }

        //获取条目的id
        @Override
        public long getItemId(int i) {
            return 0;
        }

        //设置条目的样式
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TextView textView= new TextView(getApplicationContext());
            //textView.setText("第"+position+"个条目");//position:代表是条目的位置,从0开始 0-8
            //将布局文件转化成view对象
            View view = View.inflate(getApplicationContext(), R.layout.item_home, null);
            //每个条目的样式都不一样,初始化控件,去设置控件的值
            //view.findViewById:是从item_home布局中找控件,findViewById是从activity_home中找控件
            ImageView iv_itemhome_icon = (ImageView) view.findViewById(R.id.iv_itemhome_icon);
            TextView tv_itemhome_text = (TextView) view.findViewById(R.id.tv_itemhome_text);
            //设置控件的值
            iv_itemhome_icon.setImageResource(imageId[position]);//给imageView设置图片,根据条目位置从图片数组中获取相应的图片
            tv_itemhome_text.setText(names[position]);
            return view;
        }
    }
}
