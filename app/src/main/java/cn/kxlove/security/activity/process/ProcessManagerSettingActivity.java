package cn.kxlove.security.activity.process;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseActivity;
import cn.kxlove.security.service.AutoKillProcessService;
import cn.kxlove.security.utils.SystemInfoUtils;

@ContentView(R.layout.activity_process_manager_setting)
public class ProcessManagerSettingActivity extends BaseActivity {

    @ViewInject(R.id.tgb_showsys_process)
    private ToggleButton mShowSysAppsTgb;

    @ViewInject(R.id.tgb_killprocess_lockscreen)
    private ToggleButton mKillProcessTgb;

    private SharedPreferences mSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        initView();
    }

    /**初始化控件*/
    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("进程管理设置");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_green));
        setStatusBarColor(getResources().getColor(R.color.bright_green));

        mShowSysAppsTgb.setChecked(mSP.getBoolean("showSystemProcess", true));
        boolean running = SystemInfoUtils.isServiceRunning(this, "cn.kxlove.security.service.AutoKillProcessService");
        mKillProcessTgb.setChecked(running);
    }



    @Event(value = {R.id.imgv_leftbtn})
    private void onImgvClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }

    @Event(value = {R.id.tgb_killprocess_lockscreen,R.id.tgb_showsys_process},type = CompoundButton.OnCheckedChangeListener.class)
    private void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tgb_showsys_process:
                saveStatus("showSystemProcess",isChecked);
                break;
            case R.id.tgb_killprocess_lockscreen:
                Intent service = new Intent(this,AutoKillProcessService.class);
                if(isChecked){
                    //开启服务
                    startService(service );
                }else{
                    //关闭服务
                    stopService(service);
                }
                break;

        }

    }

    private void saveStatus(String string, boolean isChecked) {
        SharedPreferences.Editor edit = mSP.edit();
        edit.putBoolean(string, isChecked);
        edit.apply();
    }
}
