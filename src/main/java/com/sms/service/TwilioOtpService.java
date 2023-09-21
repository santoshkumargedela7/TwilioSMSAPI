package com.sms.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sms.config.TwilioConfig;
import com.sms.dto.OtpStatus;
import com.sms.dto.PasswordResetRequestDto;
import com.sms.dto.PasswordResetResponseDto;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import reactor.core.publisher.Mono;

@Service
public class TwilioOtpService {

	@Autowired
	private TwilioConfig twilioConfig;

	Map<String, String> otpMap = new HashMap<>();

	public Mono<PasswordResetResponseDto> sendOtpForPasswordReset(PasswordResetRequestDto passwordResetRequestDto) {

		PasswordResetResponseDto passwordResetResponseDto = null;

		try {
			PhoneNumber to = new com.twilio.type.PhoneNumber(passwordResetRequestDto.getPhoneNumber());
			PhoneNumber from = new PhoneNumber(twilioConfig.getTrailNumber());
			String otp = generateOTP();
			String otpMessage = "Dear Customer , Your OTp is ##"+
					 otp + "##.Use this passcode to complete your transaction. THank you"  ;

			Message message = Message.creator(to, from, otpMessage).create();

			System.out.println(message.getSid());

			otpMap.put(passwordResetRequestDto.getUserName(), otp);
			passwordResetResponseDto = new PasswordResetResponseDto(OtpStatus.DELIVERED, otpMessage);

		} catch (Exception e) {
			passwordResetResponseDto = new PasswordResetResponseDto(OtpStatus.FAILED, e.getMessage());

		}
		return Mono.just(passwordResetResponseDto);
		
	}

	private String generateOTP() {

		return new DecimalFormat("000000").format(new Random().nextInt(999999));
	}

	public Mono<String> validateOTP(String userInputOtp, String userName) {

		if (userInputOtp.equals(otpMap.get(userName))) {
			return Mono.just("Valid OTP please proceed with your transaction  ! ");
		} else {
			return Mono.error(new IllegalArgumentException("Invalid OTP please retry!!"));
		}
	}

}
