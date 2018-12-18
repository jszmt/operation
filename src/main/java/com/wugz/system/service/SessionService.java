package com.wugz.system.service;

import java.util.Collection;
import java.util.List;

import com.wugz.system.domain.UserDO;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import com.wugz.system.domain.UserOnline;

@Service
public interface SessionService {
	List<UserOnline> list();

	List<UserDO> listOnlineUser();

	Collection<Session> sessionList();
	
	boolean forceLogout(String sessionId);
}
