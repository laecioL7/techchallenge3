package com.fiap.techchallenge.restaurant_service.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.restaurant_service.adapter.dto.RestaurantDTO;
import com.fiap.techchallenge.restaurant_service.domain.entity.Restaurant;
import com.fiap.techchallenge.restaurant_service.domain.repository.RestaurantRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private String responseMessage;

    @Given("um restaurante com nome {string}, localização {string}, tipo de cozinha {string}, horário de funcionamento {string}, e capacidade {int}")
    public void um_restaurante_com_detalhes(String name, String location, String cuisineType, String openingHours, int capacity) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setLocation(location);
        restaurant.setCuisineType(cuisineType);
        restaurant.setOpeningHours(openingHours);
        restaurant.setCapacity(capacity);
        restaurantRepository.save(restaurant);
    }

    @When("o usuário registra o restaurante")
    public void o_usuário_registra_o_restaurante() throws Exception {
        RestaurantDTO restaurantDto = new RestaurantDTO();
        restaurantDto.setName("Restaurante Teste");
        restaurantDto.setLocation("Rua Teste, 123");
        restaurantDto.setCuisineType("Italiana");
        restaurantDto.setOpeningHours("10:00-22:00");
        restaurantDto.setCapacity(100);

        responseMessage = mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @When("o usuário busca por restaurantes com localização {string} e tipo de cozinha {string}")
    public void o_usuário_busca_por_restaurantes_com_localização_e_tipo_de_cozinha(String location, String cuisineType) throws Exception {
        responseMessage = mockMvc.perform(get("/restaurants/search")
                        .param("location", location)
                        .param("cuisineType", cuisineType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @When("o usuário busca por todos os restaurantes")
    public void o_usuário_busca_por_todos_os_restaurantes() throws Exception {
        responseMessage = mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @When("o usuário busca por restaurantes com nome {string}")
    public void o_usuário_busca_por_restaurantes_com_nome(String name) throws Exception {
        responseMessage = mockMvc.perform(get("/restaurants/search")
                        .param("name", name))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Then("o status da resposta do restaurante deve ser {int}")
    public void o_status_da_resposta_do_restaurante_deve_ser(int status) throws Exception {
        mockMvc.perform(post("/restaurants"))
                .andExpect(status().is(status));
    }

    @Then("a resposta deve conter os detalhes do restaurante")
    public void a_resposta_deve_conter_os_detalhes_do_restaurante() {
        assertTrue(responseMessage.contains("\"name\":\"Restaurante Teste\""));
        assertTrue(responseMessage.contains("\"location\":\"Rua Teste, 123\""));
        assertTrue(responseMessage.contains("\"cuisineType\":\"Italiana\""));
        assertTrue(responseMessage.contains("\"openingHours\":\"10:00-22:00\""));
        assertTrue(responseMessage.contains("\"capacity\":100"));
    }

    @Then("a resposta deve conter a lista de restaurantes")
    public void a_resposta_deve_conter_a_lista_de_restaurantes() {
        assertTrue(responseMessage.contains("\"name\":\"Restaurante Teste\""));
    }

    @Then("a resposta deve conter a lista de todos os restaurantes")
    public void a_resposta_deve_conter_a_lista_de_todos_os_restaurantes() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        for (Restaurant restaurant : restaurants) {
            assertTrue(responseMessage.contains("\"name\":\"" + restaurant.getName() + "\""));
        }
    }
}