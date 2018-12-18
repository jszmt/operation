package com.wugz.oper.dao;

import com.wugz.oper.domain.OperDatasource;

import java.util.List;
import java.util.Map;

/**
 * 数据源配置表
 * 
 * @author wugz
 * @email 1019036248@qq.com
 * @date 2018-12-05 10:26:53
 */
public interface OperDatasourceDao {

    OperDatasource selectOneByObject(OperDatasource OperDatasource);
	
	List<OperDatasource> selectListByObject(OperDatasource OperDatasource);
		
	void insert(OperDatasource operDatasource);
	
	void update(OperDatasource operDatasource);

	List<OperDatasource> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int delete(Integer id);
}
