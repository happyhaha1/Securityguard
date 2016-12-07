package cn.kxlove.security.activity.app;

/**
 * @author happyhaha
 *         Created on 2016-09-12 14:36
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseActivity;
import cn.kxlove.security.adapter.AppManagerAdapter;
import cn.kxlove.security.model.AppInfo;
import cn.kxlove.security.utils.AppInfoParser;

@ContentView(R.layout.activity_app_manager)
public class AppManagerActivity extends BaseActivity{
    /**手机剩余内存TextView*/
    @ViewInject(R.id.tv_phonememory_appmanager)
    private TextView mPhoneMemoryTV;
    /**展示SD卡剩余内存TextView*/
    @ViewInject(R.id.tv_sdmemory_appmanager)
    private TextView mSDMemoryTV;
    @ViewInject(R.id.lv_appmanager)
    private ListView mListView;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos = new ArrayList<AppInfo>();
    private  List<AppInfo> systemAppInfos = new ArrayList<AppInfo>();
    private AppManagerAdapter adapter;
    /**接收应用程序卸载成功的广播*/
    private UninstallRececiver receciver;

    @ViewInject(R.id.tv_appnumber)
    private TextView mAppNumTV;

    private Handler mHandler = new Handler(){
//
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 10:
                    if(adapter == null){
                        adapter = new AppManagerAdapter(userAppInfos, systemAppInfos, AppManagerActivity.this);
                    }
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case 15:
                    adapter.notifyDataSetChanged();
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册广播
        receciver = new UninstallRececiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(receciver, intentFilter);
        initView();
    }

    /**初始化控件*/
    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("通讯卫士");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_yellow));
        setStatusBarColor(getResources().getColor(R.color.bright_yellow));

        //拿到手机剩余内存和SD卡剩余内存
        getMemoryFromPhone();
        initData();
        initListener();
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                if (adapter != null) {
                    new Thread(){
                        public void run() {
                            AppInfo mappInfo  = (AppInfo) adapter.getItem(position);
                            //记住当前条目的状态
                            boolean flag = mappInfo.isSelected;
                            //先将集合中所有条目的AppInfo变为未选中状态
                            for(AppInfo appInfo :userAppInfos){
                                appInfo.isSelected = false;
                            }
                            for(AppInfo appInfo : systemAppInfos){
                                appInfo.isSelected = false;
                            }
                            if(mappInfo != null){
                                //如果已经选中，则变为未选中
                                if(flag){
                                    mappInfo.isSelected = false;
                                }else{
                                    mappInfo.isSelected = true;
                                }
                                mHandler.sendEmptyMessage(15);
                            }
                        };
                    }.start();

                }


            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem >= userAppInfos.size()+1){
                    mAppNumTV.setText("系统程序："+systemAppInfos.size()+"个");
                }else{
                    mAppNumTV.setText("用户程序："+userAppInfos.size()+"个");
                }
            }
        });
    }
//
    private void initData() {
        appInfos = new ArrayList<>();
        new Thread(){
            public void run() {
                appInfos.clear();
                userAppInfos.clear();
                systemAppInfos.clear();
                appInfos.addAll(AppInfoParser.getAppInfos(AppManagerActivity.this));
                for( AppInfo appInfo : appInfos){
                    //如果是用户App
                    if(appInfo.isUserApp){
                        userAppInfos.add(appInfo);
                    }else{
                        systemAppInfos.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(10);
            };
        }.start();

    }

    /**拿到手机和SD卡剩余内存*/
    private void getMemoryFromPhone() {
        long avail_sd = Environment.getExternalStorageDirectory()
                .getFreeSpace();
        long avail_rom = Environment.getDataDirectory().getFreeSpace();
        //格式化内存
        String str_avail_sd = Formatter.formatFileSize(this, avail_sd);
        String str_avail_rom = Formatter.formatFileSize(this, avail_rom);
        mPhoneMemoryTV.setText("剩余手机内存：" + str_avail_rom);
        mSDMemoryTV.setText("剩余SD卡内存：" + str_avail_sd);
    }

    @Event(value = {R.id.imgv_leftbtn})
    private void onImgvClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }
    /***
     * 接收应用程序卸载的广播
     * @author admin
     */
    class UninstallRececiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 收到广播了
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receciver);
        receciver = null;
        super.onDestroy();
    }
}
