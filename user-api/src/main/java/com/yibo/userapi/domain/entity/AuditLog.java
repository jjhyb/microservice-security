package com.yibo.userapi.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "audit_log")
public class AuditLog {
    /**
     * id主键
     */
    @Id
    private Integer id;

    /**
     * 请求的方式GET、POST等
     */
    private String method;

    /**
     * 请求路径
     */
    private String path;

    /**
     * http返回的状态码
     */
    private Integer status;

    /**
     * 请求发出的用户
     */
    private String username;

    /**
     * 审计日志的创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 审计日志的修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;
}