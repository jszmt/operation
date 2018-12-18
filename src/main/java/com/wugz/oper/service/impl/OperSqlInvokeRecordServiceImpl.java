package com.wugz.oper.service.impl;

import com.wugz.oper.dao.OperSqlInvokeRecordDao;
import com.wugz.oper.domain.OperSqlInvokeRecord;
import com.wugz.oper.service.OperSqlInvokeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class OperSqlInvokeRecordServiceImpl implements OperSqlInvokeRecordService {

	@Autowired
	private OperSqlInvokeRecordDao operSqlInvokeRecordDao;
	
	@Override
	public OperSqlInvokeRecord selectOneByObject(OperSqlInvokeRecord operSqlInvokeRecord){
		return operSqlInvokeRecordDao.selectOneByObject(operSqlInvokeRecord);
	}
	
	@Override
	public List<OperSqlInvokeRecord> selectListByObject(OperSqlInvokeRecord operSqlInvokeRecord){
		return operSqlInvokeRecordDao.selectListByObject(operSqlInvokeRecord);
	}
		
	@Override
	public void insert(OperSqlInvokeRecord operSqlInvokeRecord){
		operSqlInvokeRecordDao.insert(operSqlInvokeRecord);
	}
	
	@Override
	public void update(OperSqlInvokeRecord operSqlInvokeRecord){
		operSqlInvokeRecordDao.update(operSqlInvokeRecord);
	}

	@Override
	public List<OperSqlInvokeRecord> list(Map<String, Object> map) {
		return operSqlInvokeRecordDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return operSqlInvokeRecordDao.count(map);
	}

}
