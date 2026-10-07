package com.techlab.ecommerce.product;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "techlab.stock.low-threshold=3")
@AutoConfigureMockMvc
@Transactional
class LowStockApiTest {

    @Autowired
    private MockMvc mvc;

    private void create(String name, int stock) throws Exception {
        mvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"%s\", \"price\": 1, \"stock\": %d}".formatted(name, stock)))
                .andExpect(status().isCreated());
    }

    @Test
    void listsProductsAtOrBelowTheThresholdEmptiestFirst() throws Exception {
        create("Lleno", 10);
        create("Justo", 3);   // <= 3, included like in the simulator
        create("Vacío", 0);
        create("Casi", 4);

        mvc.perform(get("/api/products/low-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", contains("Vacío", "Justo")));
    }
}
