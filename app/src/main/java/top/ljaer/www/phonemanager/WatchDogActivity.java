package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jaer on 2016/10/24.
 */

public class WatchDogActivity extends Activity {
    private ImageView iv_watchdog_icon;
    private TextView tv_watchdog_name;
    private String packagename;
    private EditText ed_watchdog_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchdog);
        iv_watchdog_icon = (ImageView) findViewById(R.id.iv_watchdog_icon);
        tv_watchdog_name = (TextView) findViewById(R.id.tv_watchdog_name);
        ed_watchdog_password = (EditText) findViewById(R.id.ed_watchdog_password);

        //接收获取数据
        Intent intent = getIntent();
        packagename = intent.getStringExtra("packageName");
        //设置显示加锁的应用程序的图片和名称
        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packagename,0);
            Drawable icon = applicationInfo.loadIcon(pm);
            String name = applicationInfo.loadLabel(pm).toString();
            //设置显示
            iv_watchdog_icon.setImageDrawable(icon);
            tv_watchdog_name.setText(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.out.println("返回键");
            /**
             * Starting: Intent {
             act=android.intent.action.MAIN
             cat=[android.intent.category.HOME
             ] cmp=com.android.launcher/com.android.launcher2.Launcher } from pid 208*/

            //跳转到主界面
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



    public void lock(View v) {
        //解锁
        //1.获取输入的密码
        String password = ed_watchdog_password.getText().toString().trim();
        //2.判断密码是否输入正确
        if ("123".equals(password)) {
            //解锁
            //一般通过广播的形式将信息发送给服务
            Intent intent = new Intent();
            intent.setAction("top.ljaer.www.phonemanager.unlock");//自定义发送广播事件
            intent.putExtra("packagename", packagename);
            sendBroadcast(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
        }
    }




}
