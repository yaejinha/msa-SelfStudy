package com.example.snoopy.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.snoopy.demo.service.searchService;

@Controller
public class searchController {
	//노선 끊겼는지 검색해봄
	@Autowired
	private  searchService searchService;
	
	@GetMapping("/start/{apiAddr}/{strsch}")
	@ResponseBody
	public String searchDest(@PathVariable String apiAddr, @PathVariable String strsch) throws Exception {
		return searchService.askIsOver(apiAddr,strsch);  //getBusRouteList/153 이런꼴  
	}

}
