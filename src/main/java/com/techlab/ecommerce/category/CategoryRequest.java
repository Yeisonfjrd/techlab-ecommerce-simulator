package com.techlab.ecommerce.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(@NotBlank @Size(max = 80) String name) {
}
