package cn.kxlove.security.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.kxlove.security.R;
import cn.kxlove.security.activity.base.BaseActivity;
import cn.kxlove.security.adapter.HomeAdapter;
import cn.kxlove.security.dialog.InterPasswordDialog;
import cn.kxlove.security.dialog.SetUpPasswordDialog;
import cn.kxlove.security.utils.MD5Utils;

@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity {

    @ViewInject(R.id.gv_home)
    private GridView gridView;
    private long mExitTime;

    //存储设置文件
    private SharedPreferences msharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//        x.view().inject(this);
        msharedPreferences = getSharedPreferences("config",MODE_PRIVATE);

        //填充GridView
        gridView.setAdapter(new HomeAdapter(HomeActivity.this));
        //单击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://手机防盗
                        if(isSetUpPassword()){
                            showInterPswdDialog();
                        }else{
                            showSetUpPswdDialog();
                        }
                        break;
                    case 1://通讯卫士
                        break;
                    case 2://软件管家
                        break;
                    case 3://病毒查杀
                        break;
                    case 4://缓存清理
                        break;
                    case 5://进程管理
                        break;
                    case 6://流量统计
                        break;
                    case 7://高级工具
                        break;
                    case 8://设置中心
                        break;
                }
            }
        });

    }

    /**
     *
     * 设置密码对话框
     */

    private void showSetUpPswdDialog(){
        final SetUpPasswordDialog setUpPasswordDialog = new SetUpPasswordDialog(HomeActivity.this);
        setUpPasswordDialog.setCallBack(new SetUpPasswordDialog.MyCallBack(){
            @Override
            public void ok() {
                String firstPwsd = setUpPasswordDialog.mFirstPWDET
                        .getText().toString().trim();
                String affirmPwsd = setUpPasswordDialog.mAffirmET
                        .getText().toString().trim();
                if (!TextUtils.isEmpty(firstPwsd)
                        && !TextUtils.isEmpty(affirmPwsd)) {
                    if (firstPwsd.equals(affirmPwsd)) {
                        // 两次密码一致,存储密码
                        savePswd(affirmPwsd);
                        setUpPasswordDialog.dismiss();
                        // 显示输入密码对话框
                        showInterPswdDialog();
                    } else {
                        Toast.makeText(HomeActivity.this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancel() {
                setUpPasswordDialog.dismiss();
            }
        });
        setUpPasswordDialog.setCancelable(true);
        setUpPasswordDialog.show();
    }

    /**
     * 输入密码对话框
     */
    private void showInterPswdDialog(){
        final String password = getPassword();
        final InterPasswordDialog mInterPasswordDialog =  new InterPasswordDialog(HomeActivity.this);

        mInterPasswordDialog.setCallBack(new InterPasswordDialog.MyCallBack() {
            @Override
            public void confirm() {
                if(TextUtils.isEmpty(mInterPasswordDialog.getPassword())){
                    Toast.makeText(HomeActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(password.equals(MD5Utils.encode(mInterPasswordDialog.getPassword()))){
                    //进入防盗界面
                    Toast.makeText(HomeActivity.this,"成功进入防盗界面",Toast.LENGTH_SHORT).show();
                }else{
                    mInterPasswordDialog.dismiss();
                    Toast.makeText(HomeActivity.this,"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void dismiss() {
                mInterPasswordDialog.dismiss();
            }
        });

        mInterPasswordDialog.setCancelable(true);
        mInterPasswordDialog.show();
    }

    /**
     * 保存密码
     */

    private void savePswd(String affirmPwd){
        SharedPreferences.Editor editor = msharedPreferences.edit();
        editor.putString("PhoneAntiTheftPWD", MD5Utils.encode(affirmPwd));
        editor.apply();
    }

    /**
     * 获取密码
     * @return 密码
     */
    private String getPassword(){
        String password  = msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if(TextUtils.isEmpty(password))
            return "";
        return password;
    }

    //判断是否设置过密码
    private boolean isSetUpPassword(){
        String password = msharedPreferences.getString("PhoneAntiTheftPWD", null);
        return !TextUtils.isEmpty(password);
    }

    //按两次返回键退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis()-mExitTime)<2000){
                System.exit(0);
            }else{
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                mExitTime=System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
