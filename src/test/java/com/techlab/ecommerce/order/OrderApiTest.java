package com.techlab.ecommerce.order;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderApiTest {

    @Autowired
    private MockMvc mvc;

    private long userId;
    private long mouseId;
    private long cableId;

    private ResultActions postJson(String url, String json) throws Exception {
        return mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json));
    }

    private long idOf(ResultActions result) throws Exception {
        return ((Number) JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.id")).longValue();
    }

    @BeforeEach
    void setUp() throws Exception {
        userId = idOf(postJson("/api/users", "{\"name\": \"Ana\", \"email\": \"ana@mail.com\"}"));
        mouseId = idOf(postJson("/api/products", "{\"name\": \"Mouse\", \"price\": 19.99, \"stock\": 5}"));
        cableId = idOf(postJson("/api/products", "{\"name\": \"Cable\", \"price\": 5.00, \"stock\": 1}"));
    }

    private String order(String items) {
        return "{\"userId\": %d, \"items\": [%s]}".formatted(userId, items);
    }

    private String item(long productId, int quantity) {
        return "{\"productId\": %d, \"quantity\": %d}".formatted(productId, quantity);
    }

    @Test
    void createsTheOrderComputesTheTotalAndTakesTheStock() throws Exception {
        postJson("/api/orders", order(item(mouseId, 2) + "," + item(cableId, 1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.total").value(44.98)) // 2 × 19.99 + 5.00
                .andExpect(jsonPath("$.lines", hasSize(2)));

        mvc.perform(get("/api/products/{id}", mouseId)).andExpect(jsonPath("$.stock").value(3));
        mvc.perform(get("/api/products/{id}", cableId)).andExpect(jsonPath("$.stock").value(0));
    }

    @Test
    void allOrNothingWhenOneLineHasNoStock() throws Exception {
        // The mouse line is fine, the cable line asks for 2 of 1
        postJson("/api/orders", order(item(mouseId, 2) + "," + item(cableId, 2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Not enough stock for 'Cable': available 1, requested 2"));

        // The mouse stock must be untouched
        mvc.perform(get("/api/products/{id}", mouseId)).andExpect(jsonPath("$.stock").value(5));
    }

    @Test
    void theSameProductTwiceIsCheckedAsOneQuantity() throws Exception {
        // 3 + 3 = 6 > 5 even though each line alone would fit
        postJson("/api/orders", order(item(mouseId, 3) + "," + item(mouseId, 3)))
                .andExpect(status().isConflict());
    }

    @Test
    void theLineKeepsThePriceItWasBoughtAt() throws Exception {
        long orderId = idOf(postJson("/api/orders", order(item(mouseId, 1))));

        mvc.perform(put("/api/products/{id}", mouseId).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Mouse\", \"price\": 99.00, \"stock\": 4}"))
                .andExpect(status().isOk());

        mvc.perform(get("/api/orders/{id}", orderId))
                .andExpect(jsonPath("$.lines[0].unitPrice").value(19.99))
                .andExpect(jsonPath("$.total").value(19.99));
    }

    @Test
    void aProductThatWasOrderedCantBeDeleted() throws Exception {
        postJson("/api/orders", order(item(mouseId, 1))).andExpect(status().isCreated());

        mvc.perform(delete("/api/products/{id}", mouseId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Data conflict"));
    }

    @Test
    void emptyCartIs400() throws Exception {
        postJson("/api/orders", order("")).andExpect(status().isBadRequest());
    }

    @Test
    void unknownUserIs404() throws Exception {
        postJson("/api/orders", "{\"userId\": 999, \"items\": [%s]}".formatted(item(mouseId, 1)))
                .andExpect(status().isNotFound());
    }
}
