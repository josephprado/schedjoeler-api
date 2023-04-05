package com.portfolijo.schedjoeler.controller;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the body of an HTTP response
 */
@Getter
@Builder
public class Response<T> {
   @Builder.Default
   private final LocalDateTime TIMESTAMP = LocalDateTime.now();
   private List<T> data;
   private String message;
}
