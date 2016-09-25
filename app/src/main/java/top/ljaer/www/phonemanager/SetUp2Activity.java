package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by LJaer on 16/9/25.
 */
public class SetUp2Activity extends SetUpBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
    }

    @Override
    public void next_activity() {
        //跳转到第三个界面
        Intent intent = new Intent(this, SetUp3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_next,R.anim.setup_exit_next);
    }

    @Override
    public void pre_activity() {
        //跳转到第一个界面
        Intent intent = new Intent(this, SetUp1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.setup_enter_pre,R.anim.setup_exit_pre);
    }
}
