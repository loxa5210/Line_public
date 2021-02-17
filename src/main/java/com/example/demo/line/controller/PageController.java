package com.example.demo.line.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping("/loginPage")
	public String getloginPage() {
		return "loginPage";

	}

	@GetMapping("/index")
	public String getindex() {
		return "index";

	}
}
