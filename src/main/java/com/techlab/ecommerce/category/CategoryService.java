package com.techlab.ecommerce.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techlab.ecommerce.common.error.BusinessRuleException;
import com.techlab.ecommerce.common.error.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categories;

    public CategoryService(CategoryRepository categories) {
        this.categories = categories;
    }

    public List<CategoryResponse> findAll() {
        return categories.findAll().stream().map(CategoryResponse::from).toList();
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        String name = request.name().trim();
        if (categories.findByNameIgnoreCase(name).isPresent()) {
            throw new BusinessRuleException("Category '" + name + "' already exists");
        }
        return CategoryResponse.from(categories.save(new Category(name)));
    }

    /** Used by ProductService; a null id means "no category". */
    public Category getEntity(Long id) {
        if (id == null) {
            return null;
        }
        return categories.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }
}
