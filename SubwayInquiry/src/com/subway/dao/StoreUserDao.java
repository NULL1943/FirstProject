package com.subway.dao;

import com.subway.db.SQLUtil;

/**
 * ����΢���û�����Ϣ
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
