package top.ljaer.www.phonemanager;

import android.app.Application;
import android.content.ContextWrapper;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest<A extends ContextWrapper> extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
}