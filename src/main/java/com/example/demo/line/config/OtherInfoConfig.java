package com.example.demo.line.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:otherInfo.properties" , encoding ="utf-8")
@Data
public class OtherInfoConfig {
	@Value("${oldest}")
	public String oldest;

	@Value("${oldestName}")
	public String oldestName;
	@Value("${secondName}")
	public String secondName;
	@Value("${second}")

	public String second;
	@Value("${youngestName}")
	public  String youngestName;
	@Value("${youngest}")
	public String youngest;
}
