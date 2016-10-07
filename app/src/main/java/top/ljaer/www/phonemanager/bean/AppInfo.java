package top.ljaer.www.phonemanager.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by LJaer on 16/10/7.
 */

public class AppInfo {
    //名称
    private String name;
    //图标
    private Drawable icon;
    //包名
    private String packageName;
    //版本号
    private String versionName;
    //是否安装到SD卡
    private boolean isSD;
    //是否是用户程序
    private boolean isUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isSD() {
        return isSD;
    }

    public void setSD(boolean SD) {
        isSD = SD;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public AppInfo() {
    }

    public AppInfo(String name, Drawable icon, String packageName, String versionName, boolean isSD, boolean isUser) {
        this.name = name;
        this.icon = icon;
        this.packageName = packageName;
        this.versionName = versionName;
        this.isSD = isSD;
        this.isUser = isUser;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", isSD=" + isSD +
                ", isUser=" + isUser +
                '}';
    }
}
