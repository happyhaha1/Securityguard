package cn.kxlove.security.activity.lost;

import android.content.SharedPreferences;
import android.os.Bundle;

import org.xutils.view.annotation.ContentView;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseActivity;

/**
 * @author: zhengkaixin
 * @date: 16/7/1
 */

@ContentView(R.layout.activity_lostfind)
public class LostFindActivity extends BaseActivity {

    private SharedPreferences msharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        if(!isSetUp()){
            //如果没有进入过设置向导，则进入
            startActivity(SetUp1Activity.class);
        }
    }

    private boolean isSetUp() {
        return msharedPreferences.getBoolean("isSetUp", false);
    }
}
