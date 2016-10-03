package top.ljaer.www.phonemanager.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import org.w3c.dom.Text;

/**
 * Created by LJaer on 16/9/25.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        System.out.println("手机重启了。。。。。");
        //根据保存用户是否开启防盗保护状态,来设置是否发送报警短信
        if (sp.getBoolean("protected", false)) {
            //检查SIM卡是否发生变化
            //1、获取保存的SIM卡号
            String sp_sim = sp.getString("SIM", "");
            //2、再次获取本地SIM卡号
            TelephonyManager tel = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            //  tel.getLine1Number();//获取SIM卡绑定的电话号码 line1:双卡双待,在中国不太试用,运营商一般不会讲SIM卡和手机号码绑定
            String sim = tel.getSimSerialNumber();//获取SIM卡序列号,唯一标识
            //3、判断两个sim卡号是否为空
            if (TextUtils.isEmpty(sp_sim) && TextUtils.isEmpty(sim)) {
                //4、判断两个SIM卡是否一致,如果一致就不发送报警短信,不一致发送报警短信
                if (!sp_sim.equals(sim)) {
                    //发送报警短信
                    SmsManager smsManager = SmsManager.getDefault();
                    //destinationAddress:收件人
                    //scAddress:短信中心地址,一般为null
                    //text:短信内容
                    //sentIntent:是否发送成功
                    //deliveryIntent:短信协议 一般null
                    smsManager.sendTextMessage(sp.getString("safenum","5554"), null, "大哥我被盗了,帮我!", null, null);
                }
            }
        }
    }
}
