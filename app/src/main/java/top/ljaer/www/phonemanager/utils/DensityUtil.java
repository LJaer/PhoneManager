package top.ljaer.www.phonemanager.utils;

import android.content.Context;

/**
 * Created by jaer on 2016/10/21.
 */

public class DensityUtil {
    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     * @return
     */
    public static int dip2qx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  //获取屏幕的密度
        return (int) (dpValue * scale + 0.5f); //+0.5f四舍五入   3.7  3   3.7+0.5 = 4.2   4
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
