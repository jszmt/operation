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
public class OperSqlInvokeRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private String id;
	//操作类型（select、update、insert、delete）
	private String type;
	//申请书编号
	private String applCde;
	//操作状态
	private String status;
	//数据源id
	private String datasourceId;
	//sql操作表多个表用,分割
	private String operTables;
	//sql操作字段；多个字段用，
	private String operColumns;
	//sql where条件
	private String operWhere;
	//sql 修改之前的值
	private String operBefore;
	//sql 修改之后的值
	private String operAfter;
	//sql 内容
	private String content;
	//创建时间
	private Date crtTime;
	//创建人
	private String creator;
	//备注
	private String remark;

	private String dataSourceName;

	/**
	 * 设置：id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：操作类型（select、update、insert、delete）
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：操作类型（select、update、insert、delete）
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：申请书编号
	 */
	public void setApplCde(String applCde) {
		this.applCde = applCde;
	}
	/**
	 * 获取：申请书编号
	 */
	public String getApplCde() {
		return applCde;
	}
	/**
	 * 设置：数据源id
	 */
	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}
	/**
	 * 获取：数据源id
	 */
	public String getDatasourceId() {
		return datasourceId;
	}
	/**
	 * 设置：sql操作表多个表用,分割
	 */
	public void setOperTables(String operTables) {
		this.operTables = operTables;
	}
	/**
	 * 获取：sql操作表多个表用,分割
	 */
	public String getOperTables() {
		return operTables;
	}
	/**
	 * 设置：sql操作字段；多个字段用，
	 */
	public void setOperColumns(String operColumns) {
		this.operColumns = operColumns;
	}
	/**
	 * 获取：sql操作字段；多个字段用，
	 */
	public String getOperColumns() {
		return operColumns;
	}
	/**
	 * 设置：sql where条件
	 */
	public void setOperWhere(String operWhere) {
		this.operWhere = operWhere;
	}
	/**
	 * 获取：sql where条件
	 */
	public String getOperWhere() {
		return operWhere;
	}
	/**
	 * 设置：sql 修改之前的值
	 */
	public void setOperBefore(String operBefore) {
		this.operBefore = operBefore;
	}
	/**
	 * 获取：sql 修改之前的值
	 */
	public String getOperBefore() {
		return operBefore;
	}
	/**
	 * 设置：sql 修改之后的值
	 */
	public void setOperAfter(String operAfter) {
		this.operAfter = operAfter;
	}
	/**
	 * 获取：sql 修改之后的值
	 */
	public String getOperAfter() {
		return operAfter;
	}
	/**
	 * 设置：sql 内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：sql 内容
	 */
	public String getContent() {
		return content;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
}
