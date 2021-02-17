package com.example.demo.line.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImgurResponse {

	private ImgurVO data;

	private Boolean success;

	private Integer status;

}
