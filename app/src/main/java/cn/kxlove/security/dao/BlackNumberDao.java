package cn.kxlove.security.dao;

import java.util.List;

import cn.kxlove.security.model.BlackContactInfo;
import io.realm.Realm;
import io.realm.RealmResults;

public class BlackNumberDao {

	private Realm realm = Realm.getDefaultInstance();

	/***
	 * 添加数据
	 *
	 */
	public boolean add(BlackContactInfo blackContactInfo) {
		realm.beginTransaction();
		realm.copyToRealm(blackContactInfo);
		realm.commitTransaction();
		return true;
	}

	/**
	 * 删除数据
	 *
	 */
	public boolean detele(BlackContactInfo blackContactInfo) {
		BlackContactInfo phoneNumber = realm.where(BlackContactInfo.class).equalTo("phoneNumber", blackContactInfo.phoneNumber).findFirst();
		realm.beginTransaction();
		phoneNumber.deleteFromRealm();
		realm.commitTransaction();
		return true;
	}

	/**
	 * 分页查询数据库的记录
	 * @param pagenumber,第几页页码 从第0页开始
	 * @param pagesize
	 *            每一个页面的大小
	 */
	public List<BlackContactInfo> getPageBlackNumber(int pagenumber,
													 int pagesize) {
        RealmResults<BlackContactInfo> all = realm.where(BlackContactInfo.class).findAll();
        if ((pagenumber+1)*pagesize > all.size()) {
            return all.subList(pagenumber*pagesize,all.size());
        }
        return all.subList(pagenumber*pagesize,(pagenumber+1)*pagesize);
	}

	/**
	 * 判断号码是否在黑名单中存在
	 *
	 */
	public boolean IsNumberExist(String number) {
		// 得到可读的数据库
		BlackContactInfo phoneNumber = realm.where(BlackContactInfo.class).equalTo("phoneNumber", number).findFirst();
		return phoneNumber != null;
	}

	/**
	 * 根据号码查询黑名单信息
	 *
	 * @param number
	 * @return
	 */
	public int getBlackContactMode(String number) {
        // 得到可读的数据库
        BlackContactInfo phoneNumber = realm.where(BlackContactInfo.class).equalTo("phoneNumber", number).findFirst();
        if (phoneNumber == null) {
            return 0;
        }
        return phoneNumber.mode;
    }

	/**
	 * 获取数据库的总条目个数
	 *
	 */
	public int getTotalNumber() {
		return realm.where(BlackContactInfo.class).findAll().size();
	}
}