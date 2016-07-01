package cn.kxlove.security.activity.lost;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseSetUpActivity;

/**
 * @author: zhengkaixin
 * @date: 16/7/1
 */

@ContentView(R.layout.activity_setup1)
public class SetUp1Activity extends BaseSetUpActivity{

    @ViewInject(R.id.rb_first)
    private RadioButton rbFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setup1);
        rbFirst.setChecked(true);


    }

    @Override
    public void showNext() {
        startActivityAndFinishSelf(SetUp2Activity.class);
    }

    @Override
    public void showPre() {
        Toast.makeText(this, "已经是第一页了", Toast.LENGTH_SHORT).show();

    }
}
