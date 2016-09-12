package cn.kxlove.security.activity.lost;

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

/**
 * @author: zhengkaixin
 * @date: 16/7/1
 */

@ContentView(R.layout.activity_lostfind)
public class LostFindActivity extends BaseActivity{

    private SharedPreferences msharedPreferences;

    @ViewInject(R.id.tv_title)
    private TextView mTextView;

    @ViewInject(R.id.imgv_leftbtn)
    private ImageView mLeftImgv;

    @ViewInject(R.id.togglebtn_lostfind)
    private ToggleButton mToggleButton;

    @ViewInject(R.id.tv_safephone)
    private TextView mSafePhoneTV;

    @ViewInject(R.id.tv_lostfind_protectstauts)
    private TextView mProtectStatusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        if(!isSetUp()){
            //如果没有进入过设置向导，则进入
            startActivity(SetUp1Activity.class);
        }
        initView();
    }

    private void initView() {
        mTextView.setText("手机防盗");
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));
        setStatusBarColor(getResources().getColor(R.color.purple));

        mSafePhoneTV.setText(msharedPreferences.getString("safephone", ""));
        //查询手机防盗是否开启，默认为开启
        boolean protecting = msharedPreferences.getBoolean("protecting", true);
        if(protecting){
            mProtectStatusTV.setText("防盗保护已经开启");
            mToggleButton.setChecked(true);
        }else{
            mProtectStatusTV.setText("防盗保护没有开启");
            mToggleButton.setChecked(false);
        }
        mToggleButton.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mProtectStatusTV.setText("防盗保护已经开启");
                }else{
                    mProtectStatusTV.setText("防盗保护没有开启");
                }
                SharedPreferences.Editor editor = msharedPreferences.edit();
                editor.putBoolean("protecting", isChecked);
                editor.apply();

            }
        });
    }

    private boolean isSetUp() {
        return msharedPreferences.getBoolean("isSetUp", false);
    }


    @Event(value = {R.id.imgv_leftbtn,R.id.rl_inter_setup_wizard})
    private void onImgvClick(View v) {
        switch (v.getId()) {
            case R.id.rl_inter_setup_wizard:
                startActivityAndFinishSelf(SetUp1Activity.class);
                break;
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }
}
