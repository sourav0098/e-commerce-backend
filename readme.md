# QuickPik E-Commerce Web Application

## Introduction

QuickPik is an e-commerce web app built using SpringBoot and MySQL. The app allows users to browse and purchase a variety of electronic devices including smartphones, laptops, and smart home devices. The app also includes features such as Spring Security, JWT authentication, and Google login.

## Features

- [x] User authentication and authorization with Spring Security
- [x] JSON Web Token (JWT) authentication
- [x] Google login integration
- [x] AWS S3 integration for image storage
- [x] Entities for user, products, orders, and cart
- [x] Role-based authentication

## Technologies

- [x] Java 17
- [x] SpringBoot 3.0
- [x] Spring Security
- [x] AWS S3
- [x] JWT
- [x] Google OAuth
- [x] MySQL 8.0

## Setup

1. Clone the repository
2. Create a MySQL database named `quickpik`
3. Create a `application.yml` file and add the following variables:

```
server:
  port: 8080

# Database Configuartion
spring:
  datasource:
    url: "url"
    username: "sql-username"
    password: "sql-password"
    driver-class-name: com.mysql.cj.jdbc.Driver

# JPA Configuration
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect

#File upload config
  servlet:
    multipart:
      max-file-size: 2MB
      max-request-size: 5MB

#Role Id
role.admin.id: "unique id for admin role"
role.normal.id: "unique id for normal role"

# Google Login
googleClientId: "google-client-id"
googleSecret: "google-secret"

# JWT Secret Key
application:
  security:
    jwt:
     secret-key: "secret-key"
     expiration: "jwt-token-expiry-time"
     refresh-token:
      expiration: "refresh-token-expiry-time"

#AWS S3
aws:
  s3:
    accessKey: "s3-accessKey"
    secretKey: "s3-secretKey"
    bucketName: "s3-bucketName"
    region: "aws-bucket-region"
    user-image-path: "s3 bucket directory for storing user images"
    products-image-path: "s3 bucket directory for storing product images"
    categories-image-path: "s3 bucket directory for storing category images"
```
