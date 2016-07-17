package cn.kxlove.security.activity.lost;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseActivity;
import cn.kxlove.security.adapter.ContactAdapter;
import cn.kxlove.security.model.ContactInfo;
import cn.kxlove.security.utils.ContactInfoParser;

/**
 * @author: zhengkaixin
 * @date: 16/7/1
 */
@ContentView(R.layout.activity_contact_select)
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
//         = (ImageView) findViewById(R.id.imgv_leftbtn);
//        mLeftImgv.setOnClickListener(this);
        mLeftImgV.setImageResource(R.drawable.back);
        //设置导航栏颜色
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.purple));
        setStatusBarColor(getResources().getColor(R.color.purple));

        new Thread(){
            public void run() {
                systemContacts = ContactInfoParser.getSystemContact(ContactSelectActivity.this);
                systemContacts.addAll(ContactInfoParser.getSimContacts(ContactSelectActivity.this));
                mHandler.sendEmptyMessage(10);
            };
        }.start();
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

//    @Override
    @Event(R.id.imgv_leftbtn)
    public void leftClick(View v) {
//        switch (v.getId()) {
//            case R.id.imgv_leftbtn:
                finish();
//                break;


    }
}
