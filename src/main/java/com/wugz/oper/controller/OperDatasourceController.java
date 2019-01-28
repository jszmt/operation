package com.wugz.oper.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.wugz.common.controller.BaseController;
import com.wugz.common.utils.PageUtils;
import com.wugz.common.utils.Query;
import com.wugz.common.utils.R;
import com.wugz.common.utils.ShiroUtils;
import com.wugz.oper.domain.OperDatasource;
import com.wugz.oper.service.OperDatasourceService;
import com.wugz.oper.utils.DBUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 数据源配置表
 * 
 * @author wugz
 * @email 1019036248@qq.com
 * @date 2018-12-05 10:26:53
 */
@Controller
@RequestMapping("/oper/datasource")
public class OperDatasourceController extends BaseController {
	@Autowired
	private OperDatasourceService operDatasourceService;

	private String prefix="oper/datasource";

	@RequiresPermissions("oper:datasource:datasource")
	@GetMapping("")
	String page(Model model) {
		return prefix + "/list";
	}

	@GetMapping("/list")
	@ResponseBody
	PageUtils list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params,true);
		List<OperDatasource> sysUserList = operDatasourceService.list(query);
		int total = operDatasourceService.count(query);
		PageUtils pageUtil = new PageUtils(sysUserList, total);
		return pageUtil;
	}

	/**
	 * 保存
	 */
	@RequestMapping("/add")
	@RequiresPermissions("oper:datasource:add")
	public String add(){
		return prefix + "/add";
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@ResponseBody
	public R save(OperDatasource operDatasource){
		String url = getDataSourceURL(operDatasource);
		Date now = DateUtil.parseDateTime(DateUtil.now());
		String userId = ShiroUtils.getUserId().toString();
		operDatasource.setUpdator(userId);
		operDatasource.setCrtTime(now);
		operDatasource.setUrl(url);
		operDatasource.setCreator(userId);
		operDatasource.setCrtTime(now);
		operDatasourceService.insert(operDatasource);
		return R.ok();
	}
	
	/**
	 * 修改页面
	 */
	@RequestMapping("/edit/{id}")
	@RequiresPermissions("oper:datasource:edit")
	public String edit(@PathVariable("id") Integer id, Model model){
		OperDatasource dt = operDatasourceService.selectById(id);
		model.addAttribute("datasource", dt);
		return prefix + "/edit";
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@ResponseBody
	public R update(OperDatasource operDatasource){
		String url = getDataSourceURL(operDatasource);
		operDatasource.setUpdator(ShiroUtils.getUserId().toString());
		operDatasource.setCrtTime(DateUtil.parseDateTime(DateUtil.now()));
		operDatasource.setUrl(url);
		operDatasourceService.update(operDatasource);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete/{id}")
	@ResponseBody
	@RequiresPermissions("oper:datasource:delete")
	public R delete(@PathVariable("id") Integer id){
		operDatasourceService.delete(id);
		return R.ok();
	}

	/**
	 * 判断是否是该用户自己添加的数据源
	 */
	@RequestMapping("/auth/{id}")
	@ResponseBody
	public R auth(@PathVariable("id") Integer id){
		OperDatasource dt = operDatasourceService.selectById(id);
		if(!dt.getCreator().equals(ShiroUtils.getUserId().toString())){
			return R.error("没有权限修改该条记录");
		}
		return R.ok();
	}

	/**
	 * 判断是否是该用户自己添加的数据源
	 */
	@RequestMapping("/connection")
	@ResponseBody
	public R connection(OperDatasource operDatasource){
		String type  = operDatasource.getType();
		if(StrUtil.isBlank(type)){
			return R.error("数据库类型为空");
		}
		String ip = operDatasource.getIp();
		if(StrUtil.isBlank(ip)){
			return R.error("ip为空");
		}
		String port = operDatasource.getPort();
		if(StrUtil.isBlank(port)){
			return R.error("端口为空");
		}
		String databaseName = operDatasource.getDatabaseName();
		if(StrUtil.isBlank(databaseName)){
			return R.error("数据库为空");
		}
		String username = operDatasource.getUsername();
		if(StrUtil.isBlank(username)){
			return R.error("用户名为空");
		}
		String password = operDatasource.getPassword();
		if(StrUtil.isBlank(password)){
			return R.error("密码为空");
		}
		String url = getDataSourceURL(operDatasource);
		Connection conn = null;
		try {
			conn = DBUtils.getConnection(type,url,username,password,true);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("连接异常");
		} finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return R.ok("连接成功");
	}

	/**
	 * 判断是否是该用户自己添加的数据源
	 */
	@RequestMapping("/connection/{id}")
	@ResponseBody
	public Boolean connection(@PathVariable Integer id){
		OperDatasource operDatasource = operDatasourceService.selectById(id);
		String type  = operDatasource.getType();
		String username = operDatasource.getUsername();
		String password = operDatasource.getPassword();
		String url = getDataSourceURL(operDatasource);

		Connection conn = null;
		try {
			conn  = DBUtils.getConnection(type,url,username,password,true);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	/**
	 * @author wuguizhen
	 * @Description 获取数据库连接
	 * @date 2018/12/10 17:29
	 * @param datasourceId
	 * @return java.sql.Connection
	 */
	public Connection getConnection(Integer datasourceId,boolean autoCommit){
		OperDatasource operDatasource = operDatasourceService.selectById(datasourceId);
		String type  = operDatasource.getType();
		String username = operDatasource.getUsername();
		String password = operDatasource.getPassword();
		String url = getDataSourceURL(operDatasource);
		Connection conn = null;
		try {
			conn  = DBUtils.getConnection(type,url,username,password,autoCommit);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return conn;
	}


	private String getDataSourceURL(OperDatasource operDatasource) {
		StringBuilder url = new StringBuilder();
		//mysql
		if(DBUtils.DATABASE_MYSQL.equals(operDatasource.getType())){
			url.append("jdbc:mysql://")
					.append(operDatasource.getIp())
					.append(":")
					.append(operDatasource.getPort())
					.append("/")
					.append(operDatasource.getDatabaseName())
					.append("?characterEncoding=utf8");
		//oracle
		}else if(DBUtils.DATABASE_ORACLE.equals(operDatasource.getType())){
			url.append("jdbc:oracle:thin:@//")
					.append(operDatasource.getIp())
					.append(":")
					.append(operDatasource.getPort())
					.append("/")
					.append(operDatasource.getDatabaseName());
		}else{
			System.out.println("不支持的数据类型："+operDatasource.getType());
		}
		return url.toString();
	}
	
}
