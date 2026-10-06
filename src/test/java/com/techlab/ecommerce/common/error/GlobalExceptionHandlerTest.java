package com.techlab.ecommerce.common.error;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.ThrowingController.class)
@Import(GlobalExceptionHandlerTest.ThrowingController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void notFoundIs404WithProblemDetail() throws Exception {
        mvc.perform(get("/test/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not found"))
                .andExpect(jsonPath("$.detail").value("Product 99 not found"));
    }

    @Test
    void businessRuleIs409() throws Exception {
        mvc.perform(get("/test/rule"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Not enough stock"));
    }

    @Test
    void validationIs400AndListsTheFields() throws Exception {
        mvc.perform(post("/test/validate").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());
    }

    record Body(@NotBlank String name) {
    }

    @RestController
    static class ThrowingController {

        @GetMapping("/test/missing")
        void missing() {
            throw new ResourceNotFoundException("Product", 99);
        }

        @GetMapping("/test/rule")
        void rule() {
            throw new BusinessRuleException("Not enough stock");
        }

        @PostMapping("/test/validate")
        void validate(@Valid @RequestBody Body body) {
        }
    }
}
