package com.wugz.oper.controller;

import com.wugz.oper.domain.OperDatasource;
import com.wugz.oper.domain.OperSqlInvokeRecord;
import com.wugz.oper.service.OperDatasourceService;
import com.wugz.oper.service.OperSqlInvokeRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * 跳转页面
 * 
 * @author wugz
 * @email 1019036248@qq.com
 * @date 2018-12-05 10:26:53
 */
@Controller
@RequestMapping("/oper/execute")
public class OperPageController {
	@Autowired
	private OperSqlInvokeRecordService operSqlInvokeRecordService;

	@Autowired
	private OperDatasourceService operDatasourceService;

	private String prefix="oper/execute";

	private final String select = "select";

	private final String change = "change";
	/**
	 * 查询页面
	 */
	@RequestMapping("/select")
	@RequiresPermissions("oper:execute:select")
	public String select(Model model){
		List<OperDatasource> list = operDatasourceService.selectListByObject(new OperDatasource());
		model.addAttribute("datasourceList",list);
		model.addAttribute("type",select);
		return prefix + "/choose";
	}

	/**
	 * 数据变更页面
	 */
	@RequestMapping("/change")
	@RequiresPermissions("oper:execute:change")
	public String change(Model model){
		List<OperDatasource> list = operDatasourceService.selectListByObject(new OperDatasource());
		model.addAttribute("datasourceList",list);
		model.addAttribute("type",change);
		return prefix + "/choose";
	}

	@RequestMapping("/jump")
	public String jump(Model model,String type,Integer datasourceId){
		model.addAttribute("datasourceId",datasourceId);
		if(select.equals(type)){
			return prefix + "/select";
		}
		return prefix + "/change";
	}

	@RequestMapping("/commit")
	public String commit(Model model,String operId){
		model.addAttribute("operId",operId);
		return prefix + "/commit";
	}

	@RequestMapping("/history")
	@RequiresPermissions("oper:execute:history")
	public String history(){
		return prefix + "/history/list";
	}

	@RequestMapping("/history/info")
	public String historyInfo(String id,Model model){
		OperSqlInvokeRecord record = new OperSqlInvokeRecord();
		record.setId(id);
		record = operSqlInvokeRecordService.selectOneByObject(record);
		model.addAttribute("record",record);
		return prefix + "/history/info";
	}
}
