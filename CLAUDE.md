# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 4.0.3 backend for an employee management system. Java 21, Gradle build, PostgreSQL database, Lombok for boilerplate reduction.

## Build & Run Commands

- **Build:** `./gradlew build`
- **Run:** `./gradlew bootRun`
- **Test all:** `./gradlew test`
- **Run single test:** `./gradlew test --tests "com.ethan.employee_system.SomeTestClass.testMethod"`
- **Clean build:** `./gradlew clean build`

## Architecture

- **Base package:** `com.ethan.employee_system`
- **Entry point:** `EmployeeSystemApplication` (standard Spring Boot main class)
- **Database:** PostgreSQL (driver included, connection not yet configured in `application.properties`)
- **Testing:** JUnit 5 via `spring-boot-starter-webmvc-test`

This is an early-stage project with the scaffolding in place but no domain entities, controllers, or repositories yet.
