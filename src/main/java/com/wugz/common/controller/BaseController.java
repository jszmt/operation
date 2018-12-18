package com.wugz.common.controller;

import org.springframework.stereotype.Controller;
import com.wugz.common.utils.ShiroUtils;
import com.wugz.system.domain.UserDO;

@Controller
public class BaseController {
	public UserDO getUser() {
		return ShiroUtils.getUser();
	}

	public Long getUserId() {
		return getUser().getUserId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}
}