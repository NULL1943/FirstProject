package com.subway.bean;

/**
 * 地铁站出口查询结果实体类
 * @author NULL
 *
 */
public class StationExitResult {
	
	public static final int STATUS_SUCC = 1;
	public static final int STATUS_FAIL = 0;

	private int status;
	private String title;
	private String describe;
	private String walkRoute;
	
	private int recommend;// 是否有推荐站点，1-是，0-否
	
	private String title2;
	private String describe2;
	private String walkRoute2;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescribe() {
		return describe;
	}
	public String getWalkRoute() {
		return walkRoute;
	}
	public void setWalkRoute(String walkRoute) {
		this.walkRoute = walkRoute;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getTitle2() {
		return title2;
	}
	public void setTitle2(String title2) {
		this.title2 = title2;
	}
	public String getDescribe2() {
		return describe2;
	}
	public void setDescribe2(String describe2) {
		this.describe2 = describe2;
	}
	public String getWalkRoute2() {
		return walkRoute2;
	}
	public void setWalkRoute2(String walkRoute2) {
		this.walkRoute2 = walkRoute2;
	}
	public int getRecommend() {
		return recommend;
	}
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	
	
}
