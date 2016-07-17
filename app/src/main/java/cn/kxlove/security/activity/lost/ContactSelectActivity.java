package cn.kxlove.security.activity.lost;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseActivity;
import cn.kxlove.security.adapter.ContactAdapter;
import cn.kxlove.security.model.ContactInfo;
import cn.kxlove.security.utils.ContactInfoParser;
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
@ContentView(R.layout.activity_contact_select)
@RuntimePermissions
public class ContactSelectActivity extends BaseActivity {

    @ViewInject(R.id.lv_contact)
    private ListView mListView;

    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;

    @ViewInject(R.id.imgv_leftbtn)
    private ImageView mLeftImgV;

    private ContactAdapter adapter;

    private List<ContactInfo> systemContacts;
    private Handler mHandler  = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 10:
                    if(systemContacts != null){
                        adapter = new ContactAdapter(systemContacts,ContactSelectActivity.this);
                        mListView.setAdapter(adapter);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_contact_select);
        initView();
    }

    private void initView() {
        mTvTitle.setText("选择联系人");
        mLeftImgV.setImageResource(R.drawable.back);
        //设置导航栏颜色
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.purple));
        setStatusBarColor(getResources().getColor(R.color.purple));

        ContactSelectActivityPermissionsDispatcher.getContactsWithCheck(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ContactInfo item = (ContactInfo) adapter.getItem(position);
                Intent intent  = new Intent();
                intent.putExtra("phone", item.phone);
                setResult(0, intent);
                finish();
            }
        });
    }


    @Event(R.id.imgv_leftbtn)
    private void leftClick(View v) {
        finish();
    }

    @NeedsPermission(android.Manifest.permission.READ_CONTACTS)
    void getContacts() {
        new Thread(){
            public void run() {
                systemContacts = ContactInfoParser.getSystemContact(ContactSelectActivity.this);
                systemContacts.addAll(ContactInfoParser.getSimContacts(ContactSelectActivity.this));
                mHandler.sendEmptyMessage(10);
            }
        }.start();
    }

//    @SuppressLint("NoCorrespondingNeedsPermission")
    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    void showRationaleForSIM(PermissionRequest request) {
        showRationaleDialog("获取通讯录权限被关闭,请开启", request);
    }

    @OnPermissionDenied(Manifest.permission.READ_CONTACTS)
    void showDeniedForContacts() {
        Toast.makeText(this, "权限不被允许", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    void onContactsNeverAskAgain() {
        Toast.makeText(this, "你没有同意该权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        SetUp2ActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
        ContactSelectActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    private void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }
}
