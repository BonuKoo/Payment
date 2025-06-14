package com.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.function.Consumer;

@EnableRetry
@EnableScheduling
@EnableFeignClients(basePackages = "com.payment")
@SpringBootApplication
public class PaymentApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(PaymentApplication.class);
		application.run(args);
	}

	@Bean
	public Consumer<Message<String>> consumer(){
		return message ->{
			String payload = message.getPayload();
			System.out.println("Received message: " + payload);
		};
	}

}
