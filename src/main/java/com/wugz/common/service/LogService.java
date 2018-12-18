package com.wugz.common.service;

import org.springframework.stereotype.Service;

import com.wugz.common.domain.LogDO;
import com.wugz.common.domain.PageDO;
import com.wugz.common.utils.Query;
@Service
public interface LogService {
	void save(LogDO logDO);
	PageDO<LogDO> queryList(Query query);
	int remove(Long id);
	int batchRemove(Long[] ids);
}
