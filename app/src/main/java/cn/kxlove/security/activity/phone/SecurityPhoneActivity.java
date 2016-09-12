package cn.kxlove.security.activity.phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseActivity;
import cn.kxlove.security.adapter.BlackContactAdapter;
import cn.kxlove.security.dao.BlackNumberDao;
import cn.kxlove.security.model.BlackContactInfo;

/**
 * @author happyhaha
 * Created on 2016-09-12 09:28
 */
@ContentView(R.layout.activity_securityphone)
public class SecurityPhoneActivity extends BaseActivity{

    /** 有黑名单时，显示的帧布局 */
    @ViewInject(R.id.fl_haveblacknumber)
    private FrameLayout mHaveBlackNumber;

    /** 没有黑名单时，显示的帧布局 */
    @ViewInject(R.id.fl_noblacknumber)
    private FrameLayout mNoBlackNumber;

    private BlackNumberDao dao = new BlackNumberDao();

    @ViewInject(R.id.lv_blacknumbers)
    private ListView mListView;

    private int pagenumber = 0;
    private int pagesize = 15;
    private int totalNumber;
    private List<BlackContactInfo> pageBlackNumber = new ArrayList<BlackContactInfo>();
    private BlackContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (totalNumber != dao.getTotalNumber()) {
            // 数据发生变化
            if (dao.getTotalNumber() > 0) {
                mHaveBlackNumber.setVisibility(View.VISIBLE);
                mNoBlackNumber.setVisibility(View.GONE);
            } else {
                mHaveBlackNumber.setVisibility(View.GONE);
                mNoBlackNumber.setVisibility(View.VISIBLE);
            }
            pagenumber = 0;
            pageBlackNumber.clear();
            pageBlackNumber
                    .addAll(dao.getPageBlackNumber(pagenumber, pagesize));
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

    }

    private void fillData() {
        totalNumber = dao.getTotalNumber();
        if (totalNumber == 0) {
            // 数据库中没有黑名单数据
            mHaveBlackNumber.setVisibility(View.GONE);
            mNoBlackNumber.setVisibility(View.VISIBLE);
        } else if (totalNumber > 0) {
            // 数据库中含有黑名单数据
            mHaveBlackNumber.setVisibility(View.VISIBLE);
            mNoBlackNumber.setVisibility(View.GONE);
            pagenumber = 0;
            if (pageBlackNumber.size() > 0) {
                pageBlackNumber.clear();
            }
            pageBlackNumber
                    .addAll(dao.getPageBlackNumber(pagenumber, pagesize));
            if (adapter == null) {
                adapter = new BlackContactAdapter(pageBlackNumber,
                        SecurityPhoneActivity.this);
                adapter.setCallBack(new BlackContactAdapter.BlackConactCallBack() {
                    @Override
                    public void DataSizeChanged() {
                        fillData();
                    }
                });
                mListView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }

    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("通讯卫士");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));
        setStatusBarColor(getResources().getColor(R.color.purple));
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 没有滑动的状态
                        // 获取最后一个可见条目
                        int lastVisiblePosition = mListView
                                .getLastVisiblePosition();
                        // 如果当前条目是最后一个 增查询更多的数据
                        if (lastVisiblePosition == pageBlackNumber.size() - 1) {
                            pagenumber++;
                            if (pagenumber * pagesize >= totalNumber) {
                                Toast.makeText(SecurityPhoneActivity.this,
                                        "没有更多的数据了", Toast.LENGTH_SHORT).show();
                            } else {
                                pageBlackNumber.addAll(dao.getPageBlackNumber(
                                        pagenumber, pagesize));
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
    }


    @Event(value = {R.id.imgv_leftbtn,R.id.btn_addblacknumber})
    private void onImgvClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.btn_addblacknumber:
                // 跳转至添加黑名单页面
                startActivity(new Intent(this, AddBlackNumberActivity.class));
                break;
        }
    }

}
