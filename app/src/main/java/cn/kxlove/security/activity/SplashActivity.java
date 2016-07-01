package cn.kxlove.security.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseActivity;

/**
 * @author: zhengkaixin
 * @date: 16/6/25
 */
public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 闪屏的核心代码
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,
                        HomeActivity.class); // 从启动动画ui跳转到主ui
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
                SplashActivity.this.finish(); // 结束启动动画界面

            }
        }, 2000);
    }
}
