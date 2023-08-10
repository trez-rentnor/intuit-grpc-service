package com.rjones.restservice;

import java.util.concurrent.TimeUnit;
//import java.io.System;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

import com.rjones.restservice.SetPersonalInfoGrpc.SetPersonalInfoBlockingStub;

@RestController
public class PersonalInfoController {

	public PersonalInfoController() {
	}

	@Override
	protected void finalize() {
	}

	@PostMapping("/personal_info")
	public ResponseEntity<Void> personalInfo(
			@RequestBody PersonalInfoRest newPersonalInfo) throws InterruptedException {

		ManagedChannel channel;
		SetPersonalInfoBlockingStub grpcStub;

		channel = Grpc.newChannelBuilder("localhost:50051",
						InsecureChannelCredentials.create())
				.build();

		try {
			grpcStub = SetPersonalInfoGrpc.newBlockingStub(channel);

			// TODO Handle null values
			// TODO Validate email and phone number
			// TODO Use Calendar to parse dates
			PersonalInfo personalInfo = PersonalInfo.newBuilder()
					.setFirstName(newPersonalInfo.firstName())
					.setLastName(newPersonalInfo.lastName())
					.setDob(DateOfBirth.newBuilder()
							.setMonth(DateOfBirth.Month.forNumber(
									newPersonalInfo.dob().getMonth() + 1))
							.setDay(newPersonalInfo.dob().getDate() + 1)
							.setYear(newPersonalInfo.dob().getYear() + 1900)
							.build())
					.setEmail(newPersonalInfo.email())
					.setPhoneNumber(newPersonalInfo.phoneNumber())
					.setModifiedTs(System.currentTimeMillis() / 1000)
					.build();

			grpcStub.setPersonalInfo(personalInfo);
		} finally {
			channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
