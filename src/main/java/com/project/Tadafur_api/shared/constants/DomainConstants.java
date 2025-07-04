// File: shared/constants/DomainConstants.java
package com.project.Tadafur_api.shared.constants;

public final class DomainConstants {

    // Status Codes
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_DELETED = "DELETED";
    public static final String STATUS_ARCHIVED = "ARCHIVED";

    // Database Schema - REMOVED to use default (public)
    public static final String DATABASE_SCHEMA = "";

    // Planning Status
    public static final String PLANNING_STATUS_DRAFT = "DRAFT";
    public static final String PLANNING_STATUS_ACTIVE = "ACTIVE";
    public static final String PLANNING_STATUS_COMPLETED = "COMPLETED";

    // Progress Status
    public static final String PROGRESS_STATUS_NOT_STARTED = "NOT_STARTED";
    public static final String PROGRESS_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String PROGRESS_STATUS_COMPLETED = "COMPLETED";

    private DomainConstants() {}
}