package top.ljaer.www.phonemanager.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import top.ljaer.www.phonemanager.R;

/**
 * Created by LJaer on 16/9/22.
 */
public class SettingClickView extends RelativeLayout {
    TextView tv_setting_title;
    TextView tv_setting_des;
    //在代码中使用的时候调用
    public SettingClickView(Context context) {
        super(context);
        //可以在初始化自定义控件的时候就添加控件
        init();
    }

    //在布局文件中使用的时候调用
    //AttributeSet:保存有控件的所有属性
    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //在布局文件中使用的时候调用,比两个参数多了个样式
    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 添加控件
     */
    private void init() {
        //添加布局控件
        //第一种方式
        //将布局文件转化成view对象
        //View view = View.inflate(getContext(),R.layout.settingview,null);//爹有了,去找孩子,亲生的
        //添加操作
        //this.addView(view);
        //第二种方式
        //获取view对象,同时给view对象设置父控件,相当于先创建一个view对象,再把控件放到自定义控件中
        View view = View.inflate(getContext(), R.layout.settingclickview, this);//孩子有了,去找爹,喜当爹
        //初始化标题描述信息,checkbox控件
        //view.findViewById:表示从settingview布局文件中获取控件
        tv_setting_title = (TextView) view.findViewById(R.id.tv_setting_title);
        tv_setting_des = (TextView) view.findViewById(R.id.tv_setting_des);
    }
    //需要添加一些方法,使程序员能方便的去改变自定义控件中的控件的值

    /**
     * 设置标题的方法
     */
    public void setTitle(String title) {
        tv_setting_title.setText(title);
    }

    /**
     * 设置描述信息的方法
     */
    public void setDes(String des) {
        tv_setting_des.setText(des);
    }


}
