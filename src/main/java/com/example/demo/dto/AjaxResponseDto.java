package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
public class AjaxResponseDto {
    @Data
    @AllArgsConstructor
    public static class CommonAjaxResponseDto {
        private boolean successFlag;

        private String successMessage;

        private String errorMessage;

        private Map<String, ?> resultMap;

        public static CommonAjaxResponseDto success(String successMessage, Map<String, ?> resultMap) {
            return new CommonAjaxResponseDto(true, successMessage, null, resultMap);
        }

        public static CommonAjaxResponseDto success(String successMessage) {
            return new CommonAjaxResponseDto(true, successMessage, null, null);
        }

        public static CommonAjaxResponseDto error(String errorMessage) {
            return new CommonAjaxResponseDto(false, null, errorMessage, null);
        }
    }
}
