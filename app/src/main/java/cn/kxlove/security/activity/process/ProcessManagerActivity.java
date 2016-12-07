package cn.kxlove.security.activity.process;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseActivity;
import cn.kxlove.security.adapter.ProcessManagerAdapter;
import cn.kxlove.security.model.TaskInfo;
import cn.kxlove.security.utils.SystemInfoUtils;
import cn.kxlove.security.utils.TaskInfoParser;

@ContentView(R.layout.activity_process_manager)
public class ProcessManagerActivity extends BaseActivity {


    @ViewInject(R.id.tv_runningprocess_num)
    private TextView mRunProcessNum;

    @ViewInject(R.id.tv_memory_processmanager)
    private TextView mMemoryTV;

    @ViewInject(R.id.tv_user_runningprocess)
    private TextView mProcessNumTV;

    @ViewInject(R.id.lv_runningapps)
    private ListView mListView;

    private ProcessManagerAdapter adapter;
    private ActivityManager manager;


    private List<TaskInfo> runningTaskInfos;
    private List<TaskInfo> userTaskInfos = new ArrayList<TaskInfo>();
    private List<TaskInfo> sysTaskInfo = new ArrayList<TaskInfo>();

    private int runningPocessCount;
    private long totalMem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        fillData();
    }
    @Override
    protected void onResume() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }


    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("进程管理");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setImageResource(R.drawable.back);

        ImageView mRightImgv = (ImageView) findViewById(R.id.imgv_rightbtn);
        mRightImgv.setImageResource(R.drawable.processmanager_setting_icon);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_green));
        setStatusBarColor(getResources().getColor(R.color.bright_green));


        runningPocessCount = SystemInfoUtils
                .getRunningPocessCount(ProcessManagerActivity.this);
        mRunProcessNum.setText("运行中的进程： "+runningPocessCount+ "个");
        long totalAvailMem = SystemInfoUtils.getAvailMem(this);
        totalMem = SystemInfoUtils.getTotalMem();
        mMemoryTV.setText("可用/总内存："
                + Formatter.formatFileSize(this, totalAvailMem) + "/"
                + Formatter.formatFileSize(this, totalMem));
        initListener();
    }


    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object object = mListView.getItemAtPosition(position);
                if (object != null & object instanceof TaskInfo) {
                    TaskInfo info = (TaskInfo) object;
                    if (info.packageName.equals(getPackageName())) {
                        // 当前点击的条目是本应用程序
                        return;
                    }
                    info.isChecked = !info.isChecked;
                    adapter.notifyDataSetChanged();
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
                if (firstVisibleItem >= userTaskInfos.size() + 1){
                    mProcessNumTV.setText("系统进程：" + sysTaskInfo.size() + "个");
                }else{
                    mProcessNumTV.setText("用户进程： " + userTaskInfos.size() + "个");
                }
            }
        });
    }

    private void fillData() {
        userTaskInfos.clear();
        sysTaskInfo.clear();
        new Thread() {
            public void run() {
                runningTaskInfos = TaskInfoParser
                        .getRunningTaskInfos(getApplicationContext());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences mSP = getSharedPreferences("config", MODE_PRIVATE);
                        boolean showSystemProcess = mSP.getBoolean("showSystemProcess", true);
                        for (TaskInfo taskInfo : runningTaskInfos) {
                            if (taskInfo.isUserApp) {
                                userTaskInfos.add(taskInfo);
                            } else {
                                if (showSystemProcess) {
                                    sysTaskInfo.add(taskInfo);
                                }
                            }
                        }
                        if (adapter == null) {
                            adapter = new ProcessManagerAdapter(
                                    getApplicationContext(), userTaskInfos,
                                    sysTaskInfo);
                            mListView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        if (userTaskInfos.size() > 0){
                            mProcessNumTV.setText("用户进程： "
                                    + userTaskInfos.size() + "个");
                        }
                        if (sysTaskInfo.size() > 0){
                            mProcessNumTV.setText("系统进程：" + sysTaskInfo.size()
                                + "个");
                        }
                    }
                });
            };
        }.start();
    }

    @Event(value = {R.id.imgv_leftbtn,R.id.imgv_rightbtn,R.id.btn_selectall,R.id.btn_select_inverse,R.id.btn_cleanprocess})
    private void onImgvClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.imgv_rightbtn:
                //跳转至 进程管理设置页面
                startActivity(new Intent(this,ProcessManagerSettingActivity.class));
                break;
            case R.id.btn_selectall:
                selectAll();
                break;
            case R.id.btn_select_inverse:
                inverse();
                break;
            case R.id.btn_cleanprocess:
                cleanProcess();
                break;
        }
    }

    /**清理进程*/
    private void cleanProcess() {
        manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int count=0;
        long saveMemory = 0;
        List<TaskInfo> killedtaskInfos = new ArrayList<TaskInfo>();
        for(TaskInfo info : userTaskInfos){
            if(info.isChecked){
                count++;
                saveMemory += info.appMemory;
                manager.killBackgroundProcesses(info.packageName);
                killedtaskInfos.add(info);
            }
        }
        for(TaskInfo info : sysTaskInfo){
            if(info.isChecked){
                count++;
                saveMemory += info.appMemory;
                manager.killBackgroundProcesses(info.packageName);
                killedtaskInfos.add(info);
            }
        }

        for(TaskInfo info : killedtaskInfos){
            if(info.isUserApp){
                userTaskInfos.remove(info);
            }
            else{
                sysTaskInfo.remove(info);
            }

        }
        runningPocessCount -=count;
        mRunProcessNum.setText("运行中的进程："+runningPocessCount+"个");
        mMemoryTV.setText("可用/总内存："
                + Formatter.formatFileSize(this, SystemInfoUtils.getAvailMem(this)) + "/"
                + Formatter.formatFileSize(this, totalMem));
        Toast.makeText(this, "清理了" + count + "个进程,释放了"
                + Formatter.formatFileSize(this, saveMemory) + "内存", Toast.LENGTH_LONG).show();
        mProcessNumTV.setText("用户进程："+userTaskInfos.size()+"个");
        adapter.notifyDataSetChanged();
    }

    /** 反选 */
    private void inverse() {
        for (TaskInfo taskInfo : userTaskInfos) {
            // 就是本应用程序
            if (taskInfo.packageName.equals(getPackageName())) {
                continue;
            }
            boolean checked = taskInfo.isChecked;
            taskInfo.isChecked = !checked;
        }
        for (TaskInfo taskInfo : sysTaskInfo) {
            boolean checked = taskInfo.isChecked;
            taskInfo.isChecked = !checked;
        }
        adapter.notifyDataSetChanged();
    }

    /** 全选 */
    private void selectAll() {
        for (TaskInfo taskInfo : userTaskInfos) {
            // 就是本应用程序
            if (taskInfo.packageName.equals(getPackageName())) {
                continue;
            }
            taskInfo.isChecked = true;
        }
        for (TaskInfo taskInfo : sysTaskInfo) {
            taskInfo.isChecked = true;
        }
        adapter.notifyDataSetChanged();
    }
}
