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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by jaer on 2016/10/24.
 */

public class WatchDogActivity extends Activity {
    private ImageView iv_watchdog_icon;
    private TextView tv_watchdog_name;
    private String packagename;
    private EditText ed_watchdog_password;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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
            /**
             * Starting: Intent {
             act=android.intent.action.MAIN
             cat=[android.intent.category.HOME
             ] cmp=com.android.launcher/com.android.launcher2.Launcher } from pid 208
             */
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("WatchDog Page") // TODO: Define a title for the content shown.
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
}
