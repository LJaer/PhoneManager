package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by LJaer on 16/9/20.
 */
public class HomeActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载布局文件
        setContentView(R.layout.activity_home);
    }
}
