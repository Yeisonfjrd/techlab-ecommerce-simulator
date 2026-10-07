package com.techlab.ecommerce.user;

import java.util.List;
import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techlab.ecommerce.common.error.BusinessRuleException;
import com.techlab.ecommerce.common.error.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    public List<UserResponse> findAll() {
        return users.findAll().stream().map(UserResponse::from).toList();
    }

    public UserResponse get(Long id) {
        return UserResponse.from(getEntity(id));
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (users.existsByEmail(email)) {
            throw new BusinessRuleException("A user with email " + email + " already exists");
        }

        Role role = request.role() == null ? Role.CLIENT : request.role();
        try {
            // the unique constraint catches a concurrent duplicate
            return UserResponse.from(users.saveAndFlush(new User(request.name().trim(), email, role)));
        } catch (DataIntegrityViolationException e) {
            throw new BusinessRuleException("A user with email " + email + " already exists");
        }
    }

    @Transactional
    public void delete(Long id) {
        User user = getEntity(id);
        if (users.hasOrders(id)) {
            throw new BusinessRuleException("User " + id + " has orders and can't be deleted");
        }
        users.delete(user);
        users.flush(); // same safety net as ProductService.delete
    }

    public User getEntity(Long id) {
        return users.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
