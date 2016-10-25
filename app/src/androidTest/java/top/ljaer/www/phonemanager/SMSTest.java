package top.ljaer.www.phonemanager;

import android.test.AndroidTestCase;

import top.ljaer.www.phonemanager.engine.SMSEngine;

/**
 * Created by LJaer on 16/10/25.
 */

public class SMSTest extends AndroidTestCase {
    public void testsms(){
        SMSEngine.getAllSMS(getContext());
    }
}
