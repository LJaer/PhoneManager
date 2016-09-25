package top.ljaer.www.phonemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import org.w3c.dom.Text;

import top.ljaer.www.phonemanager.ui.SettingView;

/**
 * Created by LJaer on 16/9/25.
 */
public class SetUp2Activity extends SetUpBaseActivity {
    private SettingView sv_setup2_sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        sv_setup2_sim = (SettingView) findViewById(R.id.sv_setup2_sim);
        //设置回写操作
        //根据保存的SIM卡去初始化控件的状态,有保存SIM卡号就是绑定SIM卡,如果没有就是没有绑定SIM卡
        if(TextUtils.isEmpty(sp.getString("SIM",""))){
            //没有绑定
            sv_setup2_sim.setChecked(false);
        }else{
            //绑定SIM卡
            sv_setup2_sim.setChecked(true);
        }
        sv_setup2_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sp.edit();
                //绑定sim卡
                //根据checkbox的状态设置描述信息的状态
                //isChecked:获取之前checkbox的状态
                if (sv_setup2_sim.isChecked()) {
                    //解绑操作
                    edit.putString("SIM", "");
                    sv_setup2_sim.setChecked(false);
                } else {
                    //绑定操作
                    //电话的管理者
                    TelephonyManager tel = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    //  tel.getLine1Number();//获取SIM卡绑定的电话号码 line1:双卡双待,在中国不太试用,运营商一般不会讲SIM卡和手机号码绑定
                    String sim = tel.getSimSerialNumber();//获取SIM卡序列号,唯一标识
                    //保存SIM卡号
                    edit.putString("SIM", sim);
                    sv_setup2_sim.setChecked(true);
                }
                edit.commit();
            }
        });
    }

    @Override
    public void next_activity() {
        //判断用户是否绑定SIM卡,绑定跳转到第三个界面,没有绑定提醒用户必须绑定
        if(sv_setup2_sim.isChecked()){
            //跳转到第三个界面
            Intent intent = new Intent(this, SetUp3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.setup_enter_next, R.anim.setup_exit_next);
        }else{
            Toast.makeText(getApplicationContext(),"请绑定SIM卡",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void pre_activity() {
        //跳转到第一个界面
        Intent intent = new Intent(this, SetUp1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }
}
