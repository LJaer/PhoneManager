package top.ljaer.www.phonemanager.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import java.util.List;

/**
 * Created by jaer on 2016/10/21.
 */

public class WatchDogService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //时时刻刻监听用户打开的程序
        //activity都是存放在任务栈中的，一个应用只有一个任务栈
        final ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        new Thread(){
            @Override
            public void run() {
                while (true){
                    //监听操作
                    //监听用户打开了哪些任务栈,打开哪些应用
                    //获取正在运行的任务栈,如果任务栈运行,就表示应用打开过
                    //maxNum : 获取正在运行的任务栈的个数
                    //现在打开的应用的任务栈,永远在第一个,而之前(点击home最小化,没有退出)的应用的任务栈会依次往后推
                    List<RunningTaskInfo>  runningTasks = am.getRunningTasks(1);
                    System.out.println("----------------------");
                    for (RunningTaskInfo runningTaskInfo:runningTasks){
                        //获取任务栈，栈底的activity
                        ComponentName baseactivity = runningTaskInfo.baseActivity;
                        /*//获取任务栈栈顶的activity
                        runningTaskInfo.topActivity;*/
                        String packageName = baseactivity.getPackageName();
                        System.out.println(packageName);
                        if (packageName.equals("com.android.mms")){
                            Intent intent = new Intent(WatchDogService.this,MainActivity.class);
                            //因为activity保存在任务栈中,而服务,广播没有保存在任务栈中的,所以在服务,广播中跳转activity必须给activity设置任务栈
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                    System.out.println("----------------------");
                    SystemClock.sleep(3000);
                }
            }
        }.start();
    }
}













