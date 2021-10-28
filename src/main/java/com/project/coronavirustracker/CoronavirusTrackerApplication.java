package com.project.coronavirustracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 
 * @EnableScheduling: It tells spring to wrap the methods which has a schedule
 *                    annotation in proxy and has logic to call those methods.
 * 
 * 
 */
@SpringBootApplication
@EnableScheduling
public class CoronavirusTrackerApplication {

   public static void main(String[] args) {
      SpringApplication.run(CoronavirusTrackerApplication.class, args);
   }

}
