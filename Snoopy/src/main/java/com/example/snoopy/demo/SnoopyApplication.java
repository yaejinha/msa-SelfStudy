package com.example.snoopy.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
@EnableEurekaClient
@SpringBootApplication
public class SnoopyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnoopyApplication.class, args);
	}

}
