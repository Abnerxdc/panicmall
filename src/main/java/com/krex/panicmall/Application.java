package com.krex.panicmall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xdc
 */
@SpringBootApplication
@EnableScheduling
public class Application {
	static Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		try{
			SpringApplication.run(Application.class, args);
		}catch (Throwable e){
			logger.error(e.getMessage(),e);
		}
	}

}
