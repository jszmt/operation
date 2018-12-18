package com.wugz.oper.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wugz.common.utils.R;
import com.wugz.oper.domain.OperSqlInvokeRecord;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.update.Update;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName SQLTools
 * @Description TODO
 * @Author wuguizhen
 * @Date 2018/12/10 16:01
 */
public class SQLTools {

    public static final String SELECT = "select";

    public static final String UPDATE = "update";

    public static final String DELETE = "delete";

    public static final String INSERT = "insert";

    public static void main(String[] args) {

    }

    /**
     * @author wuguizhen
     * @Description 校验sql正确性
     * @date 2018/12/10 16:03
     * @param content
     * @return boolean
     */
    public static R verify(String content, String supportType) {
        R r = R.ok("correct");
        try {
            //查询的验证
            if(supportType.contains(SELECT)){
                Select select =  (Select)CCJSqlParserUtil.parse(content);
                SelectBody selectBody = select.getSelectBody();
                PlainSelect plainSelect = (PlainSelect)selectBody;
                Expression where = plainSelect.getWhere();
                if(where == null){
                    return R.error("请输入查询条件！");
                }else{
                    //防止 where 1=1 内中情况
                   if(where instanceof EqualsTo){
                       EqualsTo equalsTo = (EqualsTo) where;
                       if(equalsTo.getLeftExpression().getClass().toString().contains("Value") &&
                               equalsTo.getRightExpression().getClass().toString().contains("Value")){
                           return R.error("不支持全表查询");
                       }
                   }
                }
            }else{
                //增删改sql操作
                Statements statements = CCJSqlParserUtil.parseStatements(content);
                List<Statement> list = statements.getStatements();
                if(list.size() > 100){
                    return R.error("不支持超过100条sql的操作");
                }
                Statement first = list.get(0);
                //第一条语句的操作类型
                String firstStatementtype = getLowerCaseClassName(first.getClass().toString());
                if(!supportType.contains(firstStatementtype)){
                    return R.error("不支持的操作类型："+firstStatementtype);
                }
                //第一条语句操作的table
                List<String> firstOpTables = getOperTable(first,firstStatementtype);

                if(firstOpTables.size() > 1){
                    return R.error("操作表超过一个");
                }

                for(Statement statement:list){
                    String statementType = getLowerCaseClassName(statement.getClass().toString());
                    if(!statementType.equals(firstStatementtype)){
                        return R.error("操作类型超过一种");
                    }
                    List<String> opTables = getOperTable(statement,statementType);
                    if(opTables.size() > 1){
                        return R.error("操作表超过一个");
                    }else if(!firstOpTables.get(0).equals(opTables.get(0))){
                        return R.error("批量操作表不一致");
                    }
                    if(statement instanceof Delete && ((Delete) statement).getWhere() == null){
                        return R.error("delete语句请输入where条件");
                    }
                }
                r.put("type",firstStatementtype);
                r.put("sqls",list.stream().map(Statement::toString).collect(Collectors.toList()));
            }
        } catch (JSQLParserException e) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(baos));
            String exception = baos.toString();
            String regEx = "Encountered(.*)";
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(exception);
            while (mat.find()) {
                exception = mat.group(1);
            }
            return R.error(exception);
        }
        return r;
    }
    
    /**
     * @author wuguizhen
     * @Description 解析insert 语句
     * @date 2018/12/11 17:43
     * @param content, record
     * @return void
     */
    public static JSONArray paserInsert(String content, OperSqlInvokeRecord record) throws JSQLParserException {
        String operTable = "";
        JSONArray operAfter = new JSONArray();
        Statements statements = CCJSqlParserUtil.parseStatements(content);
        List<Statement> list = statements.getStatements();
        operTable = getOperTable(list.get(0),INSERT).get(0);
        record.setOperTables(operTable);
        list.stream().forEach(statement -> {
            Insert insert = (Insert) statement;
            List<Column> columns = insert.getColumns();
            List<Expression> expressions = ((ExpressionList) insert
                    .getItemsList()).getExpressions();
            JSONObject obj = new JSONObject();
            for(int i=0;i<columns.size();i++){
                obj.put(columns.get(i).toString(),expressions.get(i).toString());
            }
            operAfter.add(obj);
        });
        record.setOperAfter(operAfter.toJSONString());
        return operAfter;
    }
    
    /**
     * @author wuguizhen
     * @Description 解析delete语句
     * @date 2018/12/11 19:06
     * @param content, record, connection
     * @return java.lang.Object
     */
    public static Object paserDelete(String content, OperSqlInvokeRecord record, Connection connection) throws Exception {
        List<Map<String,String>> list = new ArrayList<>();
        Statements statements = CCJSqlParserUtil.parseStatements(content);
        List<Statement> statementList = statements.getStatements();
        final String operTable = getOperTable(statementList.get(0),DELETE).get(0);
        record.setOperTables(operTable);
        record.setOperWhere(((Delete)statementList.get(0)).getWhere().toString());
        for(Statement statement:statementList){
            Delete delete = (Delete) statement;
            String sql = "select * from "+ operTable + " where " + delete.getWhere().toString();
            list.addAll(DBUtils.query(connection,sql));
        }
        record.setOperBefore(JSON.toJSONString(list));
        return list;
    }

    /**
     * @author wuguizhen
     * @Description 解析update语句
     * @date 2018/12/11 19:23
     * @param content, record, connection
     * @return java.lang.Object
     */
    public static Object paserUpdate(String content, OperSqlInvokeRecord record, Connection connection) throws Exception {
        List<Map<String,String>> operBefore = new ArrayList<>();
        Map<String,Object> operAfter = new HashMap<>();
        Statements statements = CCJSqlParserUtil.parseStatements(content);
        List<Statement> statementList = statements.getStatements();
        String operTables = getOperTable(statementList.get(0),UPDATE).get(0);
        for(Statement statement:statementList){
            Update update = (Update) statement;
            String operTable = getOperTable(statement,UPDATE).get(0);
            //如果update的表不一致 追加到operTables
            if(operTables.contains(",")){
                if(Arrays.asList(operTables.split(",")).contains(operTable)){
                    operTables += operTable + ",";
                }
            }else if(!operTables.equals(operTable)){
                operTables += operTable + ",";
            }
            //update修改的 列
            List<String> columns = update.getColumns().stream().map(Column::toString).collect(Collectors.toList());
            //update修改的值 顺序与列一致
            List<String> values = update.getExpressions().stream().map(Expression::toString).collect(Collectors.toList());
            for(int i=0;i<columns.size();i++){
                operAfter.put(columns.get(i),values.get(i));
            }
            String operBeforeSQl = "select * from " + operTable + " where " + update.getWhere().toString();
            operBefore.addAll(DBUtils.query(connection,operBeforeSQl));
        }
        if(operTables.contains(",")){
            operTables = operTables.substring(0,operTables.length()-1);
        }
        record.setOperTables(operTables);
        record.setOperBefore(JSON.toJSONString(operBefore));
        record.setOperAfter(JSON.toJSONString(operAfter));
        return operBefore;
    }

    /**
     * @author wuguizhen
     * @Description 获取操作表
     * @date 2018/12/11 17:40
     * @param statement, statementType
     * @return java.util.List<java.lang.String>
     */
    private static List<String> getOperTable(Statement statement, String statementType) {
        List<String> firstOpTables = new ArrayList<>();
        if(UPDATE.equals(statementType)){
            Update update = (Update) statement;
            firstOpTables = update.getTables().stream().map(Table::toString).collect(Collectors.toList());
        }else if(DELETE.equals(statementType)){
            Delete delete = (Delete) statement;
            firstOpTables.add(delete.getTable().toString());
        }else if(INSERT.equals(statementType)){
            Insert insert = (Insert) statement;
            firstOpTables.add(insert.getTable().toString());
        }
        return firstOpTables;
    }

    /**
     * @author wuguizhen
     * @Description  class net.sf.jsqlparser.statement.update.Update 获取类名
     * @date 2018/12/11 15:31
     * @param className
     * @return java.lang.String
     */
    private static String getLowerCaseClassName(String className){
        String[] info = className.split("\\.");
        String realClassName = info[info.length-1];
        return realClassName.toLowerCase();
    }
}
