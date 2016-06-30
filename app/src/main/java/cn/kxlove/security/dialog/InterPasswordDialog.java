package cn.kxlove.security.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.kxlove.security.R;


/**
 * Created by QiWangming on 2015/11/10.
 * Blog: www.jycoder.com
 * GitHub: msAndroid
 */
public class InterPasswordDialog extends Dialog implements View.OnClickListener{


    private EditText mInterPWDET;
    private TextView mTitleTV;
    private MyCallBack myCallBack;



    public InterPasswordDialog(Context context) {
        super(context, R.style.dialog_custom);
//        this.context=context;
    }

    public void setCallBack(MyCallBack mycallBack){
        this.myCallBack = mycallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.inter_password_dialog);
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mTitleTV = (TextView) findViewById(R.id.tv_interpwd_title);
        mInterPWDET  = (EditText) findViewById(R.id.et_inter_password);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        findViewById(R.id.btn_dismiss).setOnClickListener(this);
    }

    public void setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            mTitleTV.setText(title);
        }
    }

    public String getPassword(){
        return mInterPWDET.getText().toString();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                myCallBack.confirm();
                break;
            case R.id.btn_dismiss:
                myCallBack.dismiss();
                break;
        }

    }

    public interface MyCallBack{
        void confirm();
        void dismiss();
    }
}
