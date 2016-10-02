package top.ljaer.www.phonemanager;

import android.test.AndroidTestCase;


import java.util.HashMap;
import java.util.List;

import top.ljaer.www.phonemanager.engine.ContactEngine;

/**
 * Created by LJaer on 16/10/2.
 */

public class ContactsTest extends AndroidTestCase {

    public void testContacts(){
        List<HashMap<String,String>> list = ContactEngine.getAllContactInfo(getContext());
        for (HashMap<String,String> hashMap:
             list) {
            System.out.println("姓名:"+hashMap.get("name")+"  电话:"+hashMap.get("phone"));
        }
    }

}
