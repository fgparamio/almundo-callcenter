package com.almundo.callcenter;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/*
 * This is the main Spring Boot application class. It configures Spring Boot, JPA, Swagger
 */
/**
 * 
 * @author fgparamio
 *
 */
@EnableAsync
@SpringBootApplication
@Configuration
@EnableAutoConfiguration // Sprint Boot Auto Configuration
@ComponentScan(basePackages = "com.almundo.callcenter")
public class Application extends SpringBootServletInitializer implements AsyncConfigurer {

	private static final Class<Application> applicationClass = Application.class;
	private static final Logger LOG = LoggerFactory.getLogger(applicationClass);
	
	@Value("${callcenter.numThreads}")
	private Integer maxNumThreads;

	public static void main(final String[] args) {
		SpringApplication.run(applicationClass, args);
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}


	@Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(maxNumThreads);;
        threadPoolTaskExecutor.setMaxPoolSize(maxNumThreads);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
	
	@Bean("callcenterExecutor")
	@Override
	public Executor getAsyncExecutor() {
		return new ConcurrentTaskExecutor(threadPoolTaskExecutor());
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (throwable, method, objects) -> LOG.error("-- exception handler -- " + throwable);
	}

}
