# 🧵 BizBitsNow — API Documentation

> **Backend REST API** for BizBitsNow — a multi-tenant e-commerce marketplace and retail management system allowing seamless interactions between Admins, Sellers, and Customers.

***

## 📋 Table of Contents

* [Project Overview](#-project-overview)
* [Tech Stack](#-tech-stack)
* [Getting Started](#-getting-started)
* [Environment Variables](#-environment-variables)
* [Project Structure](#-project-structure)
***

## 🏢 Project Overview

**BizBitsNow** ek comprehensive multi-tenant retail platform hai jo support karta hai:

* Role-based access control across Admin, Seller, aur Customer roles.
* Domain-level dynamic parameters jaise `subdomain` aur custom `brand_color` for individual storefronts.
* Multi-stage order lifecycle tracking.
* Secure email pipeline workflow for real-time OTP verification using Google SMTP.
* Automated dynamic database table initialization schemas via custom Hibernate wrappers.

***

## 🛠 Tech Stack

| Layer        | Technology                       |
| ------------ | ------------------------------- |
| Runtime      | Java 21 (Eclipse Temurin)       |
| Framework    | Spring Boot v4.0.6              |
| Security     | Spring Security (Stateless)     |
| Auth Token   | JWT (JSON Web Tokens)           |
| Data Layer   | Spring Data JPA, Hibernate 7.x  |
| Database     | MySQL 8.0                       |
| Build Tool   | Maven v3.9.6                    |
| Deployment   | Docker, Docker Compose (Bridge) |

***

## 🚀 Getting Started

### 1. Clone the repository
bash
git clone [https://github.com/Shresth860/BizBitsNow.git](https://github.com/Shresth860/BizBitsNow.git)
cd BizBitsNow

### 2. Set up environment variables
Project ke root directory mein ek .env file create karein:
Bash

# New local secure env create karein
touch .env

### 3. Fill values in .env
Code snippet
DATABASE_USERNAME=AskFromOwner
DATABASE_PASSWORD=AskFromOwner
EMAIL_PASSWORD=your_16_digit_gmail_app_password\

### 4. Build and Run via Docker Compose
Bash
docker-compose up --build
Is single isolated orchestration layer command se base system complete architecture deploy kar dega.

### 5. Verify backend is running
GET http://localhost:8080/

### 📁 Project Structure
BizBitsNow/
├── src/
│   ├── main/
│   │   ├── java/com/BizBitsNow/BizBitsNow/
│   │   │   ├── Authentication/       # Login, Registration endpoints controllers
│   │   │   ├── config/               # JWT token configurations, Spring Security Filters
│   │   │   ├── Controller/           # Layered REST route entry point managers
│   │   │   ├── DTO/                  # Request/Response structure schemas
│   │   │   ├── Entity/               # JPA mapped tables declarations
│   │   │   ├── Enum/                 # Data Type state constants (Role, Status codes)
│   │   │   ├── Repository/           # Data layer persistence abstraction boundaries
│   │   │   └── Service/              # Core algorithmic functional handlers
│   │   └── resources/
│   │       └── application.properties # Main Spring core settings
├── .env
├── .env.example
├── .gitignore
├── docker-compose.yml
├── dockerfile                     # Multi-stage image optimizer engine
└── pom.xml


### BizBitsNow Enterprise Backend Application Server
Built & Maintained by
Shresth Saxena
