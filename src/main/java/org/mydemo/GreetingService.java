package org.mydemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class GreetingService {
	
	@Autowired
	private EurekaClient eurekaClient;
	
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	
	@HystrixCommand(fallbackMethod="defaultGreeting")
	public String getGreeting(String username) {
		RestTemplate restTemplate = restTemplateBuilder.build();
		
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("rest-producer", false);
		String baseUrl = instanceInfo.getHomePageUrl();
		baseUrl = baseUrl + "/greet/" + username;
		return restTemplate.getForObject(baseUrl, String.class);
	}

	private String defaultGreeting(String username) {
		return "Hello User!";
	}
}