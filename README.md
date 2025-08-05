# Java E-commerce Backend System (Spring Boot, PostgreSQL, RabbitMQ)

This project is a scalable and test-driven e-commerce backend system built with **Spring Boot**, **Java**, **JPA/Hibernate**, **PostgreSQL**, and **RabbitMQ**, designed to handle core e-commerce functionalities including user management, product management, and asynchronous messaging.

## Features

- User registration with hashed password storage (BCrypt)
- User login with credential verification
- Email confirmation with expiring link (15 minutes)
- PostgreSQL database integration via JPA/Hibernate
- RabbitMQ integration for async email messaging
- Full CRUD support for user data
- Spring Boot powered RESTful backend
- Clean architecture with DTO & Entity separation

## Registration Page

After user is registered, their login data will be stored and an email will be sent to enable their account
<img width="1146" height="469" alt="Screenshot_3" src="https://github.com/user-attachments/assets/ada7f64b-9c79-437e-b4f3-6f65b90f1359" />

Currently, user is not enabled thus can not log in
<img width="1354" height="116" alt="Screenshot_5" src="https://github.com/user-attachments/assets/9c2c9918-ddd0-4369-8aa1-33a5b6c7e491" />

## Email Verification
<img width="1919" height="1025" alt="Screenshot_6" src="https://github.com/user-attachments/assets/74408f9c-99ed-4fda-b57d-03017f62933c" />
<br><br>
After clicking the verification link, user will be enabled and can login
<img width="1345" height="109" alt="Screenshot_7" src="https://github.com/user-attachments/assets/023daa0b-e4e0-4bde-9d83-8ee1de6a0f79" />

## Login Page
<img width="1919" height="1026" alt="Screenshot_8" src="https://github.com/user-attachments/assets/da5f8621-a42a-42e3-86e7-b729ec57b736" />

## Technologies Used

- Java 17+
- Spring Boot
- Spring Data JPA & Hibernate
- PostgreSQL
- RabbitMQ
- JavaMailSender
- BCrypt (password hashing)
- JUnit / Mockito (for testing)
