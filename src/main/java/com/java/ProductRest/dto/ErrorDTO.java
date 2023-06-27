package com.java.ProductRest.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    private String field;
    private String errorMessage;
}
