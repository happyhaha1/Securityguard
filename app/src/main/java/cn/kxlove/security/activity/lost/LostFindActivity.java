package cn.kxlove.security.activity.lost;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        if(!isSetUp()){
            //如果没有进入过设置向导，则进入
//            startActivity(SetUp1Activity.class);
        }
        initView();
    }

    private void initView() {
        mTextView.setText("手机防盗");
//        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));
        setStatusBarColor(getResources().getColor(R.color.purple));
    }

    private boolean isSetUp() {
        return msharedPreferences.getBoolean("isSetUp", false);
    }


    @Event(value = {R.id.imgv_leftbtn,R.id.rl_inter_setup_wizard})
    private void onImgvClick(View v) {
        switch (v.getId()) {
            case R.id.rl_inter_setup_wizard:
                //重新进入设置向导
                Toast.makeText(getApplicationContext(),
                        "无效动作,移动太慢", Toast.LENGTH_SHORT).show();
                startActivity(SetUp1Activity.class);
                break;
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }
}
