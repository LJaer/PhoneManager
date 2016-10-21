package top.ljaer.www.phonemanager.db.dao;

import android.content.Context;

import top.ljaer.www.phonemanager.db.WatchDogOpenHelper;

/**
 * Created by jaer on 2016/10/21.
 */

public class WatchDogDao {
    private WatchDogOpenHelper watchDogOpenHelper;
    private byte[] b = new byte[1024];
    //在构造函数中获取WatchDogOpenHelper
    public WatchDogDao(Context context){
        watchDogOpenHelper = new WatchDogOpenHelper(context);
    }

    //增删改查
    //面试:我在同一时刻对数据库既进行读操作也进行写操作,怎么避免这个两个操作同时操作数据库,同步锁+将WatchDogOpenHelper设置成单例模式

    /**
     * 添加应用程序包名
     * @param packagename
     */
    public void addLockApp(String packagename){

    }
}
