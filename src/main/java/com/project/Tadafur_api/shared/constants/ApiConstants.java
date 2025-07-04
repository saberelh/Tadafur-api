// File: shared/constants/ApiConstants.java
package com.project.Tadafur_api.shared.constants;

/**
 * API Constants
 */
public final class ApiConstants {

    // API Version
    public static final String API_VERSION = "/api/v1";

    // Base paths
    public static final String STRATEGIES_BASE_PATH = API_VERSION + "/strategies";
    public static final String PERSPECTIVES_BASE_PATH = API_VERSION + "/perspectives";
    public static final String GOALS_BASE_PATH = API_VERSION + "/goals";
    public static final String PROGRAMS_BASE_PATH = API_VERSION + "/programs";
    public static final String INITIATIVES_BASE_PATH = API_VERSION + "/initiatives";
    public static final String PROJECTS_BASE_PATH = API_VERSION + "/projects";
    public static final String WORK_ITEMS_BASE_PATH = API_VERSION + "/work-items";

    // Pagination defaults
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "DESC";

    // Response messages
    public static final String SUCCESS_MESSAGE = "Operation completed successfully";
    public static final String ERROR_MESSAGE = "An error occurred";

    private ApiConstants() {}
}