package com.furkantokgz.productapi.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductRequest(
        @NotBlank(message = "Name is required") String name,
        @Min(value = 0, message = "Price must be non-negative") double price
) {
}
