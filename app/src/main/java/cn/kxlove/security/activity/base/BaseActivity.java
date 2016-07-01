package cn.kxlove.security.activity.base;

import android.content.Intent;
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

    /**
     * 开启新的activity并且关闭自己
     * @param cls 新的activity的字节码
     */
    public void startActivityAndFinishSelf(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }

    /**
     * 开启新的activity不关闭自己
     * @param cls 新的activity的字节码
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
