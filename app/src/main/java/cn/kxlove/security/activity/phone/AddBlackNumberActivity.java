package cn.kxlove.security.activity.phone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseActivity;
import cn.kxlove.security.activity.lost.ContactSelectActivity;
import cn.kxlove.security.dao.BlackNumberDao;
import cn.kxlove.security.model.BlackContactInfo;

/**
 * @author happyhaha
 *         Created on 2016-09-12 10:32
 */
@ContentView(R.layout.activity_add_blacknumber)
public class AddBlackNumberActivity extends BaseActivity {

    private CheckBox mSmsCB;
    private CheckBox mTelCB;
    private EditText mNumET;
    private EditText mNameET;
    private BlackNumberDao dao = new BlackNumberDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("添加黑名单");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));
        setStatusBarColor(getResources().getColor(R.color.purple));

        mSmsCB = (CheckBox) findViewById(R.id.cb_blacknumber_sms);
        mTelCB = (CheckBox) findViewById(R.id.cb_blacknumber_tel);
        mNumET = (EditText) findViewById(R.id.et_balcknumber);
        mNameET = (EditText) findViewById(R.id.et_blackname);
    }

    @Event(value = {R.id.add_blacknum_btn,R.id.add_fromcontact_btn})
    private void onImgvClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.add_blacknum_btn:
                String number = mNumET.getText().toString().trim();
                String name = mNameET.getText().toString().trim();
                if (TextUtils.isEmpty(number) || TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "电话号码和手机号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // 电话号码和名称都不为空
                    BlackContactInfo blackContactInfo = new BlackContactInfo();
                    blackContactInfo.phoneNumber = number;
                    blackContactInfo.contactName = name;
                    if (mSmsCB.isChecked() & mTelCB.isChecked()) {
                        // 两种拦截模式都选
                        blackContactInfo.mode = 3;
                    } else if (mSmsCB.isChecked() & !mTelCB.isChecked()) {
                        // 短信拦截
                        blackContactInfo.mode = 2;
                    } else if (!mSmsCB.isChecked() & mTelCB.isChecked()) {
                        // 电话拦截
                        blackContactInfo.mode = 1;
                    } else {
                        Toast.makeText(this, "请选择拦截模式！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!dao.IsNumberExist(blackContactInfo.phoneNumber)) {
                        dao.add(blackContactInfo);
                    } else {
                        Toast.makeText(this, "该号码已经被添加至黑名单", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
                break;
            case R.id.add_fromcontact_btn:
                startActivityForResult(
                        new Intent(this, ContactSelectActivity.class), 0);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            // 获取选中的联系人信息
            String phone = data.getStringExtra("phone");
            String name = data.getStringExtra("name");
            mNameET.setText(name);
            mNumET.setText(phone);
        }
    }
}
