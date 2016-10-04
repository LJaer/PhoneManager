package top.ljaer.www.phonemanager.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import top.ljaer.www.phonemanager.R;
import top.ljaer.www.phonemanager.service.GPSService;

/**
 * Created by LJaer on 16/10/3.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static MediaPlayer mediaPlayer;
    //广播接收者在每个接收到一个广播事件,重新new广播接收者
    @Override
    public void onReceive(Context context, Intent intent) {

        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context,Admin.class);

        //接收解析短信
        //70汉字一条短信,71条汉字两条短信
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj:
             objs) {
            //解析成SmsMessage
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])obj);
            String body = smsMessage.getMessageBody();//获取短信的内容
            String sender = smsMessage.getOriginatingAddress();//获取发件人
            System.out.println("发件人:"+sender+" 短信内容:"+body);
            //真机测试,加发件人判断
            //判断短信是哪个指令
            if("#*location*#".equals(body)){
                //GPS追踪
                System.out.println("GPS追踪");
                //开启一个服务
                Intent intent_gps = new Intent(context, GPSService.class);
                context.startService(intent_gps);
                //拦截短信
                abortBroadcast();//拦截操作,原生android系统中,国产深度定制系统中屏蔽,比如小米
            }else if ("#*alarm*#".equals(body)){
                //播放报警音乐
                //在播放报警音乐之前,将系统音量设置为最大
                //声音的管理者
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                //设置系统音量的大小
                //streamType:声音的类型
                //index:声音的大小 0最小,15最大
                //flags:指定信息的标签
                //getStreamMaxVolume:获取系统最大音量,streamtype:声音的类型
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);

                //判断是否在播放音乐
                if (mediaPlayer!=null){
                    mediaPlayer.release();//释放资源
                }

                mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
                //mediaPlayer.setVolume(1.0f,1.0f);//设置最大音量
                //mediaPlayer.setLooping(true);
                mediaPlayer.start();

                System.out.println("播放报警音乐");
                abortBroadcast();//拦截操作,原生android系统中,国产深度定制系统中屏蔽,比如小米
            }else if ("#*wipedata*#".equals(body)){
                //远程删除数据,类似于恢复出厂设置
                System.out.println("远程删除数据");
                if(devicePolicyManager.isAdminActive(componentName)){
                    devicePolicyManager.wipeData(0);//远程删除数据
                }
                abortBroadcast();//拦截操作,原生android系统中,国产深度定制系统中屏蔽,比如小米
            }else if ("#*lockscreen*#".equals(body)){
                //远程锁屏
                System.out.println("远程锁屏");
                //判断超级管理员是否激活
                if(devicePolicyManager.isAdminActive(componentName)){
                    devicePolicyManager.lockNow();
                }
                abortBroadcast();//拦截操作,原生android系统中,国产深度定制系统中屏蔽,比如小米
            }
        }
    }
}
