package com.sms;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sms.config.TwilioConfig;
import com.twilio.Twilio;

@SpringBootApplication
public class TwilioExampleApplication {
	
	
	@Autowired
	private TwilioConfig twilioConfig;

	@PostConstruct
	public void initTwilio() {
		Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
	}

	public static void main(String[] args) {
		SpringApplication.run(TwilioExampleApplication.class, args);
	}

}
