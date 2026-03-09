# Stripe API Automation Framework

A comprehensive REST API automation framework for testing Stripe's **Customers** and **Payments** APIs. Built with Java, Gradle, TestNG, and REST Assured for reliable, maintainable, and scalable API testing.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Architecture](#test-architecture)
- [API Endpoints Covered](#api-endpoints-covered)
- [Documentation](#documentation)
- [Best Practices](#best-practices)

---

## 🎯 Overview

This framework provides a robust testing solution for Stripe API integrations, focusing on:
- **Customer Management**: Creation, retrieval, and updates
- **Payment Operations**: Invoice creation, finalization, and tracking
- **Automated Testing**: Comprehensive test cases with proper isolation
- **Service Layer Architecture**: Separation of concerns between tests and API calls

---

## ✨ Features

✅ **Service-Oriented Architecture** - Dedicated service classes for API interactions  
✅ **Test Isolation** - Each test runs independently with fresh test data  
✅ **Parallel Test Execution** - Tests can run concurrently without conflicts  
✅ **REST Assured Integration** - Fluent API for request/response handling  
✅ **Configuration Management** - Externalized config via `config.properties`  
✅ **Comprehensive Test Reports** - HTML reports with detailed execution metrics  
✅ **TestNG Framework** - Advanced test organization and execution control  
✅ **Hamcrest Matchers** - Powerful assertion library for validation  

---

## 📁 Project Structure

```
stripe-api-automation-framework/
├── src/
│   ├── main/java/
│   │   └── com/stripe/
│   │       └── utilities/
│   │           └── ConfigManager.java          # Configuration loader
│   │
│   └── test/java/
│       └── com/stripe/
│           ├── api/
│           │   ├── models/
│           │   │   ├── requests/                # Request DTOs
│           │   │   └── responses/               # Response DTOs
│           │   └── service/
│           │       ├── customers/               # Customer API service
│           │       └── payments/                # Invoice/Payment service
│           │
│           └── tests/
│               ├── base/
│               │   └── BaseTest.java           # Base test class (global setup)
│               ├── customers/
│               │   └── CustomerCreationTest.java
│               └── payments/
│                   ├── InnvoiceCreationTest.java
│                   └── InvoiceTest.java
│
├── build/                                       # Build artifacts
├── gradle/                                      # Gradle wrapper
├── docs/                                        # Documentation
├── build.gradle                                 # Gradle dependencies & tasks
├── settings.gradle                              # Gradle settings
└── README.md                                    # This file
```

---

## 📦 Prerequisites

- **Java**: JDK 8 or higher
- **Gradle**: 7.0+ (included via Gradle Wrapper)
- **Stripe Account**: Valid API keys for authentication
- **Git**: For cloning the repository

---

## 🚀 Installation

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/stripe-api-automation-framework.git
cd stripe-api-automation-framework
```

### 2. Verify Gradle Installation
```bash
./gradlew --version  # On Mac/Linux
gradlew.bat --version  # On Windows
```

### 3. Build the Project
```bash
./gradlew clean build
```

---

## ⚙️ Configuration

### 1. Set Up Stripe API Keys

Edit `src/test/resources/config.properties`:

```properties
# Stripe API Configuration
baseUrl=https://api.stripe.com/v1
stripe.secret.key=sk_test_YOUR_SECRET_KEY_HERE

# Test Configuration
test.timeout=5000
test.retry.count=3
```

### 2. Obtain Your Stripe Keys

1. Log in to [Stripe Dashboard](https://dashboard.stripe.com)
2. Go to **Developers** → **API Keys**
3. Copy your **Secret Key** (starts with `sk_test_`)
4. Paste into `config.properties`

⚠️ **Security Note**: Never commit real API keys to version control. Use environment variables or secure secret management.

## Test Architecture

### BaseTest Class
- **Purpose**: Global setup for all tests
- **Responsibility**: Configure REST Assured (baseURL, authentication)
- **Scope**: `@BeforeClass` - Runs once per test class
- **Location**: `src/test/java/com/stripe/api/tests/base/BaseTest.java`

### Service Layer
- **Purpose**: Encapsulate API interactions
- **Classes**:
  - `CustomerService` - Customer CRUD operations
  - `InvoiceService` - Invoice operations
- **Benefits**: Reusability, maintainability, separation of concerns

### Test Classes
- **Customer Tests**: `CustomerCreationTest`
- **Invoice Tests**: `InnvoiceCreationTest`
- **Each test**: 
  - Gets fresh test data via `@BeforeMethod`
  - Verifies one specific behavior
  - Cleans up in `@AfterMethod`

