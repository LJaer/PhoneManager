package top.ljaer.www.phonemanager.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by LJaer on 16/10/18.
 */

public class TaskInfo {

    //进程名称
    private String name;
    //图标
    private Drawable icon;
    //占用的内存空间
    private long ramSize;
    //包名
    private String packageName;
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

    public long getRamSize() {
        return ramSize;
    }

    public void setRamSize(long ramSize) {
        this.ramSize = ramSize;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public TaskInfo() {
    }

    public TaskInfo(String name, Drawable icon, long ramSize, String packageName, boolean isUser) {
        this.name = name;
        this.icon = icon;
        this.ramSize = ramSize;
        this.packageName = packageName;
        this.isUser = isUser;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", ramSize=" + ramSize +
                ", packageName='" + packageName + '\'' +
                ", isUser=" + isUser +
                '}';
    }
}
