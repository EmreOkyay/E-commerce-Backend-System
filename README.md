# Java E-commerce Backend System

This project is a scalable and test-driven e-commerce backend system written in **Java**, and uses technologies such as **Spring Boot**, **JPA/Hibernate**, **PostgreSQL**, **RabbitMQ**, and **Redis**.
<br><br>
It is designed to handle core e-commerce functionalities including user management, product management, shopping cart operations such as adding and removing products, as well as order management that allows placing and tracking orders, all while supporting asynchronous messaging for improved scalability and decoupling.
<br><br>
The entire project is built on a CI/CD pipeline where every new version is tested, built, containerized, and published to Docker Hub using GitHub Actions.

## Features

- User registration with hashed password storage (BCrypt)
- User login with credential verification
- Email confirmation with an expiring verification link
- PostgreSQL database integration via JPA/Hibernate
- RabbitMQ integration for asynchronous email messaging
- Redis integration for caching
- Full CRUD support for user, product, cart and order data
- Spring Boot powered RESTful backend
- Clean architecture with DTO & Entity separation

## Registration
<img width="1146" height="469" alt="Screenshot_3" src="https://github.com/user-attachments/assets/ada7f64b-9c79-437e-b4f3-6f65b90f1359" />
<br><br>
After the user is registered, their login data will be stored and a code will be sent to their account via email.
<br><br>

<img width="1354" height="116" alt="false" src="https://github.com/user-attachments/assets/e5805f45-49ae-41ff-9fe5-8f17326835bc" />
<br><br>
Currently, user's account is not enabled, so they can not log in.

## Email Verification
<img width="1919" height="1025" alt="Screenshot_6" src="https://github.com/user-attachments/assets/74408f9c-99ed-4fda-b57d-03017f62933c" />
<br><br>
Either by using MailDev SMTP server or Postman, we can quickly test emailing and user activation locally.
<br><br>
<img width="1345" height="109" alt="true" src="https://github.com/user-attachments/assets/d5aa337a-07d2-4bf5-bbec-521525c653ce" />
<br><br>
After clicking the verification link, user will be enabled and can log in.

## RabbitMQ
<img width="1900" height="931" alt="rabbitmq" src="https://github.com/user-attachments/assets/4be4bce0-9be2-4eef-8d36-51ea14dd21db" />
<br><br>
RabbitMQ is hosted locally via Docker and used as a message broker to handle asynchronous email notifications. <br>
When a user registers or completes an order, corresponding email messages are sent by Spring Boot services through RabbitMQ queues.

## Redis Cache
<img width="1919" height="1079" alt="read-and-write" src="https://github.com/user-attachments/assets/d788c575-d1ba-46ac-beee-c0e1b7a73dd2" />
<br><br>
Redis is used to cache data for faster access. RabbitMQ handles asynchronous cache updates through a publisher–listener setup that triggers on events such as product add, delete, and purchase.


## Docker
<img width="1591" height="239" alt="docker" src="https://github.com/user-attachments/assets/3d6de2e9-c57c-444b-8ade-8f8dc5558636" />
<br><br>
Docker containers are created and run for each service used in the project.

## Technologies Used

- Java 17+
- Spring Boot
- Spring Data JPA & Hibernate
- PostgreSQL
- RabbitMQ
- Redis
- Docker
- JavaMailSender
- BCrypt (password hashing)
- JUnit / Mockito (for testing)
