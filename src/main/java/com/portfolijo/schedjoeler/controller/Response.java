package com.portfolijo.schedjoeler.controller;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Represents the body of an HTTP response
 */
@Getter
@Builder
public class Response {
   @Builder.Default
   private final LocalDateTime TIMESTAMP = LocalDateTime.now();
   private Object data;
   private String message;
}
