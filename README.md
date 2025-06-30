# Tadafur Strategic Planning API

A comprehensive Strategic Planning Management REST API built with Spring Boot, designed to manage the complete strategy-to-execution hierarchy.

## Overview

The Tadafur API provides a robust platform for strategic planning management, covering the entire cascade from high-level strategies down to tactical work items.

### Strategic Hierarchy Model
Strategy → Perspective → Goal → Program → Initiative → Project → Work Items


## Features

### Core Functionality
- **Strategic Hierarchy Management** - Complete strategy cascade
- **Financial Analytics** - Budget planning, tracking, and variance analysis
- **Progress Monitoring** - Multi-method progress calculations
- **Vision Alignment** - Strategic objective alignment tracking
- **Cross-Domain Integration** - Ready for Process, SAM, and Service domains

### API Capabilities
- **RESTful API** design with JSON responses
- **Read-optimized** operations for analytics and reporting
- **Hierarchical navigation** and cascade analysis
- **Advanced search** and filtering capabilities
- **Performance analytics** for strategic insights

## Technology Stack

- **Framework**: Spring Boot 3.3.6
- **Language**: Java 21
- **Database**: PostgreSQL
- **Build Tool**: Maven 3.9.10
- **Architecture**: Domain-Driven Design with Layered Architecture

## Project Structure

src/main/java/com/project/Tadafur_api/
├── domain/strategy/              # Strategy Planning Domain
│   ├── controller/
│   │   ├── hierarchy/           # CRUD endpoints
│   │   └── analytics/           # Analytics endpoints
│   ├── service/
│   │   ├── hierarchy/           # Domain services
│   │   ├── analytics/           # Analytics services
│   │   └── aggregation/         # Aggregation services
│   ├── repository/              # Data access layer
│   ├── dto/                     # Data transfer objects
│   └── entity/                  # JPA entities
├── shared/                      # Cross-domain infrastructure
│   ├── common/                  # Base classes
│   ├── analytics/               # Shared analytics
│   └── utils/                   # Utilities
└── integration/                 # Cross-domain integration

## Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.9+
- PostgreSQL database
- IntelliJ IDEA (recommended) or any Java IDE

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/tadafur-strategic-planning-api.git
   cd tadafur-strategic-planning-api