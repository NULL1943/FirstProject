package com.subway.dao;

import com.subway.db.SQLUtil;

/**
 * 保存微信用户的信息
 * 
 * @author NULL
 *
 */
public class StoreUserDao {
	
	/**
	 * pity / praise
	 */
	public void storeAUser(String openid) {
		SQLUtil.insertIntoTable("t_user", new String[]{"openid","time"}, new String[]{""});
		
		
	}

}
