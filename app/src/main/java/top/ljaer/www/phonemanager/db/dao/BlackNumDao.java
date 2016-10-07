package top.ljaer.www.phonemanager.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import top.ljaer.www.phonemanager.bean.BlackNumInfo;
import top.ljaer.www.phonemanager.db.BlackNumOpenHelper;

/**
 * Created by LJaer on 16/10/6.
 */

public class BlackNumDao {
    public static final int CALL = 0;
    public static final int SMS = 1;
    public static final int ALL = 2;

    private final BlackNumOpenHelper blackNumOpenHelper;

    //在构造函数中获取BlackNumOpenHelper
    public BlackNumDao(Context context){
        blackNumOpenHelper = new BlackNumOpenHelper(context);
    }
    //增删改查

    /**
     * 添加黑名单
     * @param blacknum
     * @param mode
     */
    public void addBlackNum(String blacknum,int mode){
        //1、获取数据库
        SQLiteDatabase database =  blackNumOpenHelper.getReadableDatabase();
        //2、添加操作
        //ContentValues:添加的数据
        ContentValues values = new ContentValues();
        values.put("blacknum",blacknum);
        values.put("mode",mode);
        database.insert(blackNumOpenHelper.DB_NAME,null,values);
        //3、关闭数据库
        database.close();
    }

    /**
     * 更新黑名单的拦截模式
     */
    public void updateBlackNum(String blacknum,int mode){
        //1、获取数据库
        SQLiteDatabase database = blackNumOpenHelper.getReadableDatabase();
        //2、更新操作
        ContentValues values = new ContentValues();
        values.put("mode",mode);
        //table:表明
        //values:要更新数据
        //whereClause:查询条件  where blacknum = xxxx
        //whereArgs:查询条件的参数
        database.update(BlackNumOpenHelper.DB_NAME,values,"blacknum=?",new String[]{blacknum});
        //关闭数据库
        database.close();
    }

    /**
     * 通过黑名单号码,查询黑名单的拦截模式
     */
    public int queryBlackNumMode(String blacknum){
        int mode = 1;
        //1、获取数据库
        SQLiteDatabase database = blackNumOpenHelper.getReadableDatabase();
        //2、查询数据库
        //table:表名
        //columns:查询的字段 mode
        //selection:查询条件 where  xxxx = xxxx
        //selectionArgs:查询条件参数
        //groupBy:分组
        //having:去重
        //orderBy:排序
        Cursor cursor = database.query(BlackNumOpenHelper.DB_NAME,new String[]{"mode"},"blacknum=?",new String[]{blacknum},null,null,null);
        //3、解析cursor
        if (cursor.moveToNext()){
            //获取查询出来的数据
            mode =  cursor.getInt(0);
        }
        //关闭数据库
        cursor.close();
        database.close();
        return mode;
    }

    /**
     * 根据黑名单号码,删除相应的数据
     * @param blackNum
     */
    public void deleteBlackNum(String blackNum){
        //1、获取数据库
        SQLiteDatabase database = blackNumOpenHelper.getReadableDatabase();
        //2、删除
        //table:表明
        //whereClause:查询的条件
        //whereArgs:查询条件的参数
        database.delete(BlackNumOpenHelper.DB_NAME,"blacknum=?",new String[]{blackNum});
        //3、关闭数据库
        database.close();
    }

    /**
     * 查询全部黑名单号码
     */
    public List<BlackNumInfo> queryAllBlackNum(){
        List<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
        //1、获取数据库
        SQLiteDatabase database = blackNumOpenHelper.getReadableDatabase();
        //2、查询操作
        Cursor cursor = database.query(BlackNumOpenHelper.DB_NAME,new String[]{"blacknum","mode"},null,null,null,null,"_id desc");//desc:倒序查询,asc:正序查询,默认正序查询
        //3、解析cursor
        while (cursor.moveToNext()){
            //获取查询出来的数据
            String blacknum= cursor.getString(0);
            int mode = cursor.getInt(1);
            BlackNumInfo blackNumInfo = new BlackNumInfo(blacknum,mode);
            list.add(blackNumInfo);
        }
        //4、关闭数据库
        cursor.close();
        database.close();
        return list;
    }

    /**
     * 查询部分数据的方法
     * 查询20条数据
     * MaxNum:查询的总个数
     * startindex:查询的起始位置
     * @return
     */
    public List<BlackNumInfo> getPartBlackNum(int MaxNum,int startindex){
        List<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
        //1、获取数据库
        SQLiteDatabase database = blackNumOpenHelper.getReadableDatabase();
        //2、查询操作
        //Cursor cursor = database.query(BlackNumOpenHelper.DB_NAME,new String[]{"blacknum","mode"},null,null,null,null,"_id desc");//desc:倒序查询,asc:正序查询,默认正序查询
        Cursor cursor = database.rawQuery("select blacknum,mode from info order by _id desc limit ? offset ?",new String[]{MaxNum+"",startindex+""});
        //3、解析cursor
        while (cursor.moveToNext()){
            //获取查询出来的数据
            String blacknum= cursor.getString(0);
            int mode = cursor.getInt(1);
            BlackNumInfo blackNumInfo = new BlackNumInfo(blacknum,mode);
            list.add(blackNumInfo);
        }
        //4、关闭数据库
        cursor.close();
        database.close();
        return list;
    }

}
