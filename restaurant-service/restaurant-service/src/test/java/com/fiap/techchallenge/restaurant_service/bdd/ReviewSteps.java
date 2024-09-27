package com.fiap.techchallenge.restaurant_service.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.restaurant_service.adapter.dto.ReviewDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String responseMessage;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Given("que o usuário com ID {string} existe")
    public void que_o_usuário_com_ID_existe(String userId) {
        // Não faz nada, pois esse serviço não valida o usuário
    }

    @Given("o restaurante com ID {string} existe")
    public void o_restaurante_com_ID_existe(String restaurantId) {
        restaurantRepository.save(getRestaurant());
    }

    @When("o usuário submete uma avaliação com nota {int} e comentário {string}")
    public void o_usuário_submete_uma_avaliação_com_nota_e_comentário(int rating, String comment) throws Exception {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setUserId("1");
        reviewDto.setRestaurantId("1");
        reviewDto.setRating(rating);
        reviewDto.setComment(comment);

        responseMessage = mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Then("o status da resposta da avaliação deve ser {int}")
    public void o_status_da_resposta_da_avaliação_deve_ser(int status) throws Exception {
        mockMvc.perform(post("/reviews"))
                .andExpect(status().is(status));
    }

    @Then("a mensagem da resposta da avaliação deve ser {string}")
    public void a_mensagem_da_resposta_da_avaliação_deve_ser(String message) {
        assert responseMessage.equals(message);
    }

    Restaurant getRestaurant(){
        Restaurant restaurant = new Restaurant();
        restaurant.setId("1");
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("123 Test St");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("10:00-22:00");
        restaurant.setCapacity(100);
        return restaurant;
    }
}