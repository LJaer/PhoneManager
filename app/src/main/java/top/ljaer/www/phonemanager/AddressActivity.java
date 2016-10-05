package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import top.ljaer.www.phonemanager.db.dao.AddressDao;

/**
 * Created by LJaer on 16/10/4.
 */

public class AddressActivity extends Activity {
    EditText et_address_queryphone;
    TextView tv_address_queryaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        et_address_queryphone = (EditText) findViewById(R.id.et_address_queryphone);
        tv_address_queryaddress = (TextView) findViewById(R.id.tv_address_queryaddress);

        //监听输入框文本变化的操作
        et_address_queryphone.addTextChangedListener(new TextWatcher() {
            //当文本变化之前调用
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //当文本变化完成的时候调用
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //1、获取输入框输入的内容
                String phone = s.toString();
                //2、根据号码查询号码归属地
                String queryAddress = AddressDao.queryAddress(phone, getApplicationContext());
                //3、判断查询的号码归属地是否为空
                if (!TextUtils.isEmpty(queryAddress)) {
                    //将查询的号码归属地设置给textView显示
                    tv_address_queryaddress.setText(queryAddress);
                }
            }

            //文本变化之后
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 查询号码归属地操作
     *
     * @param v
     */
    public void query(View v) {
        //1、获取输入的号码
        String phone = et_address_queryphone.getText().toString().trim();
        //2、判断号码是否为空
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "请输入要查询的号码", Toast.LENGTH_SHORT).show();
            //实现抖动的效果
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            et_address_queryphone.startAnimation(shake);//开启动画
            //振动
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(100);//振动100毫秒,0.1s
        } else {
            //3、根据号码查询号码归属地
            String queryAddress = AddressDao.queryAddress(phone, getApplicationContext());
            //4、判断查询的号码归属地是否为空
            if (!TextUtils.isEmpty(queryAddress)) {
                tv_address_queryaddress.setText(queryAddress);
            }
        }
    }
}
