package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import top.ljaer.www.phonemanager.service.AddressService;
import top.ljaer.www.phonemanager.ui.SettingView;
import top.ljaer.www.phonemanager.utils.AddressUtils;

/**
 * Created by LJaer on 16/9/22.
 */
public class SettingActivity extends Activity {
    private SettingView sv_setting_update;
    private SharedPreferences sp;
    private SettingView sv_setting_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //name:保存信息的文件的名称
        //mode:权限
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
        sv_setting_address = (SettingView) findViewById(R.id.sv_setting_address);

        update();
    }

    //没有焦点的时候调用
    @Override
    protected void onPause() {
        super.onPause();
    }

    //获取焦点的时候操作
    @Override
    protected void onResume() {
        super.onResume();
    }

    //activity可见的时候调用
    @Override
    protected void onStart() {
        super.onStart();
        address();
    }

    //activity不可见的时候调用
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 显示号码归属地
     */
    public void address(){
        //回写操作
        //动态的获取服务是否开启
        if(AddressUtils.isRunningService("top.ljaer.www.phonemanager.service.AddressService",getApplicationContext())){
            //开启服务
            sv_setting_address.setChecked(true);
        }else{
            //关闭服务
            sv_setting_address.setChecked(false);
        }

        sv_setting_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,AddressService.class);
                //根据checkbox状态设置描述信息状态
                //isChecked:代表之前的状态
                if(sv_setting_address.isChecked()){
                    //关闭提示更新
                    stopService(intent);
                    //更新checkbox状态
                    sv_setting_address.setChecked(false);
                }else {
                    //打开提示更新
                    startService(intent);
                    sv_setting_address.setChecked(true);
                }
            }
        });
    }

    /**
     * 提示更新
     */
    public void update(){
        //初始化自定义控件中各个控件的值
        //sv_setting_update.setTitle("提示更新");
        //defValue:缺省的值
        if(sp.getBoolean("update",true)){
            //sv_setting_update.setDes("打开提示更新");
            sv_setting_update.setChecked(true);
        }else{
            //sv_setting_update.setDes("关闭提示更新");
            sv_setting_update.setChecked(false);
        }


        //设置自定义组合控件的点击事件
        //问题1:点击checkbox发现描述信息没有改变,原因:是因为checkbox天生是有点击事件和获取焦点事件,
        // 当点击checkbox,这个checkbox就会执行它的点击事件,而不会执行条目的点击事件
        //问题2:没有保存用户点击的操作
        sv_setting_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = sp.edit();
                //更改状态
                //根据checkbox现有的状态来改变checkbox的状态
                if (sv_setting_update.isChecked()) {
                    //关闭提示更新
                    sv_setting_update.setChecked(false);
                    //sv_setting_update.setDes("关闭提示更新");
                    //保存状态

                    edit.putBoolean("update", false);
                    //  edit.apply();//保存到文件中,但是仅限于9版本之上,9版本之下是保存到内存中的
                } else {
                    //打开提示更新
                    sv_setting_update.setChecked(true);
                    //sv_setting_update.setDes("打开提示更新");
                    //保存状态
                    edit.putBoolean("update", true);
                }
                edit.commit();
            }
        });
    }
}
