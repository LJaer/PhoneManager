package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by LJaer on 16/9/24.
 */
public class LostFindActivity extends Activity{
    private SharedPreferences sp;
    private TextView tv_lostfind_safenum;
    private ImageView iv_lostfind_protected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        //分为两部分1、显示设置过的手机防盗功能,2、设置手机防盗功能
        //判断用户是否第一次进入手机防盗模块,是,跳转到设置向导界面,不是,跳转防盗功能显示界面
        if(sp.getBoolean("first",true)){
            //第一次进入,跳转到手机防盗设置向导界面
            Intent intent = new Intent(this,SetUp1Activity.class);
            startActivity(intent);
            //移除lostfindActivity
            finish();
        }else{
            //手机防盗显示界面
            setContentView(R.layout.activity_lostfind);
            tv_lostfind_safenum = (TextView) findViewById(R.id.tv_lostfind_safenum);
            iv_lostfind_protected = (ImageView) findViewById(R.id.iv_lostfind_protected);
            //根据保存的安全号码和防盗保护状态进行设置
            //设置安全号码
            tv_lostfind_safenum.setText(sp.getString("safenum",""));
            //设置防盗保护是否开启状态
            //获取保存的防盗保护状态
            boolean b = sp.getBoolean("protected",false);
            //根据获取防盗保护状态设置相应显示图片
            if(b){
                //开启防盗保护
                iv_lostfind_protected.setImageResource(R.drawable.lock);
            }else{
                //关闭防盗保护
                iv_lostfind_protected.setImageResource(R.drawable.unlock);
            }
        }
    }

    /**
     * 重新进入设置向导的点击事件
     * @param v
     */
    public void resetup(View v){
        Intent intent = new Intent(this,SetUp1Activity.class);
        startActivity(intent);
        finish();
    }
}
