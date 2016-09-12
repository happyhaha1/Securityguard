package cn.kxlove.security.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.kxlove.security.R;
import cn.kxlove.security.adapter.BlackContactAdapter;
import cn.kxlove.security.dao.BlackNumberDao;
import cn.kxlove.security.model.BlackContactInfo;

/**
 * @author happyhaha
 * Created on 2016-09-12 09:28
 */
public class SecurityPhoneActivity extends Activity{

    /** 有黑名单时，显示的帧布局 */
    private FrameLayout mHaveBlackNumber;
    /** 没有黑名单时，显示的帧布局 */
    private FrameLayout mNoBlackNumber;
    private BlackNumberDao dao;
    private ListView mListView;
    private int pagenumber = 0;
    private int pagesize = 15;
    private int totalNumber;
    private List<BlackContactInfo> pageBlackNumber = new ArrayList<BlackContactInfo>();
    private BlackContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_securityphone);
        initView();
        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (totalNumber != dao.getTotalNumber()) {
//            // 数据发生变化
//            if (dao.getTotalNumber() > 0) {
//                mHaveBlackNumber.setVisibility(View.VISIBLE);
//                mNoBlackNumber.setVisibility(View.GONE);
//            } else {
//                mHaveBlackNumber.setVisibility(View.GONE);
//                mNoBlackNumber.setVisibility(View.VISIBLE);
//            }
//            pagenumber = 0;
//            pageBlackNumber.clear();
//            pageBlackNumber
//                    .addAll(dao.getPageBlackNumber(pagenumber, pagesize));
//            if (adapter != null) {
//                adapter.notifyDataSetChanged();
//            }
//        }

    }

    private void fillData() {
//        dao = new BlackNumberDao(SecurityPhoneActivity.this);
//        totalNumber = dao.getTotalNumber();
//        if (totalNumber == 0) {
//            // 数据库中没有黑名单数据
//            mHaveBlackNumber.setVisibility(View.GONE);
//            mNoBlackNumber.setVisibility(View.VISIBLE);
//        } else if (totalNumber > 0) {
//            // 数据库中含有黑名单数据
//            mHaveBlackNumber.setVisibility(View.VISIBLE);
//            mNoBlackNumber.setVisibility(View.GONE);
//            pagenumber = 0;
//            if (pageBlackNumber.size() > 0) {
//                pageBlackNumber.clear();
//            }
//            pageBlackNumber
//                    .addAll(dao.getPageBlackNumber(pagenumber, pagesize));
//            if (adapter == null) {
//                adapter = new BlackContactAdapter(pageBlackNumber,
//                        SecurityPhoneActivity.this);
//                adapter.setCallBack(new BlackConactCallBack() {
//                    @Override
//                    public void DataSizeChanged() {
//                        fillData();
//                    }
//                });
//                mListView.setAdapter(adapter);
//            } else {
//                adapter.notifyDataSetChanged();
//            }
//        }

    }

    private void initView() {
//        findViewById(R.id.rl_titlebar).setBackgroundColor(
//                getResources().getColor(R.color.bright_purple));
//        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
//        ((TextView) findViewById(R.id.tv_title)).setText("通讯卫士");
//        mLeftImgv.setOnClickListener(this);
//        mLeftImgv.setImageResource(R.drawable.back);
//        mHaveBlackNumber = (FrameLayout) findViewById(R.id.fl_haveblacknumber);
//        mNoBlackNumber = (FrameLayout) findViewById(R.id.fl_noblacknumber);
//        findViewById(R.id.btn_addblacknumber).setOnClickListener(this);
//        mListView = (ListView) findViewById(R.id.lv_blacknumbers);
//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                switch (scrollState) {
//                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 没有滑动的状态
//                        // 获取最后一个可见条目
//                        int lastVisiblePosition = mListView
//                                .getLastVisiblePosition();
//                        // 如果当前条目是最后一个 增查询更多的数据
//                        if (lastVisiblePosition == pageBlackNumber.size() - 1) {
//                            pagenumber++;
//                            if (pagenumber * pagesize >= totalNumber) {
//                                Toast.makeText(SecurityPhoneActivity.this,
//                                        "没有更多的数据了", 0).show();
//                            } else {
//                                pageBlackNumber.addAll(dao.getPageBlackNumber(
//                                        pagenumber, pagesize));
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                        break;
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//
//            }
//        });
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.imgv_leftbtn:
//                finish();
//                break;
//            case R.id.btn_addblacknumber:
//                // 跳转至添加黑名单页面
//                startActivity(new Intent(this, AddBlackNumberActivity.class));
//                break;
//        }
//    }

}
