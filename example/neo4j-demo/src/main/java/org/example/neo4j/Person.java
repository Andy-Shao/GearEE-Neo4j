package org.example.neo4j;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("serial")
public class Person implements Serializable{
	private String name;
	private Integer age;
	private String phone;
}
