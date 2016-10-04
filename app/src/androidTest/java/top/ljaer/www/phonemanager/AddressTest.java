package top.ljaer.www.phonemanager;

import android.test.AndroidTestCase;
import android.text.TextUtils;

import top.ljaer.www.phonemanager.db.dao.AddressDao;

/**
 * Created by LJaer on 16/10/4.
 */

public class AddressTest extends AndroidTestCase {
    public void testAddress(){
        String queryAddress = AddressDao.queryAddress("18888888888",getContext());
        if(TextUtils.isEmpty(queryAddress)){
            System.out.println("号码归属地:"+queryAddress);
        }
    }
}
