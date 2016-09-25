package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 设置手机防盗界面
 * Created by LJaer on 16/9/24.
 */
public class SetUp1Activity extends SetUpBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void next_activity() {
        //跳转到第二个界面
        Intent intent = new Intent(this,SetUp2Activity.class);
        startActivity(intent);
        finish();
        //执行平移动画
        //执行界面切换动画的操作,是在startActivity或者finish之后执行
        //enterAnim:新的界面进入的动画
        //exitAnim:旧的界面退出的动画
        overridePendingTransition(R.anim.setup_enter_next,R.anim.setup_exit_next);
    }

    @Override
    public void pre_activity() {

    }
}
