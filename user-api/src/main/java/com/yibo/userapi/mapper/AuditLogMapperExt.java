package com.yibo.userapi.mapper;

import com.yibo.userapi.domain.entity.AuditLog;
import tk.mybatis.mapper.common.Mapper;

public interface AuditLogMapperExt extends Mapper<AuditLog> {

    //可以返回自增主键，插入成功之后直接在DO类的主键上获取，并返回数据库受影响的行数
    public int insertAuditLog(AuditLog auditLog);

    //可以返回自增主键，插入成功之后直接在DO类的主键上获取，并返回数据库受影响的行数
    public int addAuditLog(AuditLog auditLog);
}