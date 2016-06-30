package cn.kxlove.security.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.kxlove.security.R;
import cn.kxlove.security.adapter.HomeAdapter;

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
//                        if(isSetUpPassword()){
//                            showInterPasswordDialog();
//                        }else{
//                            showSetUpPasswordDialog();
//                        }
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
}
