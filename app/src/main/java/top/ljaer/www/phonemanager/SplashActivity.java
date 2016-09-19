package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends Activity {
    private TextView tv_splash_vesionname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_splash_vesionname = (TextView) findViewById(R.id.tv_splash_versionname);
        tv_splash_vesionname.setText("版本号:"+getVersionName());
    }

    /**
     * 获取当前应用程序的版本号
     * @return
     */
    private String getVersionName(){
        //包的管理者,获取清单文件中的所有信息
        PackageManager pm = getPackageManager();
        try {
            //根据报名获取清单文件的信息,其实就是返回一个保存有清单文件信息的javabean
            //packageName:应用程序的包名
            //flags:指定信息的标签,0:标识获取基础的信息,比如包名,版本号,要想获取权限等等信息,必须通过标签来指定,才能获取
            //GET_PERMISSIONS:标签的含义:除了获取基础信息之外,还会获取额外权限的信息
            //getPackageName():获取当前应用的包名
            PackageInfo packageInfo= pm.getPackageInfo(getPackageName(),0);
            //获取版本号名称
            String versionName= packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //包名找不到的异常
            e.printStackTrace();
        }
        return null;
    }
}
