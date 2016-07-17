package cn.kxlove.security.activity.lost;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author: zhengkaixin
 * @date: 16/7/1
 */
@ContentView(R.layout.activity_setup2)
@RuntimePermissions
public class SetUp2Activity extends BaseSetUpActivity {

    private TelephonyManager mTelephonyManager;

    @ViewInject(R.id.btn_bind_sim)
    private Button mBindSIMBtn;

    @ViewInject(R.id.rb_second)
    private RadioButton mRbSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return !TextUtils.isEmpty(simString);
    }

    @Override
    public void showNext() {
        if (!isBind()) {
            Toast.makeText(this, "您还没有帮定SIM卡！", Toast.LENGTH_SHORT).show();
        }
        startActivityAndFinishSelf(SetUp3Activity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUp1Activity.class);
    }


    @Event(R.id.btn_bind_sim)
    private void simClick(View v) {
        SetUp2ActivityPermissionsDispatcher.bindSIMWithCheck(this);
    }

    /**
     * 绑定sim卡
     */
    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    void bindSIM() {
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

    @OnShowRationale(Manifest.permission.READ_PHONE_STATE)
    void showRationaleForSIM(PermissionRequest request) {
        showRationaleDialog("获取sim卡信息权限被关闭,请开启", request);
    }

    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
    void showDeniedForSIM() {
        Toast.makeText(this, "权限不被允许", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
    void onSIMNeverAskAgain() {
        Toast.makeText(this, "你没有同意该权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        SetUp2ActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }
}
