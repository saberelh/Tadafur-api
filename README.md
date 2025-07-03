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




erDiagram
%% ==================================
%% Core Strategy & Execution Hierarchy
%% ==================================
strategy {
int id PK
varchar primary_name
text primary_description
text vision
int owner_id FK
date timeline_from
date timeline_to
varchar secondary_name
text secondary_description
numeric planned_total_budget
numeric calculated_total_budget
numeric calculated_total_payments
integer_array budget_sources
}

    perspective {
        int id PK
        varchar primary_name
        text primary_description
        int owner_id FK
        int parent_id FK
        varchar planning_status_code
        varchar progress_status_code
        varchar secondary_name
        text secondary_description
        numeric calculated_total_budget
        numeric calculated_total_payments
        numeric planned_total_budget
        integer_array budget_sources
    }

    goal {
        int id PK
        varchar primary_name
        text primary_description
        int parent_id FK
        int owner_id FK
        date start_date
        date end_date
        varchar planning_status_code
        varchar progress_status_code
        varchar secondary_name
        text secondary_description
        numeric calculated_progress_percent
        numeric hybrid_progress_percent
        int vision_priority FK
        numeric planned_total_budget
        numeric calculated_total_budget
        numeric calculated_total_payments
        integer_array budget_sources
    }

    program {
        int id PK
        varchar primary_name
        text primary_description
        int parent_id FK
        double contributionpercent
        int owner_id FK
        varchar planning_status_code
        varchar progress_status_code
        varchar secondary_name
        text secondary_description
        integer_array vision_priorities
        numeric calculated_progress_percent
        numeric hybrid_progress_percent
        numeric planned_total_budget
        numeric calculated_total_budget
        numeric calculated_total_payments
        integer_array budget_sources
    }

    initiative {
        int id PK
        varchar primary_name
        text primary_description
        int parent_id FK
        double contributionpercent
        int owner_id FK
        numeric planned_total_budget
        int type
        date start_date
        date end_date
        varchar planning_status_code
        varchar progress_status_code
        varchar secondary_name
        text secondary_description
        integer_array vision_priorities
        numeric calculated_progress_percent
        numeric hybrid_progress_percent
        numeric calculated_total_budget
        numeric calculated_total_payments
        int owner_node_id FK
        integer_array budget_sources
    }

    project {
        int id PK
        varchar primary_name
        text primary_description
        int parent_id FK
        double contributionpercent
        int owner_id FK
        numeric planned_total_budget
        int type
        date start_date
        date end_date
        varchar planning_status_code
        varchar progress_status_code
        varchar secondary_name
        text secondary_description
        numeric actual_cost
        int priority_id FK
        int status_id FK
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
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
    }

    project_work_item {
        int id PK
        int project_id FK
        int parent_id FK
        varchar primary_name
        text primary_description
        varchar secondary_name
        text secondary_description
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
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
        bigint work_item_group_id FK
        int level
        int item_sort
        varchar verification_result
        numeric progress_by_effort
        numeric progress_by_average
        numeric manual_progress_by_effort
        numeric manual_progress_by_average
        boolean is_added_from_custom
    }

    %% ==================================
    %% Progress Tracking System
    %% ==================================
    progress_point {
        int id PK
        date date
        double percent
    }

    goal_checkpoint {
        int goal_id PK FK
        int progress_point_id PK FK
    }

    goal_progress_log {
        int goal_id PK FK
        int progress_point_id PK FK
    }

    program_checkpoint {
        int program_id PK FK
        int progress_point_id PK FK
    }

    program_progress_log {
        int program_id PK FK
        int progress_point_id PK FK
    }

    initiative_checkpoint {
        int initiative_id PK FK
        int progress_point_id PK FK
    }

    initiative_progress_log {
        int initiative_id PK FK
        int progress_point_id PK FK
    }

    project_checkpoint {
        int project_id PK FK
        int progress_point_id PK FK
    }

    project_progress_log {
        int project_id PK FK
        int progress_point_id PK FK
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

    %% ==================================
    %% Annual Planning System
    %% ==================================
    annual_plan {
        int id PK
        varchar primary_name
        varchar secondary_name
        text primary_description
        text secondary_description
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
        text primary_description
        text secondary_description
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

    %% ==================================
    %% KPI and Indicators System
    %% ==================================
    goal_indicators {
        int id PK
        int goal_id FK
        text primary_description
        text secondary_description
        numeric baseline_value
        numeric targeted_value
        varchar measurement_unit
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    goal_kpi {
        int id PK
        text primary_description
        int status_id
        int goal_id FK
        text secondary_description
    }

    program_indicators {
        int id PK
        int program_id FK
        int indicator_id FK
    }

    initiative_indicators {
        int id PK
        int initiative_id FK
        int indicator_id FK
    }
    
    %% ==================================
    %% Vision Alignment System
    %% ==================================
    vision_pillars {
        int id PK
        text primary_name
        text secondary_name
        text primary_description
        text secondary_description
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
        char country_code
    }

    vision_priorities {
        int id PK
        text primary_name
        text secondary_name
        int pillar_id FK
        text primary_strategic_objective
        text secondary_strategic_objective
        text primary_description
        text secondary_description
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    vision_priority_goals {
        int id PK
        text primary_name
        text secondary_name
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
        int priority_id FK
    }

    vision_priority_indicators {
        int id PK
        text primary_name
        text secondary_name
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
        int priority_id FK
    }

    %% ==================================
    %% Project & Work Item Details
    %% ==================================
    project_priority {
        int id PK
        varchar primary_name
        varchar secondary_name
    }

    project_status {
        int id PK
        varchar primary_name
        varchar secondary_name
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

    project_members {
        int id PK
        bigint member_id FK
        bigint project_id FK
        bigint member_role FK
    }

    project_roles {
        int id PK
        varchar secondary_name
        varchar primary_name
        text secondary_description
        text primary_description
        varchar string_id
    }

    project_stakeholder {
        int id PK
        int project_id FK
        int org_chart_node_id FK
    }

    work_item_priority {
        int id PK
        varchar primary_name
        varchar secondary_name
    }

    work_item_status {
        int id PK
        varchar primary_name
        varchar secondary_name
        int priority
        numeric progress
    }

    work_item_group {
        bigint id PK
        varchar secondary_name
        varchar primary_name
    }

    work_item_type {
        int id PK
        varchar secondary_name
        int level
        bigint project_methodology_id FK
        varchar primary_name
    }

    work_item_dependency_type {
        int id PK
        varchar primary_name
        varchar secondary_name
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

    %% ==================================
    %% Contributors System
    %% ==================================
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

    %% ==================================
    %% Financial & Budgeting System
    %% ==================================
    budget_payments {
        int id PK
        int entity_id
        varchar entity_code FK
        date payment_date
        numeric amount
        text primary_payment_notes
        text secondary_payment_notes
        varchar created_by
        timestamp created_at
        varchar last_modified_by
        timestamp last_modified_at
        varchar status_code
    }

    spm_element {
        varchar entity_code PK
        varchar entity_type
    }

    %% ==================================
    %% Issues, Risks, and Calendar
    %% ==================================
    issue_log {
        bigint id PK
        bigint project_id FK
        varchar primary_title
        varchar secondary_title
        text primary_description
        text secondary_description
        bigint category_id FK
        bigint severity_id FK
        bigint impact_area_id FK
        bigint status_id FK
        bigint assigned_user_id FK
        integer_array linked_items_ids
        bigint type_id FK
    }

    issue_category { bigint id PK; varchar primary_name; bigint project_id FK; bigint parent_id FK; }
    issue_severity { bigint id PK; varchar primary_name; }
    issue_impact_area { bigint id PK; varchar primary_name; }
    issue_status { bigint id PK; varchar primary_name; }
    issue_type { bigint id PK; varchar primary_name; }
    issue_attachment { bigint id PK; bigint issue_id FK; varchar file_name; }
    issue_log_work_item { bigint id PK; bigint issue_log_id FK; bigint work_item_id FK; }
    
    risks { bigint id PK; varchar primary_name; int classification_id FK; int type_id FK; int category_id FK; int owner_id FK; }
    risk_action_plans { int id PK; int risk_response_id FK; text action_plan; }
    risk_responses { int id PK; int risk_id FK; int response_strategy FK; }
    risk_projects { int id PK; int risk_id FK; int project_id FK; }
    risk_categories { int id PK; int type_id FK; varchar primary_name; }
    risk_types { int id PK; varchar primary_name; int classification_id FK; }
    risk_classification { int id PK; varchar primary_name; }
    risk_classification_response { int id PK; int classification_id FK; varchar primary_name; }

    calendar_event {
        int id PK
        varchar primary_title
        text primary_purpose
        timestamp start_datetime
        timestamp end_datetime
        int project_id FK
    }

    %% ==================================
    %% Service, Process & Journey
    %% ==================================
    service { bigint id PK; varchar primary_name; text primary_description; bigint owner_id FK; int org_chart_node_id FK; }
    business_process { int id PK; varchar primary_name; int service_id FK; int authority_id FK; int journey_id FK; }
    journey { bigint id PK; varchar primary_name; int authority_id FK; int journey_status_id FK; }
    journey_flow { bigint id PK; int journey_id FK; int service_id FK; }
    journey_status { int id PK; varchar primary_name; }

    %% ==================================
    %% Custom Boards
    %% ==================================
    custom_board { int id PK; varchar primary_name; int project_id FK; int user_id FK; }
    custom_board_groups { int id PK; varchar primary_name; int custom_board_id FK; }
    custom_board_work_items { int id PK; int custom_board_group_id FK; int project_work_item_id FK; }

    %% ==================================
    %% Core Organizational Entities
    %% ==================================
    authority {
        bigint id PK
        varchar primary_name
        varchar secondary_name
        char vision_master
        char is_limited
        varchar primary_language
    }

    users {
        int id PK
        varchar firstname
        varchar lastname
        varchar username
        bigint role_id FK
        varchar primary_email
        bigint authority_id FK
    }

    role {
        bigint id PK
        varchar name
        text description
        bigint authority_id FK
    }

    org_chart_tree {
        int id PK
        varchar primary_title
        int parent_id FK
        int authority_id FK
    }


    %% ==================================
    %% RELATIONSHIPS
    %% ==================================

    %% Core Hierarchy
    strategy                    ||--|{ perspective : "breaks down into"
    perspective                 ||--|{ goal : "contains"
    goal                        ||--|{ program : "is achieved by"
    program                     ||--|{ initiative : "is composed of"
    initiative                  ||--|{ project : "contains"
    project                     ||--|{ project_work_item : "contains"
    project_work_item           }o--|| project_work_item : "is parent of"

    %% Ownership & Contributors
    authority                   ||--|{ strategy : "owns"
    authority                   ||--|{ perspective : "owns"
    authority                   ||--|{ goal : "owns"
    authority                   ||--|{ program : "owns"
    authority                   ||--|{ initiative : "owns"
    authority                   ||--|{ project : "owns"
    authority                   ||--o{ program_contributors : "contributes to"
    program                     ||--o{ program_contributors : "has"
    authority                   ||--o{ initiative_contributors : "contributes to"
    initiative                  ||--o{ initiative_contributors : "has"
    authority                   ||--o{ project_contributors : "contributes to"
    project                     ||--o{ project_contributors : "has"
    authority                   ||--o{ project_contributed_authority : "contributes to"
    project                     ||--o{ project_contributed_authority : "has"

    %% Organizational Structure
    authority                   ||--|{ users : "employs"
    authority                   ||--|{ role : "defines"
    authority                   ||--|{ org_chart_tree : "has"
    users                       ||--o{ role : "has"
    org_chart_tree              }o--|| org_chart_tree : "is parent of"
    initiative                  ||--o{ org_chart_tree : "owned by unit"

    %% Vision Alignment
    vision_pillars              ||--|{ vision_priorities : "contains"
    vision_priorities           ||--|{ vision_priority_goals : "has"
    vision_priorities           ||--|{ vision_priority_indicators : "has"
    vision_priorities           ||--o{ goal : "aligns with"

    %% Indicators and KPIs
    goal                        |o--|{ goal_indicators : "measured by"
    goal                        |o--|{ goal_kpi : "measured by"
    program                     |o--o{ program_indicators : "tracks"
    goal_indicators             ||--o{ program_indicators : "linked to"
    initiative                  |o--o{ initiative_indicators : "tracks"
t;
goal_indicators             ||--o{ initiative_indicators : "linked to"

    %% Progress & Checkpoints
    goal                        |o--o{ goal_checkpoint : "has"
    progress_point              |o--o{ goal_checkpoint : "is"
    program                     |o--o{ program_checkpoint : "has"
    progress_point              |o--o{ program_checkpoint : "is"
    initiative                  |o--o{ initiative_checkpoint : "has"
    progress_point              |o--o{ initiative_checkpoint : "is"
    project                     |o--o{ project_checkpoint : "has"
    progress_point              |o--o{ project_checkpoint : "is"
    goal                        |o--o{ goal_progress_log : "logs"
    progress_point              |o--o{ goal_progress_log : "is"
    program                     |o--o{ program_progress_log : "logs"
    progress_point              |o--o{ program_progress_log : "is"
    initiative                  |o--o{ initiative_progress_log : "logs"
    progress_point              |o--o{ initiative_progress_log : "is"
    project                     |o--o{ project_progress_log : "logs"
    progress_point              |o--o{ project_progress_log : "is"
    project                     ||--o{ project_tracking : "logs history"

    %% Annual Plan
    annual_plan                 ||--o{ annual_plan_goal : "has"
    annual_plan                 ||--o{ annual_plan_indicators : "tracks"
    annual_plan                 ||--o{ annual_plan_initiative_project : "funds"
    initiative                  }o--|| annual_plan_initiative_project : "is funded by"
    project                     }o--|| annual_plan_initiative_project : "is funded by"
    goal_indicators             ||--o{ annual_plan_indicators : "is tracked in"

    %% Project & Work Item Details
    project_priority            ||--o{ project : "has"
    project_status              ||--o{ project : "has"
    project_methodology         ||--o{ project : "uses"
    project_summary_period      ||--o{ project : "reports in"
    project                     ||--o{ project_members : "has"
    users                       ||--o{ project_members : "is member of"
    project_roles               ||--o{ project_members : "has role"
    project                     |o--|| project_stakeholder : "has"
    org_chart_tree              |o--|| project_stakeholder : "is"
    project                     ||--o{ calendar_event : "has"
    project_work_item           ||--o{ work_item_attachments : "has"
    project_work_item           |o--o{ task_dependency : "depends on"
    project_work_item           |o--o{ task_dependency : "precedes"
    work_item_dependency_type   ||--o{ task_dependency : "is type of"
    work_item_priority          ||--o{ project_work_item : "has"
    work_item_status            ||--o{ project_work_item : "has"
    work_item_group             ||--o{ project_work_item : "part of"
    work_item_type              ||--o{ project_work_item : "is type of"
    project_methodology         ||--o{ work_item_type : "defines"
    users                       ||--o{ project_work_item : "assigned to"
    
    %% Issue & Risk Management
    project                     ||--o{ issue_log : "has"
    issue_category              ||--o{ issue_log : "categorizes"
    issue_severity              ||--o{ issue_log : "has"
    issue_impact_area           ||--o{ issue_log : "has"
    issue_status                ||--o{ issue_log : "has"
    issue_type                  ||--o{ issue_log : "is type of"
    issue_log                   ||--o{ issue_attachment : "has"
    issue_log                   |o--o{ issue_log_work_item : "relates to"
    project_work_item           |o--o{ issue_log_work_item : "related to"
    project                     |o--o{ risk_projects : "has"
    risks                       |o--o{ risk_projects : "is in"
    risks                       |o--o{ risk_responses : "has"
    risk_responses              |o--o{ risk_action_plans : "has"
    risk_classification_response ||--o{ risk_responses : "has"
    risk_types                  |o--o{ risk_categories : "has"
    risk_classification         |o--o{ risk_types : "has"

    %% Budgeting
    spm_element                 ||--o{ budget_payments : "tracks"