package com.example.snoopy.demo.service;

import java.io.StringReader;
import java.net.URLDecoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.example.snoopy.demo.ResttemplateFactory;
import com.example.snoopy.demo.data.BusInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@EnableCircuitBreaker  //circuit breaker
@Service 
public class searchService {
	// 안나는 엘사한테 리퀘스트만 쏜다. -- resttemplate으로 계속 말걸음
	@Autowired
	ResttemplateFactory rest;
	
	@Autowired
	BusInfo busInfo;
	
	@HystrixCommand(fallbackMethod = "fallback" , commandKey = "askLastTm",commandProperties = {
			   @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			   @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000") })

	public String askIsOver(String apiAddr, String strsch) throws Exception {
		
		String baseUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/"+apiAddr;
		
		String serviceKey = URLDecoder.decode("vGwcKfqgnc0ZJulU81%2FFu57h7ZgnjQHzZi3VRVJxRUPBSGiIKnLrJdolQaaDtGdp0rxVKCGJn6XXrb7W4hGUSQ%3D%3D", "UTF-8");   
		   
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(baseUrl+"?"+"serviceKey="+serviceKey+"&strSrch="+strsch).build();
		
		RestTemplate restTemplate = rest.restTemplate();
		
		System.out.println(baseUrl);
		
//	    try {    //fallback method 안썼을 경우에 try catch 예시 
		String apiResult = restTemplate.getForObject(uri.toString(), String.class);
		
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(new InputSource(new StringReader(apiResult)));
	    doc.getDocumentElement().normalize();
	    Node firstResult = doc.getElementsByTagName("itemList").item(0);	
	    
	    for (int temp = 0; temp < firstResult.getChildNodes().getLength(); temp++) {
	    	String nodeName =firstResult.getChildNodes().item(temp).getNodeName();
	    	if(nodeName.equals("lastBusTm")) {
	    		busInfo.setLastBusTm(firstResult.getChildNodes().item(temp).getTextContent());
	    	}
	    		
	    }        
	         
		return busInfo.getLastBusTm();
		
//	    }catch(Exception e) {
//	    	e.printStackTrace();
//	    }finally {
//	    	return "트라이캐치 에러   ";
//	    }
		
	}
	
	public String fallback(String apiAddr, String strsch,Throwable e) {
		e.printStackTrace();
		return  "노선 정보가 없습니다.";
	}
}
