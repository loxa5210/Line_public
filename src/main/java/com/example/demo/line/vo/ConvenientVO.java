package com.example.demo.line.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ConvenientVO {

	private String ParentId;
	private String EshopId;
	private String Url;
	private String OrderNo;
	private String PaymentNo;
	private String ValidationNo;
	private String Description;
	private String IsPayment;


	//	{"ParentId":"756","EshopId":"888",
//		"Url":"/C2C/Print","OrderNo":"20112505926463"
//		,"PaymentNo":"M5494324","ValidationNo":"5798"
//		,"Description":"","IsPayment":null}
}
