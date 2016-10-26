package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.BitmapFactory;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.IOUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import top.ljaer.www.phonemanager.service.AddressService;
import top.ljaer.www.phonemanager.utils.StreamUtil;

public class SplashActivity extends Activity {
    private static final int MSG_UPDATE_DIALO = 1;
    private static final int MSG_ENTER_HOME = 2;
    private static final int MSG_SERVER_ERROR = 3;
    private static final int MSG_URL_ERROR = 4;
    private static final int MSG_IO_ERROR = 5;
    private static final int MSG_JSON_ERROR = 6;
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
                case MSG_ENTER_HOME:
                    enterHome();
                    break;
                case MSG_SERVER_ERROR:
                    //连接失败,服务器出现异常
                    Toast.makeText(getApplicationContext(), "服务器异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_IO_ERROR:
                    Toast.makeText(getApplicationContext(), "亲,网络没有连接", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_URL_ERROR:
                    //方便后期定位异常
                    Toast.makeText(getApplicationContext(), "异常号:" + MSG_URL_ERROR, Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_JSON_ERROR:
                    Toast.makeText(getApplicationContext(), "异常号:" + MSG_JSON_ERROR, Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
            }
        }
    };
    private TextView tv_splash_plan;

    /**
     * 弹出对话框
     */
    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框不能消失
        builder.setCancelable(false);
        //设置对话框的标题
        builder.setTitle("新版本:" + code);
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
        HttpUtils httpUtils = new HttpUtils();
        //判断sd卡是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //url:新版本下载的路径apkurl
            //target:保存新版本的目录
            //callback:RequestCallBack
            httpUtils.download(apkurl, "/mnt/sdcard/phoneManage.apk", new RequestCallBack<File>() {
                //下载成功调用的方法
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //4、安装最新版本
                    installAPK();
                }

                //下载失败调用的方法
                @Override
                public void onFailure(HttpException e, String s) {

                }

                //显示当前下载进度
                //total:下载总进度
                //current:下载的当前进度
                //isUploading:是否支持断点续传
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    //设置显示下载进度的TextView可见,同时设置相应的下载进度
                    tv_splash_plan.setVisibility(View.VISIBLE);//设置控件是否可见
                    tv_splash_plan.setText(current + "/" + total);//100/200
                }
            });
        }
    }

    /**
     * 4、安装最新版本
     */
    private void installAPK() {

       /* <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <action android:name="android.intent.action.INSTALL_PACKAGE" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:scheme="file" />//file:从文件中获取数据
        <data android:scheme="content" />//content:从内容提供者中获取数据 content://
        <data android:mimeType="application/vnd.android.package-archive" />
        </intent-filter>*/
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setAction("android.intent.action.INSTALL_PACKAGE");
        intent.addCategory("android.intent.category.DEFAULT");
        //单独设置会相互覆盖
        /*intent.setData(Uri.fromFile(new File("/mnt/sdcard/phoneManage.apk")));
        intent.setType("application/vnd.android.package-archive");*/
        intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/phoneManage.apk")), "application/vnd.android.package-archive");
        //在当前activity退出的时候,会调用之前activity的onActivityResult方法
        //requestCode:请求码,用来标识从哪个activity跳转过来
        //ABC a→c b→c,c区分intent是从哪个activity传递过来的,这时候就要用到请求码
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    /**
     * 跳转到主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //移除splash界面
        finish();
    }

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        tv_splash_vesionname = (TextView) findViewById(R.id.tv_splash_versionname);
        tv_splash_vesionname.setText("版本号:" + getVersionName());
        tv_splash_plan = (TextView) findViewById(R.id.tv_splash_plan);
        if (sp.getBoolean("update", true)) {
            update();
        } else {
            //跳转到主界面
            //不能让主线程去睡两秒钟
            //主线程是有一个渲染界面的操作,主线程睡两秒钟,没有办法渲染界面
            new Thread() {
                public void run() {
                    SystemClock.sleep(2000);
                    enterHome();
                }
            }.start();
            //SystemClock.sleep(2000);
            //enterHome();
        }
        copyDb("address.db");
        copyDb("antivirus.db");
        //开启监听电话状态的服务
        /*Intent intent = new Intent(this, AddressService.class);
        startService(intent);*/
        shortcut();
    }

    /**
     * 创建快捷方式
     */
    private void shortcut() {
        if(sp.getBoolean("firstshortcut",true)){
            //给桌面发送一个广播
            Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            //设置属性
            //设置快捷方式名称
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"手机卫士75");
            //设置快捷方式的图标
            Bitmap value = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,value);
            //设置快捷方式执行的操作
            Intent intent2 = new Intent();
            intent2.setAction("top.ljaer.www.phonemanager.home");
            intent2.addCategory("android.intent.category.DEFAULT");
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,intent2);
            sendBroadcast(intent);

            //保存已经创建快捷方式的状态
            Editor edit =  sp.edit();
            edit.putBoolean("firstshortcut",false);
            edit.commit();
        }
    }

    /**
     * 拷贝数据库
     */
    private void copyDb(String dbname) {
        File file = new File(getFilesDir(), dbname);
        //判断文件是否存在
        if (!file.exists()) {
            //从assets目录中将数据库读取出来
            //1、获取assets的管理者
            AssetManager am = getAssets();
            InputStream in = null;
            FileOutputStream out = null;
            try {
                //2、读取数据库
                in = am.open(dbname);
                //写入流
                //getCacheDir:获取缓存的路径
                //getFilesDir:获取文件的路径
                out = new FileOutputStream(file);
                //3、读写操作
                //设置缓冲区
                byte[] b = new byte[1024];
                int len = -1;
                while ((len = in.read(b)) != -1) {
                    out.write(b, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            /*in.close();
            out.close();*/
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }
        }

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
                            message.what = MSG_ENTER_HOME;
                        } else {
                            //有最新版本
                            //2.弹出对话框,提醒用户更新版本
                            message.what = MSG_UPDATE_DIALO;
                        }
                    } else {
                        //连接失败
                        System.out.println("连接失败....");
                        message.what = MSG_SERVER_ERROR;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = MSG_URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = MSG_IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = MSG_JSON_ERROR;
                } finally {//不管有没有异常都会执行
                    //处理连接外网连接时间的问题
                    //在连接成功之后再去获取一个时间
                    int endTime = (int) System.currentTimeMillis();
                    //比较两个时间的时间差,如果小于两秒,睡两秒,大于两秒,不睡
                    int dTime = endTime - startTime;
                    if (dTime < 2000) {
                        //睡两秒钟
                        SystemClock.sleep(2000 - dTime);//始终都是睡两秒钟的时间
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
