package cn.kxlove.security.activity.lost;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseSetUpActivity;

/**
 * @author: zhengkaixin
 * @date: 16/7/1
 */

public class SetUp1Activity extends BaseSetUpActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        ((RadioButton) findViewById(R.id.rb_first)).setChecked(true);


    }

    @Override
    public void showNext() {
        Toast.makeText(this, "下一页", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showPre() {
        Toast.makeText(this, "已经是第一页了", Toast.LENGTH_SHORT).show();

    }
}
