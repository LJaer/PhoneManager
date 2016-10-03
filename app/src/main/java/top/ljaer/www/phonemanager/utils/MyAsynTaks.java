package top.ljaer.www.phonemanager.utils;


import android.os.Handler;
import android.os.Message;

/**
 * Created by LJaer on 16/10/3.
 */

public abstract class MyAsynTaks {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            postTask();
        }
    };
    /**
     * 在子线程之前执行的方法
     */
    public abstract void preTask();

    /**
     * 在子线程之中执行的方法
     */
    public abstract void doinBack();

    /**
     * 在子线程之后执行的方法
     */
    public abstract void postTask();
    /**
     * 执行
     */
    public void execute(){
        preTask();
        new Thread(){
            @Override
            public void run() {
                doinBack();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}

