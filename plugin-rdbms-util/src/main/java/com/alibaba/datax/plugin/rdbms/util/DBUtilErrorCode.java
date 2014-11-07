package com.alibaba.datax.plugin.rdbms.util;

import com.alibaba.datax.common.spi.ErrorCode;

//TODO
public enum DBUtilErrorCode implements ErrorCode {
    CONN_DB_ERROR("DBUtilErrorCode-10", "连接数据库失败. 请检查您的 账号、密码、数据库名称、IP、Port或者向 DBA 寻求帮助(注意网络环境)."),
    JDBC_CONTAINS_BLANK_ERROR("DBUtilErrorCode-11", "jdbcUrl不得为空."),
    UNSUPPORTED_TYPE("DBUtilErrorCode-12", "不支持的数据库类型. 请注意查看 DataX 已经支持的数据库类型以及数据库版本."),
    COLUMN_SPLIT_ERROR("DBUtilErrorCode-13", "根据主键进行切分失败."),
    SET_SESSION_ERROR("DBUtilErrorCode-14", "设置 session 失败."),

    REQUIRED_VALUE("DBUtilErrorCode-00", "您缺失了必须填写的参数值."),
    ILLEGAL_VALUE("DBUtilErrorCode-02", "您填写的参数值不合法."),
    ILLEGAL_SPLIT_PK("DBUtilErrorCode-04", "您填写的主键列不合法, DataX 仅支持切分主键为一个,并且类型为整数或者字符串类型."),
    SQL_EXECUTE_FAIL("DBUtilErrorCode-06", "执行数据库 Sql 失败, 请检查您的配置的 column/table/where/querySql或者向 DBA 寻求帮助."),
    READ_RECORD_FAIL("DBUtilErrorCode-07", "读取数据看数据失败. 请检查您的配置的 column/table/where/querySql或者向 DBA 寻求帮助."),
    TABLE_QUERYSQL_MIXED("DBUtilErrorCode-08", "您配置凌乱了. 不能同时既配置table又配置querySql"),
    TABLE_QUERYSQL_MISSING("DBUtilErrorCode-09", "您配置错误. table和querySql 应该并且只能配置一个."),;

    private final String code;

    private final String description;

    private DBUtilErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return String.format("Code:[%s], Description:[%s]. ", this.code,
                this.description);
    }
}
