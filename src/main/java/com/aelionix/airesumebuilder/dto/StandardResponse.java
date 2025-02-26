package com.aelionix.airesumebuilder.dto;


/**
 * A standard response structure for API responses.
 */
public record StandardResponse(String status, int code, String message, Object data) {}
