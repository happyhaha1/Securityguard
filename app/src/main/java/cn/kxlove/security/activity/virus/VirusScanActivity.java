package cn.kxlove.security.activity.virus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseActivity;

@ContentView(R.layout.activity_virus_scan)
public class VirusScanActivity extends BaseActivity {

    private TextView mLastTimeTV;
    private SharedPreferences mSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        copyDB("antivirus.db");
        initView();
    }

    /**
     * 拷贝病毒数据库
     * @param dbname 数据库名字
     */
    private void copyDB(final String dbname) {
        new Thread(){
            public void run() {
                try {
                    File file = new File(getFilesDir(),dbname);
                    if(file.exists()&&file.length()>0){
                        Log.i("VirusScanActivity","数据库已存在！");
                        return ;
                    }
                    InputStream is = getAssets().open(dbname);
                    FileOutputStream fos  = openFileOutput(dbname, MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while((len = is.read(buffer))!=-1){
                        fos.write(buffer, 0, len);
                    }
                    is.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("病毒查杀");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_blue));
        setStatusBarColor(getResources().getColor(R.color.light_blue));
    }

    @Event(value = {R.id.imgv_leftbtn,R.id.rl_allscanvirus})
    private void onImgvClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.rl_allscanvirus:
                // 跳转至添加黑名单页面
//                startActivity(new Intent(this, AddBlackNumberActivity.class));
                break;
        }
    }

}
