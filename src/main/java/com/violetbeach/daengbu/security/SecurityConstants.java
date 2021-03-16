package com.violetbeach.daengbu.security;

public interface SecurityConstants {
    String SECRET = "daengbu";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    long EXPIRATION_TIME = 864_000_000; // 10 days
}