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


/**
 * Almundo CallCenter Application SpringBoot Configuration
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
	
	// Private slf4j Logger
	private static final Logger LOG = LoggerFactory.getLogger(applicationClass);
	
	// Maximum threads in Thread Pool to attend by priority queue
	@Value("${callcenter.numThreads}")
	private Integer maxNumThreads;

	/**
	 *  ****************************** MAIN METHOD ********************************
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		SpringApplication.run(applicationClass, args);
	}

	/**
	 * Configure SpringApplicationBuilder
	 */
	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}

	/**************************  SPRING CONTEXT CONFIGURATION   *********************/ 
	
	/**
	 * 
	 * @return ThreadPoolTaskExecutor context Bean
	 */
	@Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(maxNumThreads);;
        threadPoolTaskExecutor.setMaxPoolSize(maxNumThreads);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
	
	/**
	 * @return Default Executor in Spring Context
	 */
	@Bean("callcenterExecutor")
	@Override
	public Executor getAsyncExecutor() {
		return new ConcurrentTaskExecutor(threadPoolTaskExecutor());
	}

	/**
	 * 
	 * @return ExceptionHandler for AsyncConfigurer
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (throwable, method, objects) -> LOG.error("-- exception handler -- " + throwable);
	}

}
