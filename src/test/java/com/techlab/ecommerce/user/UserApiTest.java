package com.techlab.ecommerce.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserApiTest {

    @Autowired
    private MockMvc mvc;

    private ResultActions createUser(String json) throws Exception {
        return mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(json));
    }

    @Test
    void newUsersAreClientsByDefaultAndEmailIsLowercased() throws Exception {
        createUser("{\"name\": \"Ana\", \"email\": \"Ana@Mail.com\"}")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ana@mail.com"))
                .andExpect(jsonPath("$.role").value("CLIENT"));
    }

    @Test
    void sameEmailWithDifferentCaseIs409() throws Exception {
        createUser("{\"name\": \"Ana\", \"email\": \"ana@mail.com\"}").andExpect(status().isCreated());

        createUser("{\"name\": \"Otra Ana\", \"email\": \"ANA@mail.com\"}")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("A user with email ana@mail.com already exists"));
    }

    @Test
    void invalidEmailIs400() throws Exception {
        createUser("{\"name\": \"Ana\", \"email\": \"no-es-un-email\"}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void unknownRoleIsRejected() throws Exception {
        createUser("{\"name\": \"Ana\", \"email\": \"ana@mail.com\", \"role\": \"SUPERUSER\"}")
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteThenGetIs404() throws Exception {
        String body = createUser("{\"name\": \"Ana\", \"email\": \"ana@mail.com\", \"role\": \"ADMIN\"}")
                .andReturn().getResponse().getContentAsString();
        long id = ((Number) JsonPath.read(body, "$.id")).longValue();

        mvc.perform(delete("/api/users/{id}", id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/users/{id}", id)).andExpect(status().isNotFound());
    }
}
