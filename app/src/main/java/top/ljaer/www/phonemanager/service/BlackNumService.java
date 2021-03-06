package top.ljaer.www.phonemanager.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import top.ljaer.www.phonemanager.db.dao.BlackNumDao;


/**
 * Created by LJaer on 16/10/7.
 */

public class BlackNumService extends Service {

    private SmsReceiver smsReceiver;
    private BlackNumDao blackNumDao;
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener myPhoneStateListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //接收解析短信
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj :
                    objs) {
                //解析成SmsMessage
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String body = smsMessage.getMessageBody();//获取短信的内容
                String sender = smsMessage.getOriginatingAddress();//获取发件人
                //根据发件人号码,获取号码的拦截模式
                int mode = blackNumDao.queryBlackNumMode(sender);
                //判断是否是短信拦截或者是全部拦截
                if (mode == BlackNumDao.SMS || mode == BlackNumDao.ALL) {
                    abortBroadcast();//拦截广播事件,拦截短信操作
                }
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        blackNumDao = new BlackNumDao(getApplicationContext());

        //代码注册短信到来的广播接收者
        //1、创建广播接收者
        smsReceiver = new SmsReceiver();
        //2、设置监听的广播事件
        IntentFilter intentFilter = new IntentFilter();
        //广播接收者的最高优先级Integer.MAX_VALUE,优先级相同,代码注册的广播接收者先接收广播事件
        intentFilter.setPriority(1001);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //3、注册广播接收者
        registerReceiver(smsReceiver, intentFilter);

        //监听电话状态
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        //参数1:监听
        //参数2:监听的事件
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, final String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            //如果是响铃状态,检测拦截模式是否是电话拦截,是挂断
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                //获取拦截模式
                int mode = blackNumDao.queryBlackNumMode(incomingNumber);
                if (mode == BlackNumDao.CALL || mode == BlackNumDao.ALL) {
                    //挂断电话  1.5
                    endCall();
                    //删除通话记录
                    //1、获取内容解析者
                    final ContentResolver resolver = getContentResolver();
                    //2、获取内容提供者地址   call_log    call表的地址:calls
                    //3、获取执行操作路径
                    final Uri uri = Uri.parse("content://call_log/calls");
                    //4、删除操作
                    //resolver.delete(uri,"number=?",new String[]{incomingNumber});
                    //通过内容观察者观察内容提供者,如果变化就去执行删除操作
                    //notifyForDescendants:匹配规则,true:精确匹配,false:模糊匹配
                    resolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {
                        //内容提供者内容变化的时候调用
                        @Override
                        public void onChange(boolean selfChange) {
                            super.onChange(selfChange);
                            //删除通话记录
                            resolver.delete(uri,"number=?",new String[]{incomingNumber});
                            //注销内容观察者
                            resolver.unregisterContentObserver(this);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
        //取消监听
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    /**
     * 挂断电话
     */
    private void endCall() {

        try {
            //通过反射进行实现
            //1、通过类加载器加载相应类的class文件
            Class<?> loadClass = BlackNumService.class.getClassLoader().loadClass("android.os.ServiceManager");
            //2、获取类中相应的方法
            //name:方法名
            //parameterTypes:参数类型
            Method method = loadClass.getDeclaredMethod("getService", String.class);
            //3、执行方法,获取返回值
            //receiver:类的实例
            //args:具体的参数
            IBinder invoke = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
            //aidl远程库
            ITelephony iTelephony = ITelephony.Stub.asInterface(invoke);
            //挂断电话
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
