package com.violetbeach.daengbu.exception;

public enum ExceptionType {
	
    DTO_NOT_FOUND("not.found"),
    DUPLICATE_DTO("duplicate");

    String value;

    ExceptionType(String value) {
        this.value = value;
    }

    String getValue() {
        return this.value;
    }
    
}