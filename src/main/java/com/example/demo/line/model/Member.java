package com.example.demo.line.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Member {

	private Integer id;

	private String account;

	private String password;

	private String userName;

}
