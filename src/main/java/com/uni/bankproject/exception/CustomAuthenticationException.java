package com.uni.bankproject.exception;

public class CustomAuthenticationException extends RuntimeException {
  public CustomAuthenticationException(Exception message) {
    super(message);
  }
}