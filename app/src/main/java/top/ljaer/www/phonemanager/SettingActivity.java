package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import top.ljaer.www.phonemanager.service.AddressService;
import top.ljaer.www.phonemanager.service.BlackNumService;
import top.ljaer.www.phonemanager.ui.SettingClickView;
import top.ljaer.www.phonemanager.ui.SettingView;
import top.ljaer.www.phonemanager.utils.AddressUtils;

/**
 * Created by LJaer on 16/9/22.
 */
public class SettingActivity extends Activity {
    private SettingView sv_setting_update;
    private SharedPreferences sp;
    private SettingView sv_setting_address;
    private SettingClickView scv_setting_changebg;
    private SettingClickView scv_setting_location;
    private SettingView sv_setting_blacknum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //name:保存信息的文件的名称
        //mode:权限
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sv_setting_update = (SettingView) findViewById(R.id.sv_setting_update);
        sv_setting_address = (SettingView) findViewById(R.id.sv_setting_address);
        scv_setting_changebg = (SettingClickView) findViewById(R.id.scv_setting_changebg);
        scv_setting_location = (SettingClickView) findViewById(R.id.scv_setting_location);
        sv_setting_blacknum = (SettingView) findViewById(R.id.sv_setting_blacknum);

        update();
        changedbg();
        location();
    }

    /**
     * 归属地提示框位置
     */
    private void location() {
        scv_setting_location.setTitle("归属地提示框位置");
        scv_setting_location.setDes("设置归属地提示框的显示位置");
        scv_setting_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到设置位置的界面
                Intent intent = new Intent(SettingActivity.this,DragViewActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置归属地提示框风格
     */
    private void changedbg() {
        final String[] items={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
        //设置标题和描述信息
        scv_setting_changebg.setTitle("归属地提示框风格");
        //根据保存的选中的选项的索引值设置自定义组合控件描述信息的回显操作
        scv_setting_changebg.setDes(items[sp.getInt("which",0)]);
        //设置自定义组合控件的点击事件
        scv_setting_changebg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出单选对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                //设置图标
                builder.setIcon(R.drawable.ic_launcher);
                //设置标题
                builder.setTitle("归属地提示框风格");
                //设置单选框
                //items:选项的文本的数组
                //checkItem:选中的选项
                //listener:点击事件
                //设置单选框选中选项的回显
                builder.setSingleChoiceItems(items, sp.getInt("which",0), new DialogInterface.OnClickListener() {
                    //which:选中的选项的索引值
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("which",which);
                        edit.commit();
                        //1、设置自定义组合控件描述信息文本
                        scv_setting_changebg.setDes(items[which]);//根据选中选项的索引值从items数组中获取出相应文本,设置给描述信息控件
                        //2、隐藏对话框
                        dialog.dismiss();
                    }
                });

                //设置取消按钮
                builder.setNegativeButton("取消",null);//当点击按钮只是需要进行隐藏对话框的操作的话,参数2可以写为null,表示隐藏对话框
                builder.show();
            }
        });
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
        blackNum();
    }



    //activity不可见的时候调用
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 黑名单拦截操作
     */
    private void blackNum() {
        //回写操作
        //动态的获取服务是否开启
        if(AddressUtils.isRunningService("top.ljaer.www.phonemanager.service.BlackNumService",getApplicationContext())){
            //开启服务
            sv_setting_blacknum.setChecked(true);
        }else{
            //关闭服务
            sv_setting_blacknum.setChecked(false);
        }

        sv_setting_blacknum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,BlackNumService.class);
                //根据checkbox状态设置描述信息状态
                //isChecked:代表之前的状态
                if(sv_setting_blacknum.isChecked()){
                    //关闭提示更新
                    stopService(intent);
                    //更新checkbox状态
                    sv_setting_blacknum.setChecked(false);
                }else {
                    //打开提示更新
                    startService(intent);
                    sv_setting_blacknum.setChecked(true);
                }
            }
        });
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
