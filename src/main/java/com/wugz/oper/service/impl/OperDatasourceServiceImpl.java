package com.wugz.oper.service.impl;

import com.wugz.oper.dao.OperDatasourceDao;
import com.wugz.oper.domain.OperDatasource;
import com.wugz.oper.service.OperDatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class OperDatasourceServiceImpl implements OperDatasourceService {

	@Autowired
	private OperDatasourceDao operDatasourceDao;

	@Override
	public OperDatasource selectById(Integer id) {
		if(id == null){
			return null;
		}
		OperDatasource dt = new OperDatasource();
		dt.setId(id);
		return operDatasourceDao.selectOneByObject(dt);
	}

	@Override
	public OperDatasource selectOneByObject(OperDatasource operDatasource){
		return operDatasourceDao.selectOneByObject(operDatasource);
	}
	
	@Override
	public List<OperDatasource> selectListByObject(OperDatasource operDatasource){
		return operDatasourceDao.selectListByObject(operDatasource);
	}
		
	@Override
	public void insert(OperDatasource operDatasource){
		operDatasourceDao.insert(operDatasource);
	}
	
	@Override
	public void update(OperDatasource operDatasource){
		operDatasourceDao.update(operDatasource);
	}

	@Override
	public List<OperDatasource> list(Map<String, Object> map) {

		return operDatasourceDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return operDatasourceDao.count(map);
	}

	@Override
	public int delete(Integer id) {
		return operDatasourceDao.delete(id);
	}

}
