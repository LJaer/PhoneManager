package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by LJaer on 16/10/5.
 */

public class DragViewActivity extends Activity {
    private SharedPreferences sp;
    private LinearLayout ll_dragviewe_toast;
    private int width;
    private int height;
    private TextView tv_dragview_bottom;
    private TextView tv_dragview_top;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragview);
        ll_dragviewe_toast = (LinearLayout) findViewById(R.id.ll_dragviewe_toast);
        sp = getSharedPreferences("config",MODE_PRIVATE);
        tv_dragview_bottom = (TextView) findViewById(R.id.tv_dragview_bottom);
        tv_dragview_top = (TextView) findViewById(R.id.tv_dragview_top);

        //获取屏幕的宽度
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //windowManager.getDefaultDisplay().getWidth();
        DisplayMetrics outMetrics = new DisplayMetrics();//创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);//给白纸设置宽高
        width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;

        //设置控件回显操作
        //1、获取保存的坐标
        int x = sp.getInt("x",0);
        int y = sp.getInt("y",0);
        System.out.println("x:"+x+" y:"+y);
        //2、重新绘制控件
        //由于在oncreate方法中还没执行完,不能获取到控件的宽高
        //ll_dragviewe_toast.layout(x,y,x+ll_dragviewe_toast.getWidth(),y+ll_dragviewe_toast.getHeight());
        //在初始化控件之前,重新设置控件的属性
        //获取父控件的属性规则,父控件的layoutparams
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_dragviewe_toast.getLayoutParams();
        //设置相应的属性
        //leftMargin:距离父控件左边的距离,跟布局文件中layout_marginleft属性效果相似
        params.leftMargin = x;
        params.topMargin = y;
        //给控件设置属性
        ll_dragviewe_toast.setLayoutParams(params);

        if(y>=height/2){
            //隐藏下方显示上方
            tv_dragview_bottom.setVisibility(View.INVISIBLE);
            tv_dragview_top.setVisibility(View.VISIBLE);
        }else{
            //隐藏上方显示下方
            tv_dragview_bottom.setVisibility(View.VISIBLE);
            tv_dragview_top.setVisibility(View.INVISIBLE);
        }

        setTouch();
        setDoubleClick();
    }

    long[] mHits = new long[2];

    /**
     * 双击居中
     */
    private void setDoubleClick() {
        ll_dragviewe_toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *  src the source array to copy the content.   拷贝的原数组
                 srcPos the starting index of the content in src.  是从源数组那个位置开始拷贝
                 dst the destination array to copy the data into.  拷贝的目标数组
                 dstPos the starting index for the copied content in dst.  是从目标数组那个位置开始去写
                 length the number of elements to be copied.   拷贝的长度
                 */
                //拷贝数组操作
                System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();  // 将离开机的时间设置给数组的第二个元素,离开机时间 :毫秒值,手机休眠不算
                if (mHits[0] >= (SystemClock.uptimeMillis()-500)) {  // 判断是否多击操作
                    //双击居中
                    int l = (width-ll_dragviewe_toast.getWidth())/2;
                    int t = (height-25-ll_dragviewe_toast.getHeight())/2;
                    ll_dragviewe_toast.layout(l,t,l+ll_dragviewe_toast.getWidth(),t+ll_dragviewe_toast.getHeight());
                    //保存控件的坐标
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("x",l);
                    edit.putInt("y",t);
                    edit.commit();
                }
            }
        });
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

                        //在绘制控件之前,判断ltrb的值是否超出屏幕的大小,如果是就不在进行绘制控件的操作
                        if(l<0||r>width||t<0||b>height-25){
                            break;
                        }

                        ll_dragviewe_toast.layout(l,t,r,b);//重新绘制控件
                        //判断textview的隐藏显示
                        int top = ll_dragviewe_toast.getTop();
                        if(top>=height/2){
                            //隐藏下方显示上方
                            tv_dragview_bottom.setVisibility(View.INVISIBLE);
                            tv_dragview_top.setVisibility(View.VISIBLE);
                        }else{
                            //隐藏上方显示下方
                            tv_dragview_bottom.setVisibility(View.VISIBLE);
                            tv_dragview_top.setVisibility(View.INVISIBLE);
                        }

                        //5、更新开始时间
                        startX = newX;
                        startY = newY;
                        break;
                    case MotionEvent.ACTION_UP:
                        //抬起的事件
                        System.out.println("抬起了。。。。");
                        //保存控件的坐标,保存的是控件的坐标不是手指的坐标
                        //获取控件的坐标
                        int x = ll_dragviewe_toast.getLeft();
                        int y = ll_dragviewe_toast.getTop();
                        SharedPreferences.Editor edit =  sp.edit();
                        edit.putInt("x",x);
                        edit.putInt("y",y);
                        edit.commit();
                        break;
                }
                //True if the listener has consumed the event,false otherwise
                //true:事件消费了,执行了,false:表示事件被拦截了。
                return false;
            }
        });
    }
}