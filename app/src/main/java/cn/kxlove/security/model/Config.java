package cn.kxlove.security.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author: zhengkaixin
 * @date: 16/7/1
 */

public class Config extends RealmObject {

    @PrimaryKey
    private int id;

    private String phoneAntiTheftPWD;

    public String getPhoneAntiTheftPWD() {
        return phoneAntiTheftPWD;
    }

    public void setPhoneAntiTheftPWD(String phoneAntiTheftPWD) {
        this.phoneAntiTheftPWD = phoneAntiTheftPWD;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
