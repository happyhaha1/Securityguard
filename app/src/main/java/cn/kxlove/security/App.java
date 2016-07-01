package cn.kxlove.security;

import android.app.Application;

import org.xutils.x;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author: zhengkaixin
 * @date: 16/6/25
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
