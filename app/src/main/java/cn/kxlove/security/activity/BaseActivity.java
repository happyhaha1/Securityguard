package cn.kxlove.security.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xutils.x;

/**
 * @author: zhengkaixin
 * @date: 16/6/25
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ButterKnife.bind(this);
        x.view().inject(this);
    }
}
