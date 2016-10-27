package top.ljaer.www.phonemanager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import top.ljaer.www.phonemanager.AntivirusActivity;
import top.ljaer.www.phonemanager.R;
import top.ljaer.www.phonemanager.db.dao.AntivirusDao;
import top.ljaer.www.phonemanager.utils.MD5Util;

/**
 * Created by jaer on 2016/10/27.
 */

public class CacheFragment extends Fragment {
    private List<CachInfo> list;
    private TextView tv_cachefragment_text;
    private ProgressBar pb_cachefragment_progressbar;
    private ListView lv_cachefragment_caches;
    private Button btn_cachefragment_clear;
    private PackageManager pm;
    private MyAdapter myAdapter;

    //初始化操作
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //设置fragment的布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        list = new ArrayList<CachInfo>();
        list.clear();
        //参数1:布局文件
        //参数2:容器
        //参数3:自动挂载  ,一律false
        View view = inflater.inflate(R.layout.fragment_cache,container,false);
        tv_cachefragment_text = (TextView) view.findViewById(R.id.tv_cachefragment_text);
        pb_cachefragment_progressbar = (ProgressBar) view.findViewById(R.id.pb_cachefragment_progressbar);
        lv_cachefragment_caches = (ListView) view.findViewById(R.id.lv_cachefragment_caches);
        btn_cachefragment_clear = (Button) view.findViewById(R.id.btn_cachefragment_clear);
        lv_cachefragment_caches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到详情页面
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.parse("package:"+list.get(position).getPackageName()));
                startActivity(intent);
            }
        });
        return view;
    }
    //设置填充显示数据
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        scanner();
    }

    /**
     * 扫描
     */
    private void scanner() {
        //1、获取包的管理者
        //getActivity() : 获取fragment所在的activity
        pm = getActivity().getPackageManager();
        tv_cachefragment_text.setText("正在初始化128核扫描引擎.....");
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(100);
                List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
                pb_cachefragment_progressbar.setMax(installedPackages.size());
                int count = 0;
                for (final PackageInfo packageInfo :
                        installedPackages) {
                    SystemClock.sleep(50);
                    //设置进度条的最大进度和当前
                    count++;
                    pb_cachefragment_progressbar.setProgress(count);

                    //反射获取缓存
                    try {
                        Class<?> loadClass = getActivity().getClass().getClassLoader().loadClass("android.content.pm.PackageManager");
                        Method method = loadClass.getDeclaredMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
                        //receiver : 类的实例,隐藏参数,方法不是静态的必须指定
                        method.invoke(pm, packageInfo.packageName,mStatsObserver);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    //设置扫描显示的应用的名称
                    final String name = packageInfo.applicationInfo.loadLabel(pm).toString();
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_cachefragment_text.setText("正在扫描:"+name);
                            }
                        });
                    }
                }
                //扫描完成
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_cachefragment_text.setVisibility(View.GONE);
                            pb_cachefragment_progressbar.setVisibility(View.GONE);
                            myAdapter = new MyAdapter();
                            //listview设置adapter
                            lv_cachefragment_caches.setAdapter(myAdapter);
                            if (list.size()>0){
                                btn_cachefragment_clear.setVisibility(View.VISIBLE);
                                //清理缓存
                                btn_cachefragment_clear.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //真正的实现清理缓存
                                        try {
                                            Class<?> loadClass = getActivity().getClass().getClassLoader().loadClass("android.content.pm.PackageManager");
                                            //Long.class  Long     TYPE  long
                                            Method method = loadClass.getDeclaredMethod("freeStorageAndNotify", Long.TYPE,IPackageDataObserver.class);
                                            method.invoke(pm, Long.MAX_VALUE,new MyIPackageDataObserver());
                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        //清理缓存
                                        list.clear();
                                        //更新界面
                                        myAdapter.notifyDataSetChanged();
                                        //隐藏button按钮
                                        btn_cachefragment_clear.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }.start();
    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
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
            View cacheView;
            ViewHolder viewHolder;
            if(convertView==null){
                cacheView = View.inflate(getActivity(), R.layout.item_cache, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_itemcache_icon= (ImageView) cacheView.findViewById(R.id.iv_itemcache_icon);
                viewHolder.tv_itemcache_name = (TextView) cacheView.findViewById(R.id.tv_itemcache_name);
                viewHolder.tv_itemcache_size = (TextView) cacheView.findViewById(R.id.tv_itemcache_size);
                cacheView.setTag(viewHolder);
            }else {
                cacheView = convertView;
                viewHolder= (ViewHolder) cacheView.getTag();
            }

            //设置显示数据
            CachInfo cachInfo = list.get(position);
            //根据包名获取application信息
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(cachInfo.getPackageName(), 0);
                Drawable icon = applicationInfo.loadIcon(pm);
                String name = applicationInfo.loadLabel(pm).toString();
                //设置显示
                viewHolder.iv_itemcache_icon.setImageDrawable(icon);
                viewHolder.tv_itemcache_name.setText(name);
                viewHolder.tv_itemcache_size.setText(cachInfo.getCachesize());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return cacheView;
        }
    }

    //获取缓存大小
    IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
        public void onGetStatsCompleted(PackageStats stats, boolean succeeded) {
            long cachesize = stats.cacheSize;//缓存大小
            //long codesize = stats.codeSize;//应用程序的大小
            //long datasize = stats.dataSize;//数据大小

            if (cachesize>0){
                String cache = Formatter.formatFileSize(getActivity(), cachesize);
                list.add(new CachInfo(stats.packageName,cache));
            }


            //String code = Formatter.formatFileSize(getActivity(), codesize);
            //String data = Formatter.formatFileSize(getActivity(), datasize);

            //System.out.println(stats.packageName+"cachesize:"+cache +" codesize:"+code+" datasize:"+data);
        }
    };

    private class MyIPackageDataObserver extends  IPackageDataObserver.Stub{
        //当缓存清理完成之后调用的方法
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

        }
    }

    static class ViewHolder{
        ImageView iv_itemcache_icon;
        TextView tv_itemcache_name,tv_itemcache_size;
    }

    class CachInfo{
        private String packageName;
        private String cachesize;
        public String getPackageName() {
            return packageName;
        }
        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }
        public String getCachesize() {
            return cachesize;
        }
        public void setCachesize(String cachesize) {
            this.cachesize = cachesize;
        }
        public CachInfo(String packageName, String cachesize) {
            super();
            this.packageName = packageName;
            this.cachesize = cachesize;
        }
    }
}
