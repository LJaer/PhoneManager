package top.ljaer.www.phonemanager;

import android.test.AndroidTestCase;

import top.ljaer.www.phonemanager.db.WatchDogOpenHelper;

/**
 * Created by LJaer on 16/10/22.
 */

public class WatchDogTest extends AndroidTestCase {

    public void testWatchDogOpenHelper(){
        WatchDogOpenHelper watchDogOpenHelper = new WatchDogOpenHelper(getContext());
        watchDogOpenHelper.getReadableDatabase();
    }
}
