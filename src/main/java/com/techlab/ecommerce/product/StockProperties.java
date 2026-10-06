package com.techlab.ecommerce.product;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Was `const MIN_STOCK_THRESHOLD = 5` in the simulator. Now it's configuration:
 * techlab.stock.low-threshold in application.yml, or TECHLAB_STOCK_LOW_THRESHOLD as an env var.
 */
@ConfigurationProperties(prefix = "techlab.stock")
public record StockProperties(@DefaultValue("5") int lowThreshold) {
}
