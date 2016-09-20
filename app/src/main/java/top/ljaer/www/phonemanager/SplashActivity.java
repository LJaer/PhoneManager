package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import top.ljaer.www.phonemanager.utils.StreamUtil;

public class SplashActivity extends Activity {
    private static final int MSG_UPDATE_DIALO = 1;
    private TextView tv_splash_vesionname;
    private String code;
    private String apkurl;
    private String des;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_DIALO:
                    //弹出对话框
                    showdialog();
                    break;
            }
        }
    };

    /**
     * 弹出对话框
     */
    private void showdialog() {
        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        //设置对话框不能消失
        builder.setCancelable(false);
        //设置对话框的标题
        builder.setTitle("新版本:"+code);
        //设置对话框的图标
        builder.setIcon(R.drawable.ic_launcher);
        //设置对话框的描述信息
        builder.setMessage(des);
        //设置升级取消按钮
        builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //下载最新版本
                download();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //1、隐藏对话框
                dialog.dismiss();
                //2、跳转到主界面
                enterHome();
            }
        });
        //显示对话框
       // builder.create().show();//两种方式效果一样
        builder.show();
    }

    /**
     * 3、下载最新版本
     */
    private void download() {
    }

    /**
     * 跳转到主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        //移除splash界面
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_splash_vesionname = (TextView) findViewById(R.id.tv_splash_versionname);
        tv_splash_vesionname.setText("版本号:" + getVersionName());
        update();
    }

    /**
     * 提醒用户更新版本
     *
     * @param
     */
    private void update() {
        //1、连接服务器,查看是否有最新版本,联网操作,耗时操作,4.0以后不允许在主线程中执行,放到子线程中执行
        new Thread() {
            private int startTime;
            public void run() {
                Message message = Message.obtain();
                //在连接之前获取一个时间
                startTime = (int) System.currentTimeMillis();
                try {
                    //1.1连接服务器
                    //1.1.1设置链接路径
                    //spec:连接路径
                    URL url = new URL("http://192.168.49.130:8080/updateinfo.html");
                    //1.1.2获取连接操作
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();//http协议,或者httpClient
                    //1.1.3设置超时时间
                    conn.setConnectTimeout(5000);//设置连接超时时间
                    //conn.setReadTimeout(5000);//设置读取超时时间
                    //1.1.4设置请求方式
                    conn.setRequestMethod("GET");
                    //1.1.5获取服务器返回的状态码,200,404,500
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        //连接成功,获取服务器返回的数据,code:新版本的版本号,apkurl:新版本下载路径,des:描述信息,告诉用户增加了哪些功能,修改哪些bug
                        //获取数据之前,服务器是如何封装数据
                        System.out.println("连接成功");
                        //获取服务器返回得信息
                        InputStream in = conn.getInputStream();
                        //将获取到的流信息转化成字符串
                        String json = StreamUtil.parserStreamUtil(in);
                        //解析json数据
                        JSONObject jsonObject = new JSONObject(json);
                        //获取数据
                        code = jsonObject.getString("code");
                        apkurl = jsonObject.getString("apkurl");
                        des = jsonObject.getString("des");
                        System.out.println("code:" + code + ",apkurl:" + apkurl + ",des:" + des);
                        //1.2查看是否有最新版本
                        //判断服务器返回的新版本版本号和当前程序的版本号是否一致,一致表示没有最新版本,不一致表示有最新版本
                        if (code.equals(getVersionName())) {
                            //没有最新版本
                        } else {
                            //有最新版本
                            //2.弹出对话框,提醒用户更新版本
                            message.what = MSG_UPDATE_DIALO;
                        }
                    } else {
                        //连接失败
                        System.out.println("连接失败");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {//不管有没有异常都会执行
                    //处理连接外网连接时间的问题
                    //在连接成功之后再去获取一个时间
                     int endTime = (int) System.currentTimeMillis();
                    //比较两个时间的时间差,如果小于两秒,睡两秒,大于两秒,不睡
                    int dTime = endTime - startTime;
                    if(dTime<2000){
                        //睡两秒钟
                        SystemClock.sleep(2000-dTime);//始终都是睡两秒钟的时间
                    }
                    handler.sendMessage(message);
                }
            }
        }.start();

    }

    /**
     * 获取当前应用程序的版本号
     *
     * @return
     */
    private String getVersionName() {
        //包的管理者,获取清单文件中的所有信息
        PackageManager pm = getPackageManager();
        try {
            //根据报名获取清单文件的信息,其实就是返回一个保存有清单文件信息的javabean
            //packageName:应用程序的包名
            //flags:指定信息的标签,0:标识获取基础的信息,比如包名,版本号,要想获取权限等等信息,必须通过标签来指定,才能获取
            //GET_PERMISSIONS:标签的含义:除了获取基础信息之外,还会获取额外权限的信息
            //getPackageName():获取当前应用的包名
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //获取版本号名称
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //包名找不到的异常
            e.printStackTrace();
        }
        return null;
    }
}
