package com.techlab.ecommerce.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techlab.ecommerce.category.Category;
import com.techlab.ecommerce.category.CategoryService;
import com.techlab.ecommerce.common.error.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository products;
    private final CategoryService categories;

    public ProductService(ProductRepository products, CategoryService categories) {
        this.products = products;
        this.categories = categories;
    }

    public List<ProductResponse> search(String name) {
        List<Product> found = (name == null || name.isBlank())
                ? products.findAll()
                : products.findByNameContainingIgnoreCase(name.trim());
        return found.stream().map(ProductResponse::from).toList();
    }

    public ProductResponse get(Long id) {
        return ProductResponse.from(getEntity(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = categories.getEntity(request.categoryId());
        Product product = new Product(
                request.name().trim(),
                request.description(),
                request.price(),
                request.stock(),
                request.imageUrl(),
                category);
        return ProductResponse.from(products.save(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getEntity(id);
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setImageUrl(request.imageUrl());
        product.setCategory(categories.getEntity(request.categoryId()));
        // No save() needed: the entity is managed, Hibernate writes the changes on commit
        return ProductResponse.from(product);
    }

    @Transactional
    public void delete(Long id) {
        products.delete(getEntity(id));
    }

    Product getEntity(Long id) {
        return products.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }
}
