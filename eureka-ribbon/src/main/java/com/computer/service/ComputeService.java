package com.computer.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ComputeService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "addServiceFallback")
    public String addService(Integer a, Integer b) {
        String url = String.format("http://COMPUTE-SERVICE/compute/add?a=%d&b=%d", a, b);

        return restTemplate.getForEntity(url, String.class).getBody();
    }

    public String addServiceFallback(Integer a, Integer b) {
        return "error";
    }

}
