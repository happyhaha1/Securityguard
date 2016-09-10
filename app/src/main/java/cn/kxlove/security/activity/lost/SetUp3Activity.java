package cn.kxlove.security.activity.lost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseSetUpActivity;

/**
 * @author: zhengkaixin
 * @date: 16/7/1
 */
@ContentView(R.layout.activity_setup3)
public class SetUp3Activity extends BaseSetUpActivity {

    @ViewInject(R.id.et_inputphone)
    private EditText mInputPhone;

    @ViewInject(R.id.rb_third)
    private RadioButton mRbThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        (R.layout.activity_setup3);
        initView();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        mRbThird.setChecked(true);
        String safephone= sp.getString("safephone", null);
        if(!TextUtils.isEmpty(safephone)){
            mInputPhone.setText(safephone);
        }
    }

    @Override
    public void showNext() {
        //判断文本输入框中是否有电话号码
        String safePhone = mInputPhone.getText().toString().trim();
        if(TextUtils.isEmpty(safePhone)){
            Toast.makeText(this, "请输入安全号码", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("safephone", safePhone);
        edit.apply();
        startActivityAndFinishSelf(SetUp4Activity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUp2Activity.class);
    }

//    @Override
    @Event(R.id.btn_addcontact)
    private void contactClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_addcontact:
        startActivityForResult(new Intent(this,ContactSelectActivity.class), 0);
//                break;
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            String phone = data.getStringExtra("phone");
            mInputPhone.setText(phone);
        }
    }
}
