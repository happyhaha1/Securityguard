package cn.kxlove.security.model;

import io.realm.RealmObject;

public class BlackContactInfo extends RealmObject{
	/**黑名单号码*/
	public String phoneNumber;
	/**黑名单联系人名称*/
	public String contactName;
	/**黑名单拦截模式</br>   1为电话拦截   2为短信拦截  3为电话、短信都拦截*/
	public int mode;

    public BlackContactInfo() {}

    public BlackContactInfo(String phoneNumber,String contactName,int mode) {
        this.phoneNumber = phoneNumber;
        this.contactName = contactName;
        this.mode = mode;
    }

	public String getModeString(int mode){
		switch (mode) {
			case 1:
				return "电话拦截";
			case 2:
				return "短信拦截";
			case 3:
				return "电话、短信拦截";
		}
		return "";
	}
}
