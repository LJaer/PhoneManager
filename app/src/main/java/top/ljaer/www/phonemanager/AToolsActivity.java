package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

/**
 * Created by LJaer on 16/10/4.
 */

public class AToolsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public void  queryaddress(View v){
        //跳转到查询号码归属地页面
        Intent intent = new Intent(this,AddressActivity.class);
        startActivity(intent);
    }

    /**
     * 备份短信
     * @param v
     */
    public void backupsms(View v){
        //读取短信
        //备份短信
    }

    /**
     * 还原短信
     * @param v
     */
    public void restoresms(View v){

    }
}
