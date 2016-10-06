package top.ljaer.www.phonemanager;

import android.test.AndroidTestCase;

import top.ljaer.www.phonemanager.db.BlackNumOpenHelper;
import top.ljaer.www.phonemanager.db.dao.BlackNumDao;

/**
 * Created by LJaer on 16/10/6.
 */

public class BlackNumTest extends AndroidTestCase {

    private BlackNumDao blackNumDao;

    public void testBlackNumOpenHelper(){
        BlackNumOpenHelper blackNumOpenHelper = new BlackNumOpenHelper(getContext());//不能创建数据库
        blackNumOpenHelper.getReadableDatabase();//创建数据库
    }

    //在测试方法执行之前执行的方法
    @Override
    protected void setUp() throws Exception {
        blackNumDao = new BlackNumDao(getContext());
        super.setUp();
    }

    //在测试方法之后执行的方法
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAddBlackNum(){
        //BlackNumDao blackNumDao = new BlackNumDao(getContext());
        blackNumDao.addBlackNum("110",BlackNumDao.CALL);
    }

    public void testUpdateBlackNum(){
        //BlackNumDao blackNumDao = new BlackNumDao(getContext());
        blackNumDao.updateBlackNum("110",BlackNumDao.ALL);
    }

    public void testQueryBlackNumMode(){
        //BlackNumDao blackNumDao = new BlackNumDao(getContext());
        int mode = blackNumDao.queryBlackNumMode("110");
        //断言   参数1:期望的值     参数2:实际的值,获取的值
        assertEquals(2,mode);
    }

    public void testDeleteBlackNum(){
        //BlackNumDao blackNumDao = new BlackNumDao(getContext());
        blackNumDao.deleteBlackNum("110");
    }

}
