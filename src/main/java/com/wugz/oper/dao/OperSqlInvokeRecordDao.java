package com.wugz.oper.dao;

import com.wugz.oper.domain.OperSqlInvokeRecord;

import java.util.List;
import java.util.Map;

/**
 * 数据源配置表
 * 
 * @author wugz
 * @email 1019036248@qq.com
 * @date 2018-12-05 10:26:53
 */
public interface OperSqlInvokeRecordDao {

    OperSqlInvokeRecord selectOneByObject(OperSqlInvokeRecord OperSqlInvokeRecord);
	
	List<OperSqlInvokeRecord> selectListByObject(OperSqlInvokeRecord OperSqlInvokeRecord);
		
	void insert(OperSqlInvokeRecord operSqlInvokeRecord);
	
	void update(OperSqlInvokeRecord operSqlInvokeRecord);

	List<OperSqlInvokeRecord> list(Map<String, Object> map);

	int count(Map<String, Object> map);
}
