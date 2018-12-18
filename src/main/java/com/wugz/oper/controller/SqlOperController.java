package com.wugz.oper.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.wugz.common.utils.R;
import com.wugz.common.utils.ShiroUtils;
import com.wugz.oper.common.TransactionHandle;
import com.wugz.oper.domain.OperSqlInvokeRecord;
import com.wugz.oper.service.OperSqlInvokeRecordService;
import com.wugz.oper.utils.DBUtils;
import com.wugz.oper.utils.SQLTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName SqlOperController
 * @Description sql操作
 * @Author wuguizhen
 * @Date 2018/12/10 15:07
 */
@RestController
@RequestMapping("/oper/sql")
public class SqlOperController {

    private String prefix="oper/sql";

    @Autowired
    private OperDatasourceController operDatasourceController;

    @Autowired
    private TransactionHandle transactionHandle;

    @Autowired
    private OperSqlInvokeRecordService operSqlInvokeRecordService;

    /**
     * 查询sql
     */
    @RequestMapping("/select")
    public R select(Integer datasourceId,String content){
        R r = SQLTools.verify(content,SQLTools.SELECT);
        if(!"0".equals(r.get("code").toString())){
            return r;
        }
        Connection connection = operDatasourceController.getConnection(datasourceId,true);
        if(connection == null){
            return R.error("数据源连接失败");
        }
        try {
            r.put("data",DBUtils.query(connection,content));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("查询失败,原因："+e.getMessage());
        } finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return r;
    }

    /**
     * 回滚数据变更操作
     */
    @RequestMapping("/change/rollback")
    public Object rollback(String operId){
        if(!transactionHandle.rollBack(operId)){
            return R.error("回滚失败");
        }
        return R.ok();
    }

    /**
     * 提交数据变更操作
     */
    @RequestMapping("/change/commit")
    public Object commit(String operId,String applCde,String remark){
        if(!transactionHandle.commit(operId,applCde,remark)){
            return R.error("提交失败");
        }
        return R.ok();
    }

    /**
     * 执行数据变更操作（只执行 不提交）
     */
    @RequestMapping("/change/execute")
    public Object execute(Integer datasourceId,String content)  {
        String checkType = SQLTools.UPDATE + "|" + SQLTools.DELETE + "|" + SQLTools.INSERT;
        R r = SQLTools.verify(content,checkType);
        if(!"0".equals(r.get("code").toString())){
            return r;
        }
        String operType = r.get("type").toString();
        List<String> sqls = (List<String>)r.get("sqls");
        Connection connection = operDatasourceController.getConnection(datasourceId,false);
        if(connection == null){
            return R.error("数据源连接失败");
        }
        boolean isException = false;
        try {
            int num = DBUtils.batchExecute(connection,sqls);
            if(num > 100){
                return R.error(operType+"操作影响函数大于100");
            }
            packagingData(r,content,operType,datasourceId);
        } catch (SQLException e) {
            e.printStackTrace();
            isException = true;
            return R.error("执行失败:"+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            isException = true;
            return R.error("执行失败:"+e.getMessage());
        } finally {
            if(isException){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        String operId = r.get("operId").toString();
        transactionHandle.setTransactionCache(operId,connection);
        return r;
    }
    
   /**
    * @author wuguizhen
    * @Description 解析sql
    * @date 2018/12/11 17:22
    * @param r, mainCon(用来增删改的连接), content, operType
    * @return java.lang.String
    */
    private void packagingData(R r,String content,String operType,Integer datasourceId) throws Exception {
        //用于解析sql时查询的连接
        Connection queryCon = null;
        try{
            queryCon = operDatasourceController.getConnection(datasourceId,true);
            OperSqlInvokeRecord record = new OperSqlInvokeRecord();
            String uuid = UUID.fastUUID().toString();
            record.setId(uuid);
            record.setContent(JSON.toJSONString(content));
            record.setType(operType);
            record.setCreator(ShiroUtils.getUserId().toString());
            record.setCrtTime(DateUtil.parseDateTime(DateUtil.now()));
            if(SQLTools.INSERT.equals(operType)){
                r.put("data",SQLTools.paserInsert(content,record));
            }else if(SQLTools.DELETE.equals(operType)){
                r.put("data",SQLTools.paserDelete(content,record,queryCon));
            }else if(SQLTools.UPDATE.equals(operType)){
                r.put("data",SQLTools.paserUpdate(content,record,queryCon));
            }
            r.put("operId",uuid);
            r.put("operTable",record.getOperTables());
            record.setStatus(TransactionHandle.EXECUTE);
            record.setDatasourceId(datasourceId.toString());
            operSqlInvokeRecordService.insert(record);
        }catch (Exception e){
            throw e;
        }finally {
            if(queryCon !=null){
                queryCon.close();
            }
        }
    }

}
