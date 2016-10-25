package top.ljaer.www.phonemanager.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by LJaer on 16/10/25.
 */

public class SMSEngine {

    /**
     * 获取短信
     */
    public static void getAllSMS(Context context){
        //1.获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        //2.获取内容提供者地址   sms,sms表的地址:null  不写
        //3.获取查询路径
        Uri uri = Uri.parse("content://sms");
        //4.查询操作
        //projection : 查询的字段
        //selection : 查询的条件
        //selectionArgs : 查询条件的参数
        //sortOrder : 排序
        Cursor cursor = resolver.query(uri, new String[]{"address","date","type","body"}, null, null, null);
        //5.解析cursor
        while(cursor.moveToNext()){
            String address = cursor.getString(0);
            String date = cursor.getString(1);
            String type = cursor.getString(2);
            String body = cursor.getString(3);
            System.out.println("address:"+address+"   date:"+date+"  type:"+type+"  body:"+body);
        }
    }

}
