# intuit-grpc-service

## Overview

This project builds a Spring REST service that accepts requests to add personal information for users.  The REST service
adds a timestamp to the request and makes a grpc request to a C++ server that logs the personal information to stdout.

The source code for each service is in its own directory.  The Spring REST service is in the `personal_info_rest`
directory and the C++ grpc service is in the `personal_info_grpc` directory.

## Running the services

Before running the services, install the grpc package.

Run these commands to start the grpc service

    cd personal_info_grpc/cmake/build/
    cmake ../..
    make
    ./personal_info_server

In another terminal, run these commands to start the rest service.

    cd personal_info_rest
    ./mvnw spring-boot:run

Test the service by posting this json to `http://localhost:8080/personal_info`

    {
        "firstName": "Randy",
        "lastName": "Jones",
        "dob": "1995-12-10",
        "email": "fake-address@gmail.com",
        "phoneNumber": "123-456-7890"
    }
