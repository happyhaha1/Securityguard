package cn.kxlove.security.activity.lost;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
@ContentView(R.layout.activity_setup2)
public class SetUp2Activity extends BaseSetUpActivity {

    private TelephonyManager mTelephonyManager;

    @ViewInject(R.id.btn_bind_sim)
    private Button mBindSIMBtn;

    @ViewInject(R.id.rb_second)
    private RadioButton mRbSecond;

    private static final int SIM_ALLOW=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setup2);
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        initView();
    }

    private void initView() {
        // 设置第二个小圆点的颜色
        mRbSecond.setChecked(true);
        if (isBind()) {
            mBindSIMBtn.setEnabled(false);
        } else {
            mBindSIMBtn.setEnabled(true);
        }
    }

    private boolean isBind() {
        String simString = sp.getString("sim", null);
        if (TextUtils.isEmpty(simString)) {
            return false;
        }
        return true;
    }

    @Override
    public void showNext() {
        if (!isBind()) {
            Toast.makeText(this, "您还没有帮定SIM卡！", Toast.LENGTH_SHORT).show();
            return;
        }
//        startActivityAndFinishSelf(SetUp3Activity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUp1Activity.class);
    }


    @Event(R.id.btn_bind_sim)
    private void simClick(View v) {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
                Toast.makeText(this, "获取条件不被赞同", Toast.LENGTH_SHORT)
                        .show();
            }else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, SIM_ALLOW);
            }
        }else {
            bindSIM();
        }
    }

    /**
     * 绑定sim卡
     */
    private void bindSIM() {
        if (!isBind()) {
            String simSerialNumber = mTelephonyManager.getSimSerialNumber();
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("sim", simSerialNumber);
            edit.apply();
            Toast.makeText(this, "SIM卡绑定成功！", Toast.LENGTH_SHORT).show();
            mBindSIMBtn.setEnabled(false);
        } else {
            // 已经绑定，提醒用户
            Toast.makeText(this, "SIM卡已经绑定！", Toast.LENGTH_SHORT).show();
            mBindSIMBtn.setEnabled(false);
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SIM_ALLOW:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bindSIM();
                } else {
                    Toast.makeText(this, "获取条件不被赞同", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
