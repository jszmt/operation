package com.wugz.oper.common;

import cn.hutool.core.date.DateUtil;
import com.wugz.oper.domain.OperSqlInvokeRecord;
import com.wugz.oper.service.OperSqlInvokeRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName TransactionHandle
 * @Description 事务操作
 * @Author wuguizhen
 * @Date 2018/12/11 13:45
 */
@Component
public class TransactionHandle {

    public static final String COMMIT = "commit";
    public static final String ROLL_BACK = "rollback";
    public static final String EXECUTE = "execute";

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionHandle.class);

    @Autowired
    private OperSqlInvokeRecordService operSqlInvokeRecordService;

    private final Map<String,Connection> conCache = new ConcurrentHashMap<>();

    private final String split_symbol = "\\|";

    private final Long verifyTime = 600L; //600秒

    public static void main(String[] args) {
        long a = DateUtil.currentSeconds();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long b = DateUtil.currentSeconds();
        System.out.println(b-a);
    }

    /**
     * @author wuguizhen
     * @Description 将connection 缓存起来 ，等待提交
     * @date 2018/12/11 13:48
     * @param id, con
     * @return void
     */
    public void setTransactionCache(String id, Connection con){
        //获取当前时间戳
        long currentSeconds = DateUtil.currentSeconds();
        String key = currentSeconds + "|" + id;
        conCache.put(key,con);
    }
    
    /**
     * @author wuguizhen
     * @Description 销毁过期连接
     * @date 2018/12/11 14:23
     * @param
     * @return void
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void destroyOverdueCon(){
        LOGGER.info("定时清理connection。。");
        for(Map.Entry<String,Connection> entry:conCache.entrySet()){
            String key = entry.getKey();
            String cacheTime = key.split(split_symbol)[0];
            long betweenMs = DateUtil.currentSeconds()-Long.valueOf(cacheTime);
            if(betweenMs > verifyTime){
                Connection con = entry.getValue();
                if(con != null){
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                conCache.remove(key);
            }
        }
    }

    /**
     * 提交事务
     * @param id
     * @return
     */
    public boolean commit(String id,String applCde,String remark){
        Connection con = null;
        try{
            con = getCon(id);
            if(con != null){
                con.commit();
                alterTableStatus(id,COMMIT,applCde,remark);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 提交事务
     * @param id
     * @return
     */
    public boolean rollBack(String id){
        Connection con = null;
        try{
            con = getCon(id);
            if(con != null){
                con.rollback();
                alterTableStatus(id,ROLL_BACK,null,null);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    
    /**
     * @author wuguizhen
     * @Description 修改操作记录表状态
     * @date 2018/12/11 14:05
     * @param id, status
     * @return void
     */
    public void alterTableStatus(String id,String status,String applCde,String remark){
        OperSqlInvokeRecord record = new OperSqlInvokeRecord();
        record.setId(id);
        record.setStatus(status);
        record.setApplCde(applCde);
        record.setRemark(remark);
        operSqlInvokeRecordService.update(record);
    }

    /**
     * 获取Connection
     * @param id
     * @return
     */
    private Connection getCon(String id){
        for(Map.Entry<String,Connection> entry:conCache.entrySet()){
            String key = entry.getKey();
            String cacheId = key.split(split_symbol)[1];
            if(id.equals(cacheId)){
                return entry.getValue();
            }
        }
        return null;
    }
}