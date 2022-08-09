
package com.example.demo;
import com.example.restservice.Greeting;
import com.example.valve.TraceValve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController //controller bean 的请求统一来自于DispatcherServlet的转发（springboot 在web容器启动成功后注册的）
@ServletComponentScan(basePackages = {"com.example.servlet", "com.example.filter", "com.example.listener"}) //自定义注册的servlet直接处理客户端请求，与DispatcherServlet是同等级别的
public class DemoApplication {

	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * web服务器的通用配置,与具体的Web服务器工厂无关
	 */
	@Component
	public class MyGeneralCustomizer implements
			WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

		@Override
		public void customize(ConfigurableServletWebServerFactory factory) {
			factory.setPort(8080);
			factory.setContextPath("/hello");
		}
	}


	/**
	 * tomcat服务器的定制化配置，与特定web服务器工厂相关
	 * 其他web服务器工厂可以使用JettyServletWebServerFactory、UndertowServletWebServerFactory等
	 * 这里是在engine容器中的pipeline中增加一个valve,用于为所有请求添加traceId
	 */
	@Component
	public class MyTomcatCustomizer implements
			WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

		@Override
		public void customize(TomcatServletWebServerFactory factory) {
			factory.addEngineValves(new TraceValve());
		}
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name, HttpServletRequest request) {
		log.info("/hello traceId:{}", request.getHeader("traceid"));
		return String.format("Hello %s!", name);
	}

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@GetMapping("/testConsumeService")
	public String testConsumeService(RestTemplate restTemplate) {
		String res = restTemplate.getForObject("http://www.baidu.com", String.class);
		log.info("res :{}", res);
		return res;
	}
}