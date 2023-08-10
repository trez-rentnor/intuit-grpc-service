package com.rjones.restservice;

import java.util.Date;

public record PersonalInfoRest(
        String firstName,
        String lastName,
        Date dob,
        String email,
        String phoneNumber) { }
