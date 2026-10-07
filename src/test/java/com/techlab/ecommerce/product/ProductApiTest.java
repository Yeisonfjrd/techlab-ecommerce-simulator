package com.techlab.ecommerce.product;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

/** Full stack: HTTP -> controller -> service -> H2. Each test rolls back. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductApiTest {

    @Autowired
    private MockMvc mvc;

    private ResultActions postJson(String url, String json) throws Exception {
        return mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json));
    }

    private long idOf(ResultActions result) throws Exception {
        String body = result.andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(body, "$.id")).longValue();
    }

    private long createProduct(String name, String price, int stock) throws Exception {
        return idOf(postJson("/api/products",
                "{\"name\": \"%s\", \"price\": %s, \"stock\": %d}".formatted(name, price, stock)));
    }

    @Test
    void createReturns201WithLocationAndCategory() throws Exception {
        long categoryId = idOf(postJson("/api/categories", "{\"name\": \"Periféricos\"}")
                .andExpect(status().isCreated()));

        postJson("/api/products", "{\"name\": \"Mouse\", \"price\": 19.99, \"stock\": 10, \"categoryId\": %d}".formatted(categoryId))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern(".*/api/products/\\d+$")))
                .andExpect(jsonPath("$.price").value(19.99))
                .andExpect(jsonPath("$.categoryName").value("Periféricos"));
    }

    @Test
    void stockZeroIsValid() throws Exception {
        // the JS version rejected 0: `if (!productData.stock)`
        postJson("/api/products", "{\"name\": \"Agotado\", \"price\": 5, \"stock\": 0}")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stock").value(0));
    }

    @Test
    void rejectsNonPositivePriceAndNegativeStock() throws Exception {
        postJson("/api/products", "{\"name\": \"Gratis\", \"price\": 0, \"stock\": -1}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.price").exists())
                .andExpect(jsonPath("$.errors.stock").exists());
    }

    @Test
    void unknownCategoryIs404() throws Exception {
        postJson("/api/products", "{\"name\": \"Mouse\", \"price\": 10, \"stock\": 1, \"categoryId\": 999}")
                .andExpect(status().isNotFound());
    }

    @Test
    void duplicateCategoryIs409() throws Exception {
        postJson("/api/categories", "{\"name\": \"Audio\"}").andExpect(status().isCreated());
        postJson("/api/categories", "{\"name\": \"audio\"}").andExpect(status().isConflict());
    }

    @Test
    void searchesByName() throws Exception {
        createProduct("Teclado mecánico", "50.00", 3);
        createProduct("Monitor", "200.00", 1);

        mvc.perform(get("/api/products").param("name", "teclado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Teclado mecánico"));
    }

    @Test
    void updateReplacesTheProduct() throws Exception {
        long id = createProduct("Mouse", "10.00", 1);

        mvc.perform(put("/api/products/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Mouse Pro\", \"price\": 12.50, \"stock\": 4}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mouse Pro"))
                .andExpect(jsonPath("$.stock").value(4));
    }

    @Test
    void deleteThenGetIs404() throws Exception {
        long id = createProduct("Mouse", "10.00", 1);

        mvc.perform(delete("/api/products/{id}", id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/products/{id}", id)).andExpect(status().isNotFound());
    }
}
