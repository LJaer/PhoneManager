package top.ljaer.www.phonemanager.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;


import top.ljaer.www.phonemanager.db.dao.AddressDao;

/**
 * Created by LJaer on 16/10/5.
 */

public class AddressService extends Service {
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener myPhoneStateListener;
    private TextView textView;
    private WindowManager windowManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //监听电话状态
        //1、获取电话的管理者
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //2、监听电话的状态
        myPhoneStateListener = new MyPhoneStateListener();
        //listener:电话状态的回调监听
        //events:监听电话的事件
        //LISTEN_NONE:不做任何监听操作
        //LISTEN_CALL_STATE:监听电话状态
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        //监听电话状态的回调方法
        //state:电话的状态
        //incomingNumber:来电的状态
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://空闲状态,挂断状态
                    //隐藏toast
                    hideToast();
                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃状态
                    //查询号码归属地并显示
                    String queryAddress = AddressDao.queryAddress(incomingNumber, getApplicationContext());
                    //判断查询归属地是否为空
                    if (!TextUtils.isEmpty(queryAddress)) {
                        //显示号码归属地
                        //Toast.makeText(getApplicationContext(), queryAddress, Toast.LENGTH_SHORT).show();
                        showToast(queryAddress);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    @Override
    public void onDestroy() {
        //当服务关闭的时候取消监听
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }

    /**
     * 显示toast
     */
    public void showToast(String queryAddress) {
        //1、获取windowmanager
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        textView = new TextView(getApplicationContext());
        textView.setText(queryAddress);
        textView.setTextSize(20);
        textView.setTextColor(Color.RED);

        //3、设置toast的属性
        //LayoutParams是toast的属性,控件要添加到哪个父控件中,父控件就要使用哪个父控件的属性,表示控件的属性规则符合父控件的属性规则
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;//高度包裹内容
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;//宽度包裹内容
        params.format = PixelFormat.TRANSLUCENT;//透明
        params.type = WindowManager.LayoutParams.TYPE_TOAST;//指明toast的类型
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON//保持当前屏幕
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE //没有焦点
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;//不可触摸

        //2、将view对象添加到windowManager中
        //params:layoutparams   控件的属性
        //将params属性设置给view对象,并添加到windowManager中
        windowManager.addView(textView, params);
    }

    /**
     * 隐藏toast
     */
    public  void hideToast(){
        if(windowManager!=null && textView!=null){
            windowManager.removeView(textView);//移除控件
            windowManager = null;
            textView = null;
        }
    }
}
