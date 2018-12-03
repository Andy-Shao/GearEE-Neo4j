package org.example.neo4j.domain;

import java.io.Serializable;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class SystemInfo implements Serializable{
    private String systemAlias;
}
