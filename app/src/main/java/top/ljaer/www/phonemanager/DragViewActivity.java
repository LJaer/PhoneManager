package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by LJaer on 16/10/5.
 */

public class DragViewActivity extends Activity {

    private LinearLayout ll_dragviewe_toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragview);
        ll_dragviewe_toast = (LinearLayout) findViewById(R.id.ll_dragviewe_toast);
        setTouch();

    }

    /**
     * 设置触摸监听
     */
    private void setTouch() {

        ll_dragviewe_toast.setOnTouchListener(new View.OnTouchListener() {
            private int newY;
            private int newX;
            private int startX;
            private int startY;
            //v:当前控件
            //event:控件执行的事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //event.getAction():获取控件的执行的事件
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下的事件
                        System.out.println("按下了。。。");
                        //1、按下控件,记录开始的x和y的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //移动的事件
                        System.out.println("移动了。。。。");
                        //2、移动到新的位置记录新的位置的x和y的坐标
                        newX = (int) event.getRawX();
                        newY = (int) event.getRawY();
                        //3、计算移动偏移量
                        int dX = newX - startX;
                        int dY = newY - startY;
                        //4、移动相应的偏移量,重新绘制控件
                        //获取的是原控件距离父控件左边和顶部的距离
                        int l = ll_dragviewe_toast.getLeft();
                        int t = ll_dragviewe_toast.getTop();
                        //获取洗的控件的距离父控件左边和顶部的距离
                        l+=dX;
                        t+= dY;
                        int r = l+ll_dragviewe_toast.getWidth();
                        int b = t+ll_dragviewe_toast.getHeight();
                        ll_dragviewe_toast.layout(l,t,r,b);//重新绘制控件
                        //5、更新开始时间
                        startX = newX;
                        startY = newY;
                        break;
                    case MotionEvent.ACTION_UP:
                        System.out.println("抬起了。。。。");
                        //抬起的事件
                        break;
                }
                //True if the listener has consumed the event,false otherwise
                //true:事件消费了,执行了,false:表示事件被拦截了。
                return true;
            }
        });
    }
}
