package top.ljaer.www.phonemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.content.SharedPreferences.Editor;

/**
 * Created by LJaer on 16/9/25.
 */
public class SetUp4Activity extends SetUpBaseActivity {
    private CheckBox cb_setup4_protected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        cb_setup4_protected = (CheckBox) findViewById(R.id.cb_setup4_protected);
        //根据保存的用户状态进行回显操作
        if(sp.getBoolean("protected",false)){
            //开启了防盗保护
            cb_setup4_protected.setText("您已经开启了防盗保护");
            cb_setup4_protected.setChecked(true);//必须要写
        }else{
            //关闭防盗保护
            cb_setup4_protected.setText("您还没有开启防盗保护");
            cb_setup4_protected.setChecked(false);//必须要写
        }

        //给checkbox设置点击事件
        //当checkbox状态改变的时候调用
        cb_setup4_protected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //CompoundButton:checkbox
            //isChecked:改变之后的值,点击之后的值
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Editor editor = sp.edit();
                //根据checkbox的状态去设置checkbox的信息
                if(isChecked){
                    //开始防盗保护
                    cb_setup4_protected.setText("您已经开启了防盗保护");
                    cb_setup4_protected.setChecked(true);//程序严谨性
                    //保存用户选择的状态
                    editor.putBoolean("protected",true);
                }else {
                    //关闭防盗保护
                    cb_setup4_protected.setText("您还没有开启防盗保护");
                    cb_setup4_protected.setChecked(false);//程序严谨性
                    editor.putBoolean("protected",false);
                }
                editor.commit();
            }
        });
    }

    @Override
    public void next_activity() {
        //保存一下用户第一次进入手机防盗模块设置向导的状态,first
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("first",false);
        edit.commit();
        //跳转到手机防盗界面
        Intent intent = new Intent(this, LostFindActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void pre_activity() {
        //跳转到第三个界面
        Intent intent = new Intent(this, SetUp3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre, R.anim.setup_exit_pre);
    }
}
