package com.wugz.oper.controller;

import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.system.UserInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wugz.common.utils.PageUtils;
import com.wugz.common.utils.Query;
import com.wugz.common.utils.ShiroUtils;
import com.wugz.oper.domain.OperSqlInvokeRecord;
import com.wugz.oper.service.OperSqlInvokeRecordService;
import com.wugz.oper.utils.SQLTools;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @ClassName OperHistoryController
 * @Description TODO
 * @Author wuguizhen
 * @Date 2018/12/13 9:55
 */
@RestController
@RequestMapping("/oper/history")
public class OperHistoryController {

    @Autowired
    private OperSqlInvokeRecordService operSqlInvokeRecordService;


    @GetMapping("/list")
    @ResponseBody
    public PageUtils list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params,true);
        query.put("creator",ShiroUtils.getUserId());
        List<OperSqlInvokeRecord> sysUserList = operSqlInvokeRecordService.list(query);
        int total = operSqlInvokeRecordService.count(query);
        PageUtils pageUtil = new PageUtils(sysUserList, total);
        return pageUtil;
    }

    /**
     * @author wuguizhen
     * @Description 备份数据下载
     * @date 2018/12/13 14:31
     * @param id, response
     * @return void
     */
    @RequestMapping("/backup")
    public void  backup(String id,HttpServletResponse response){
        OperSqlInvokeRecord record = new OperSqlInvokeRecord();
        record.setId(id);
        record = operSqlInvokeRecordService.selectOneByObject(record);
        String type = record.getType();
        String msg = "";
        //新增时 导出操作后的数据 删除修改导出操作之前的数据
        if(SQLTools.INSERT.equals(type)){
            msg = record.getOperAfter();
        }else{
            msg = record.getOperBefore();
        }
        String fileName = new UserInfo().getCurrentDir()+File.separator+"file"+File.separator+id+".xlsx";
        List<Map> fileMsg =  JSONObject.parseArray(msg,Map.class);
        ExcelWriter writer = new ExcelWriter(fileName) ;
        writer.write(fileMsg);
        writer.flush();
        try (InputStream inputStream = new FileInputStream(fileName);
             OutputStream outputStream = response.getOutputStream()
        ) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;fileName="+id+".xlsx");
            //把输入流copy到输出流
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/operAfter")
    @ResponseBody
    public Object operAfter(String id){
        OperSqlInvokeRecord record = new OperSqlInvokeRecord();
        record.setId(id);
        record = operSqlInvokeRecordService.selectOneByObject(record);
        String operAfter = record.getOperAfter();

        operAfter = operAfter.replaceAll("`","");
        if(SQLTools.INSERT.equals(record.getType())){
            return JSON.parseArray(operAfter,Map.class);
        }
        JSONArray array = new JSONArray();
        array.add(JSONObject.parse(operAfter));
        return array;
    }

    @RequestMapping("/operBefore")
    @ResponseBody
    public Object operBefore(String id){
        OperSqlInvokeRecord record = new OperSqlInvokeRecord();
        record.setId(id);
        record = operSqlInvokeRecordService.selectOneByObject(record);
        String operBefore = record.getOperBefore();
        operBefore = operBefore.replaceAll("`","");
        return JSON.parseArray(operBefore,Map.class);
    }
}
