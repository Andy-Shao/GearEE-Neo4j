package org.example.neo4j.domain;

import java.io.Serializable;

import org.example.neo4j.domain.vo.ApiMatchStatus;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class ApiMatch implements Serializable{
    private String apiMatchName;
    private String apiMatchDescription;
    private String pattern;
    private String path;
    private String connectStr;
    private String model;
    private Integer matchOrder;
    private Integer timeOut;
    private Boolean isIdempotent;
    private ApiMatchStatus status;
    private AuditRecord auditRecord;
}
