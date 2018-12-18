package com.wugz.oper.domain;

import java.io.Serializable;
import java.util.Date;


/**
 * 数据源配置表
 * 
 * @author wugz
 * @email 1019036248@qq.com
 * @date 2018-12-05 10:26:53
 */
public class OperDatasource implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Integer id;
	//数据源名称
	private String name;
	//描述
	private String descripion;
	//数据源连接地址
	private String url;

	private String ip;
	private String port;
	private String databaseName;

	//连接用户名
	private String username;
	//连接密码
	private String password;
	//类型 mysql oracle  
	private String type;
	//创建时间
	private Date crtTime;
	//创建人
	private String creator;
	//修改时间
	private Date updTime;
	//修改人
	private String updator;
	//排序
	private Integer orderNum;
	//备注
	private String remark;

	private String opername;

	/**
	 * 设置：id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：数据源名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：数据源名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：描述
	 */
	public void setDescripion(String descripion) {
		this.descripion = descripion;
	}
	/**
	 * 获取：描述
	 */
	public String getDescripion() {
		return descripion;
	}
	/**
	 * 设置：数据源连接地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：数据源连接地址
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：连接用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：连接用户名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：连接密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：连接密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置：类型 mysql oracle  
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：类型 mysql oracle  
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCrtTime() {
		return crtTime;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建人
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdTime() {
		return updTime;
	}
	/**
	 * 设置：修改人
	 */
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	/**
	 * 获取：修改人
	 */
	public String getUpdator() {
		return updator;
	}
	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String database) {
		this.databaseName = database;
	}

	public String getOpername() {
		return opername;
	}

	public void setOpername(String opername) {
		this.opername = opername;
	}
}
