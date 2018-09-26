package org.example.neo4j.domain;

import java.io.Serializable;

import org.example.neo4j.domain.vo.UserStatus;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class User implements Serializable{
    private String username;
    private String password;
    private UserStatus status;
    private AuditRecord auditRecord;
}
