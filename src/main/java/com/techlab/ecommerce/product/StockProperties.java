package com.techlab.ecommerce.product;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "techlab.stock")
public record StockProperties(@DefaultValue("5") int lowThreshold) {
}
