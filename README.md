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
   



Full Strategy db erd "erDiagram
%% Core Strategy Hierarchy
strategy {
int id PK
varchar arabic_name
varchar english_name
text arabic_description
text english_description
text vision
int owner_id FK
date timeline_from
date timeline_to
numeric planned_total_budget
numeric calculated_total_budget
numeric calculated_total_payments
integer_array budget_sources
varchar created_by
timestamp created_at
varchar last_modified_by
timestamp last_modified_at
varchar status_code
}

    perspective {
        int id PK
        varchar arabic_name
        varchar english_name
        text arabic_description
        text english_description
        int owner_id FK
        int parent_id FK
        varchar planning_status_code
        varchar progress_status_code
        numeric calculated_total_budget
        numeric calculated_total_payments
        numeric planned_total_budget
        integer_array budget_sources
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    goal {
        int id PK
        varchar arabic_name
        varchar english_name
        text arabic_description
        text english_description
        int parent_id FK
        int owner_id FK
        date start_date
        date end_date
        varchar planning_status_code
        varchar progress_status_code
        numeric calculated_progress_percent
        numeric hybrid_progress_percent
        int vision_priority FK
        numeric planned_total_budget
        numeric calculated_total_budget
        numeric calculated_total_payments
        integer_array budget_sources
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    program {
        int id PK
        varchar arabic_name
        varchar english_name
        text arabic_description
        text english_description
        int parent_id FK
        float contributionpercent
        int owner_id FK
        varchar planning_status_code
        varchar progress_status_code
        integer_array vision_priorities
        numeric calculated_progress_percent
        numeric hybrid_progress_percent
        numeric planned_total_budget
        numeric calculated_total_budget
        numeric calculated_total_payments
        integer_array budget_sources
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    initiative {
        int id PK
        varchar arabic_name
        varchar english_name
        text arabic_description
        text english_description
        int parent_id FK
        float contributionpercent
        int owner_id FK
        numeric planned_total_budget
        int type
        date start_date
        date end_date
        varchar planning_status_code
        varchar progress_status_code
        integer_array vision_priorities
        numeric calculated_progress_percent
        numeric hybrid_progress_percent
        numeric calculated_total_budget
        numeric calculated_total_payments
        int owner_node_id FK
        integer_array budget_sources
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    project {
        int id PK
        varchar arabic_name
        varchar english_name
        text arabic_description
        text english_description
        int parent_id FK
        float contributionpercent
        int owner_id FK
        numeric planned_total_budget
        int type
        date start_date
        date end_date
        varchar planning_status_code
        varchar progress_status_code
        numeric actual_cost
        int priority_id FK
        int status_id FK
        integer_array vision_priorities
        bigint project_methodology_id FK
        numeric progress_by_effort
        numeric progress_by_average
        int progress_specification_id
        int propagation_model_id
        numeric manual_progress_by_effort
        numeric manual_progress_by_average
        numeric calculated_progress_percent
        numeric hybrid_progress_percent
        numeric calculated_total_budget
        numeric calculated_total_payments
        timestamp summary_sent_date
        int summary_period FK
        integer_array budget_sources
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    project_work_item {
        int id PK
        int project_id FK
        int parent_id FK
        varchar arabic_name
        varchar english_name
        text arabic_description
        text english_description
        int priority_id FK
        int status_id FK
        int assignee_user_id FK
        numeric estimated_time
        int estimated_time_unit
        numeric actual_time
        int actual_time_unit
        date planned_start_date
        date planned_due_date
        date actual_start_date
        date actual_due_date
        numeric progress_percent
        bigint work_item_group_id FK
        int level
        int item_sort
        varchar verification_result
        numeric progress_by_effort
        numeric progress_by_average
        numeric manual_progress_by_effort
        numeric manual_progress_by_average
        boolean is_added_from_custom
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    %% Progress Tracking System
    progress_point {
        int id PK
        date date
        float percent
    }

    strategy_checkpoint {
        int strategy_id PK FK
        int progress_point_id PK FK
    }

    perspective_checkpoint {
        int perspective_id PK FK
        int progress_point_id PK FK
    }

    goal_checkpoint {
        int goal_id PK FK
        int progress_point_id PK FK
    }

    program_checkpoint {
        int program_id PK FK
        int progress_point_id PK FK
    }

    initiative_checkpoint {
        int initiative_id PK FK
        int progress_point_id PK FK
    }

    project_checkpoint {
        int project_id PK FK
        int progress_point_id PK FK
    }

    strategy_progress_log {
        int strategy_id PK FK
        int progress_point_id PK FK
    }

    perspective_progress_log {
        int perspective_id PK FK
        int progress_point_id PK FK
    }

    goal_progress_log {
        int goal_id PK FK
        int progress_point_id PK FK
    }

    program_progress_log {
        int program_id PK FK
        int progress_point_id PK FK
    }

    initiative_progress_log {
        int initiative_id PK FK
        int progress_point_id PK FK
    }

    project_progress_log {
        int project_id PK FK
        int progress_point_id PK FK
    }

    %% Annual Planning System
    annual_plan {
        int id PK
        varchar arabic_name
        varchar english_name
        text arabic_description
        text english_description
        int owner_id FK
        int plan_year
        varchar plan_status
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    annual_plan_goal {
        int id PK
        int annual_plan_id FK
        text arabic_description
        text english_description
    }

    annual_plan_indicators {
        int id PK
        int annual_plan_id FK
        int indicator_id FK
        numeric q1_planned
        numeric q2_planned
        numeric q3_planned
        numeric q4_planned
        numeric q1_actual
        numeric q2_actual
        numeric q3_actual
        numeric q4_actual
        numeric baseline_value
    }

    annual_plan_initiative_project {
        int id PK
        int annual_plan_id FK
        int initiative_id FK
        int project_id FK
        numeric annual_budget
    }

    %% KPI and Indicators System
    goal_indicators {
        int id PK
        int goal_id FK
        text arabic_description
        text english_description
        numeric baseline_value
        numeric targeted_value
        varchar measurement_unit
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    initiative_indicators {
        int id PK
        int initiative_id FK
        int indicator_id FK
    }

    program_indicators {
        int id PK
        int program_id FK
        int indicator_id FK
    }

    goal_kpi {
        int id PK
        text description
        int status_id
        int goal_id FK
    }

    %% Vision Alignment System
    vision_pillars {
        int id PK
        text arabic_name
        text english_name
        text arabic_description
        text english_description
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
        char country_code
    }

    vision_priorities {
        int id PK
        text arabic_name
        text english_name
        int pillar_id FK
        text arabic_strategic_objective
        text english_strategic_objective
        text arabic_description
        text english_description
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    vision_priority_goals {
        int id PK
        text arabic_name
        text english_name
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
        int priority_id FK
    }

    vision_priority_indicators {
        int id PK
        text arabic_name
        text english_name
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
        int priority_id FK
    }

    %% Contributors and Collaboration
    program_contributors {
        int program_id PK FK
        int authority_id PK FK
    }

    initiative_contributors {
        int initiative_id PK FK
        int authority_id PK FK
    }

    project_contributors {
        int project_id PK FK
        int authority_id PK FK
    }

    project_contributed_authority {
        int id PK
        int project_id FK
        int authority_id FK
        numeric contribution_percent
    }

    %% Project Management Details
    project_priority {
        int id PK
        varchar arabic_name
        varchar english_name
    }

    project_status {
        int id PK
        varchar arabic_name
        varchar english_name
    }

    project_methodology {
        bigint id PK
        varchar name
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    project_summary_period {
        int id PK
        varchar period
    }

    project_tracking {
        int id PK
        int project_id FK
        numeric calculated_progress_by_effort
        numeric calculated_progress_by_average
        numeric manual_progress_by_effort
        numeric manual_progress_by_average
        timestamp calculation_date_time
    }

    project_stakeholder {
        int id PK
        int project_id FK
        int org_chart_node_id FK
    }

    project_members {
        int id PK
        bigint member_id FK
        bigint project_id FK
        bigint member_role FK
    }

    project_roles {
        int id PK
        varchar english_name
        varchar arabic_name
        text english_description
        text arabic_description
        varchar string_id
    }

    %% Work Item Management
    work_item_priority {
        int id PK
        varchar arabic_name
        varchar english_name
    }

    work_item_status {
        int id PK
        varchar arabic_name
        varchar english_name
        int priority
        numeric progress
    }

    work_item_group {
        bigint id PK
        varchar english_name
        varchar arabic_name
    }

    work_item_dependency_type {
        int id PK
        varchar arabic_name
        varchar english_name
    }

    task_dependency {
        int id PK
        int from_work_item_id FK
        int to_work_item_id FK
        int dependency_type_id FK
    }

    work_item_attachments {
        int id PK
        int work_item_id FK
        varchar file_name
        varchar file_type
        bigint file_size
        bytea file_data
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    work_item_type {
        int id PK
        varchar english_name
        int level
        bigint project_methodology_id FK
    }

    %% Budget and Financial Tracking
    budget_payments {
        int id PK
        int entity_id
        varchar entity_code FK
        date payment_date
        numeric amount
        text arabic_payment_notes
        text english_payment_notes
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    %% Shared Reference Tables
    authority {
        bigint id PK
        varchar namearabic
        varchar nameenglish
        timestamp created_at
        timestamp last_modified_at
        varchar created_by
        varchar last_modified_by
        varchar status_code
        char vision_master
        char is_limited
        text logobase64
        char country_code
    }

    org_chart_tree {
        int id PK
        int node_type_id
        int node_classification_id
        int node_category_id
        varchar arabic_title
        varchar english_title
        int parent_id FK
        int authority_id FK
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    users {
        int id PK
        varchar firstname
        varchar lastname
        varchar username
        varchar password
        varchar role
        timestamp created_at
        timestamp last_modified_at
        varchar created_by
        varchar last_modified_by
        varchar status_code
        varchar keycloak_user_id
        bigint role_id FK
        varchar primary_email
    }

    %% Relationships - Core Hierarchy
    strategy ||--o{ perspective : "contains"
    perspective ||--o{ goal : "contains"
    goal ||--o{ program : "contains"
    program ||--o{ initiative : "contains"
    initiative ||--o{ project : "contains"
    project ||--o{ project_work_item : "contains"
    project_work_item ||--o{ project_work_item : "has subtasks"

    %% Relationships - Ownership
    authority ||--o{ strategy : "owns"
    authority ||--o{ perspective : "owns"
    authority ||--o{ goal : "owns"
    authority ||--o{ program : "owns"
    authority ||--o{ initiative : "owns"
    authority ||--o{ project : "owns"

    %% Relationships - Progress Tracking
    progress_point ||--o{ strategy_checkpoint : "strategy milestone"
    progress_point ||--o{ perspective_checkpoint : "perspective milestone"
    progress_point ||--o{ goal_checkpoint : "goal milestone"
    progress_point ||--o{ program_checkpoint : "program milestone"
    progress_point ||--o{ initiative_checkpoint : "initiative milestone"
    progress_point ||--o{ project_checkpoint : "project milestone"

    strategy ||--o{ strategy_checkpoint : "has checkpoints"
    perspective ||--o{ perspective_checkpoint : "has checkpoints"
    goal ||--o{ goal_checkpoint : "has checkpoints"
    program ||--o{ program_checkpoint : "has checkpoints"
    initiative ||--o{ initiative_checkpoint : "has checkpoints"
    project ||--o{ project_checkpoint : "has checkpoints"

    progress_point ||--o{ strategy_progress_log : "logs progress"
    progress_point ||--o{ perspective_progress_log : "logs progress"
    progress_point ||--o{ goal_progress_log : "logs progress"
    progress_point ||--o{ program_progress_log : "logs progress"
    progress_point ||--o{ initiative_progress_log : "logs progress"
    progress_point ||--o{ project_progress_log : "logs progress"

    %% Relationships - Annual Planning
    authority ||--o{ annual_plan : "creates"
    annual_plan ||--o{ annual_plan_goal : "defines goals"
    annual_plan ||--o{ annual_plan_indicators : "tracks indicators"
    annual_plan ||--o{ annual_plan_initiative_project : "includes projects"
    initiative ||--o{ annual_plan_initiative_project : "planned in"
    project ||--o{ annual_plan_initiative_project : "planned in"

    %% Relationships - KPIs and Indicators
    goal ||--o{ goal_indicators : "measures"
    goal ||--o{ goal_kpi : "tracks performance"
    initiative ||--o{ initiative_indicators : "measures"
    program ||--o{ program_indicators : "measures"
    goal_indicators ||--o{ initiative_indicators : "inherited by"
    goal_indicators ||--o{ program_indicators : "inherited by"
    goal_indicators ||--o{ annual_plan_indicators : "planned in"

    %% Relationships - Vision Alignment
    vision_pillars ||--o{ vision_priorities : "contains"
    vision_priorities ||--o{ vision_priority_goals : "defines"
    vision_priorities ||--o{ vision_priority_indicators : "measures"
    vision_priorities ||--o{ goal : "aligns with"
    vision_priorities ||--o{ program : "aligns with"
    vision_priorities ||--o{ initiative : "aligns with"
    vision_priorities ||--o{ project : "aligns with"

    %% Relationships - Contributors
    authority ||--o{ program_contributors : "contributes to"
    authority ||--o{ initiative_contributors : "contributes to"
    authority ||--o{ project_contributors : "contributes to"
    authority ||--o{ project_contributed_authority : "contributes to"
    program ||--o{ program_contributors : "has contributors"
    initiative ||--o{ initiative_contributors : "has contributors"
    project ||--o{ project_contributors : "has contributors"
    project ||--o{ project_contributed_authority : "has contributors"

    %% Relationships - Project Management
    project_priority ||--o{ project : "prioritizes"
    project_status ||--o{ project : "status of"
    project_methodology ||--o{ project : "follows methodology"
    project_summary_period ||--o{ project : "reports every"
    project ||--o{ project_tracking : "tracks progress"
    project ||--o{ project_stakeholder : "has stakeholders"
    project ||--o{ project_members : "has members"
    org_chart_tree ||--o{ project_stakeholder : "is stakeholder"
    users ||--o{ project_members : "member of"
    project_roles ||--o{ project_members : "has role"

    %% Relationships - Work Items
    work_item_priority ||--o{ project_work_item : "prioritizes"
    work_item_status ||--o{ project_work_item : "status of"
    work_item_group ||--o{ project_work_item : "groups"
    users ||--o{ project_work_item : "assigned to"
    project_work_item ||--o{ work_item_attachments : "has attachments"
    project_work_item ||--o{ task_dependency : "depends on"
    project_work_item ||--o{ task_dependency : "dependency for"
    work_item_dependency_type ||--o{ task_dependency : "type of"
    project_methodology ||--o{ work_item_type : "defines types"

    %% Relationships - Financial
    budget_payments ||--o{ strategy : "pays for"
    budget_payments ||--o{ perspective : "pays for"
    budget_payments ||--o{ goal : "pays for"
    budget_payments ||--o{ program : "pays for"
    budget_payments ||--o{ initiative : "pays for"
    budget_payments ||--o{ project : "pays for"

    %% Relationships - Organizational
    org_chart_tree ||--o{ org_chart_tree : "parent node"
    authority ||--o{ org_chart_tree : "organizational unit"
    org_chart_tree ||--o{ initiative : "owns initiative"

