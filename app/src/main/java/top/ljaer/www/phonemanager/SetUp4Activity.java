package top.ljaer.www.phonemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by LJaer on 16/9/25.
 */
public class SetUp4Activity extends SetUpBaseActivity {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        sp = getSharedPreferences("config", MODE_PRIVATE);
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
