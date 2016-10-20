package top.ljaer.www.phonemanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import top.ljaer.www.phonemanager.bean.AppInfo;
import top.ljaer.www.phonemanager.bean.TaskInfo;
import top.ljaer.www.phonemanager.engine.AppEngine;
import top.ljaer.www.phonemanager.engine.TaskEngine;
import top.ljaer.www.phonemanager.utils.MyAsynTaks;

import static top.ljaer.www.phonemanager.R.id.lv_softmanager_applicaiton;

/**
 * Created by LJaer on 16/10/18.
 */

public class TaskManagerActivity extends Activity {

    private ListView lv_taskmanager_processes;
    private ProgressBar loading;

    private List<TaskInfo> list;
    //用户程序集合
    private List<TaskInfo> userappinfo;
    //系统程序集合
    private List<TaskInfo> systemappinfo;
    private MyAdapter myadapter;
    private TaskInfo taskInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmanager);
        lv_taskmanager_processes = (ListView)findViewById(R.id.lv_taskmanager_processes);
        loading = (ProgressBar) findViewById(R.id.loading);
        //加载数据
        fillData();
        listviewItemClick();
    }

    /**
     * listview条目点击事件
     */
    private void listviewItemClick() {
        lv_taskmanager_processes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //view:条目的view对象
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //动态改变checkbox
                //1、屏蔽用户程序和系统程序(...个)弹出气泡
                if (position == 0 || position == userappinfo.size() + 1) {
                    return;
                }
                //2、获取条目所对应的应用程序的信息
                //数据就要从userappinfo和systemappinfo中获取
                if (position <= userappinfo.size()) {
                    //用户程序
                    taskInfo = userappinfo.get(position - 1);
                } else {
                    //系统程序
                    taskInfo = systemappinfo.get(position - userappinfo.size() - 2);
                }
                //3、根据之前保存的checkbox的状态设置点击之后的状态,原先选中,点击之后不选中
                if(taskInfo.isChecked()){
                    taskInfo.setChecked(false);
                }else{
                    if (!taskInfo.getPackageName().equals(getPackageName())){
                        taskInfo.setChecked(true);
                    }
                }
                //4、更新界面
                //myadapter.notifyDataSetChanged();
                //更新点击的条目
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.cd_itemtaskmanager_ischecked.setChecked(taskInfo.isChecked());
            }
        });
    }

    /**
     * 加载数据
     */
    private void fillData() {
        new MyAsynTaks() {

            @Override
            public void preTask() {
                loading.setVisibility(ListView.VISIBLE);
            }

            @Override
            public void doinBack() {
                list = TaskEngine.getTaskAllInfo(getApplicationContext());
                userappinfo = new ArrayList<TaskInfo>();
                systemappinfo = new ArrayList<TaskInfo>();
                for (TaskInfo taskInfo :
                        list) {
                    //将数据分别存放到用户程序集合和系统程序集合中
                    if (taskInfo.isUser()) {
                        userappinfo.add(taskInfo);
                    } else {
                        systemappinfo.add(taskInfo);
                    }
                }
            }

            @Override
            public void postTask() {
                if (myadapter == null) {
                    myadapter = new MyAdapter();
                    lv_taskmanager_processes.setAdapter(myadapter);
                } else {
                    myadapter.notifyDataSetChanged();
                }
                loading.setVisibility(ListView.INVISIBLE);
            }
        }.execute();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            //list.size()=userappinfo.size()+systemappinfo.size()
            return userappinfo.size() + systemappinfo.size() + 2;//+2是两个条目
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (position == 0) {
                //添加用户程序(...个)textview
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("用户进程(" + userappinfo.size() + ")");
                return textView;
            } else if (position == userappinfo.size() + 1) {
                //添加系统程序(...个)textview
                TextView textView = new TextView(getApplicationContext());
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextColor(Color.WHITE);
                textView.setText("系统进程(" + systemappinfo.size() + ")");
                return textView;
            }


            View view;
            ViewHolder viewHolder;
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(), R.layout.item_taskmanager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_itemtaskmanager_icon = (ImageView) view.findViewById(R.id.iv_itemtaskmanager_icon);
                viewHolder.tv_itemtaskmanager_name = (TextView) view.findViewById(R.id.tv_itemtaskmanager_name);
                viewHolder.tv_itemtaskmanager_ram = (TextView) view.findViewById(R.id.tv_itemtaskmanager_ram);
                viewHolder.cd_itemtaskmanager_ischecked = (CheckBox) view.findViewById(R.id.cd_itemtaskmanager_ischecked);
                //将viewholder和view对象绑定
                view.setTag(viewHolder);
            }
            //1、获取应用程序的信息
            TaskInfo taskInfo = null;
            //数据就要从userappinfo和systemappinfo中获取
            if (position <= userappinfo.size()) {
                //用户程序
                taskInfo = userappinfo.get(position - 1);
            } else {
                //系统程序
                taskInfo = systemappinfo.get(position - userappinfo.size() - 2);
            }

            //2、设置显示数据,null.方法  参数为null
            if(taskInfo.getIcon()==null){
                viewHolder.iv_itemtaskmanager_icon.setImageResource(R.mipmap.ic_default);
            }else{
                viewHolder.iv_itemtaskmanager_icon.setImageDrawable(taskInfo.getIcon());
            }
            //名称为包名
            if(TextUtils.isEmpty(taskInfo.getName())){
                viewHolder.tv_itemtaskmanager_name.setText(taskInfo.getPackageName());
            }else{
                viewHolder.tv_itemtaskmanager_name.setText(taskInfo.getName());
            }

            long ramSize = taskInfo.getRamSize();
            //数据转换
            String formatFileSize = Formatter.formatFileSize(getApplicationContext(), ramSize);
            viewHolder.tv_itemtaskmanager_ram.setText("内存占用"+formatFileSize);

            //因为checkbox的状态会跟着一起复用,所以一般要动态修改控件的状态,不会跟着去复用,而是将状态保存到bean对象,在每次复用使用控件的时候,根据每个条目对应的bean对象中保存的状态,来设置控件显示的相应的状态
            if(taskInfo.isChecked()){
                viewHolder.cd_itemtaskmanager_ischecked.setChecked(true);
            }else{
                viewHolder.cd_itemtaskmanager_ischecked.setChecked(false);
            }
            //判断如果是我们的应用程序，就把checkbox隐藏，不是的话显示，在getview中有if必须有else
            if(taskInfo.getPackageName().equals(getPackageName())){
                viewHolder.cd_itemtaskmanager_ischecked.setVisibility(View.INVISIBLE);
            }else{
                viewHolder.cd_itemtaskmanager_ischecked.setVisibility(View.VISIBLE);
            }
            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_itemtaskmanager_icon;
        TextView tv_itemtaskmanager_name, tv_itemtaskmanager_ram;
        CheckBox cd_itemtaskmanager_ischecked;
    }

    /**
     * 全选
     * @param v
     */
    public void all(View v){
        //用户进程
        for (int i=0;i<userappinfo.size();i++){
            if (!userappinfo.get(i).getPackageName().equals(getPackageName())) {
                userappinfo.get(i).setChecked(true);
            }
        }
        //系统进程
        for (int i=0;i<systemappinfo.size();i++){
            systemappinfo.get(i).setChecked(true);
        }
        //更新界面
        myadapter.notifyDataSetChanged();
    }

    /**
     * 取消
     * @param v
     */
    public void cancel(View v){
        //用户进程
        for (int i=0;i<userappinfo.size();i++){
            userappinfo.get(i).setChecked(false);
        }
        //系统进程
        for (int i=0;i<systemappinfo.size();i++){
            systemappinfo.get(i).setChecked(false);
        }
        //更新界面
        myadapter.notifyDataSetChanged();
    }

    public void clear(View v){
        //1、获取进程的管理者
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //保存杀死进程信息的集合
        List<TaskInfo> deleteTaskInfos = new ArrayList<TaskInfo>();

        //用户进程
        for (int i=0; i<userappinfo.size();i++){
            if(userappinfo.get(i).isChecked()){
                //杀死进程
                //packageName:进程的包名
                //杀死后台进程
                am.killBackgroundProcesses(userappinfo.get(i).getPackageName());
                deleteTaskInfos.add(userappinfo.get(i));//将杀死的进程信息保存到集合中
            }
        }

        //系统进程
        for (int i=0; i<systemappinfo.size();i++){
            if(systemappinfo.get(i).isChecked()){
                //杀死进程
                //packageName:进程的包名
                //杀死后台进程
                am.killBackgroundProcesses(systemappinfo.get(i).getPackageName());
                deleteTaskInfos.add(systemappinfo.get(i));//将杀死的进程信息保存到集合中
            }
        }

        long memory = 0;
        // 遍历deleteTaskInfos，分别从userappinfo和systemappinfo中删除deleteInfos中的数据
        for (TaskInfo taskInfo: deleteTaskInfos){
            if (taskInfo.isUser()){
                userappinfo.remove(taskInfo);
            }else {
                systemappinfo.remove(taskInfo);
            }
            memory+=taskInfo.getRamSize();
        }
        //数据转化
        String deleteSize = Formatter.formatFileSize(getApplicationContext(),memory);
        Toast.makeText(getApplicationContext(),"共清理"+deleteTaskInfos.size()+"个进程，释放"+deleteSize+"内存空间",Toast.LENGTH_SHORT).show();

        //为下次清理进程做准备
        deleteTaskInfos.clear();
        deleteTaskInfos=null;

        //更新界面
        myadapter.notifyDataSetChanged();
    }
}
