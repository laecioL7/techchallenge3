package com.fiap.techchallenge.restaurant_service.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.restaurant_service.adapter.dto.ReservationDto;
import com.fiap.techchallenge.restaurant_service.domain.entity.Reservation;
import com.fiap.techchallenge.restaurant_service.domain.repository.ReservationRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationRepository reservationRepository;

    private String responseMessage;

    @Given("uma reserva com ID {string}, usuário com ID {string}, restaurante com ID {string}, horário {string}, e número de pessoas {int}")
    public void uma_reserva_com_detalhes(String reservationId, String userId, String restaurantId, String dateTime, int numberOfPeople) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setUserId(userId);
        reservation.setRestaurantId(restaurantId);
        reservation.setReservationTime(LocalDateTime.parse(dateTime));
        reservation.setNumberOfPeople(numberOfPeople);
        reservationRepository.save(reservation);
    }

    @When("o usuário faz a reserva")
    public void o_usuário_faz_a_reserva() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setUserId("1");
        reservationDto.setRestaurantId("1");
        reservationDto.setReservationTime(LocalDateTime.now().plusDays(1));
        reservationDto.setNumberOfPeople(4);

        responseMessage = mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @When("o usuário visualiza as reservas pelo ID do usuário {string}")
    public void o_usuário_visualiza_as_reservas_pelo_ID_do_usuário(String userId) throws Exception {
        responseMessage = mockMvc.perform(get("/reservations/user/" + userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @When("o usuário visualiza as reservas pelo ID do restaurante {string}")
    public void o_usuário_visualiza_as_reservas_pelo_ID_do_restaurante(String restaurantId) throws Exception {
        responseMessage = mockMvc.perform(get("/reservations/restaurant/" + restaurantId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @When("o usuário atualiza a reserva com ID {string} para horário {string} e número de pessoas {int}")
    public void o_usuário_atualiza_a_reserva(String reservationId, String dateTime, int numberOfPeople) throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setReservationTime(LocalDateTime.parse(dateTime));
        reservationDto.setNumberOfPeople(numberOfPeople);

        responseMessage = mockMvc.perform(put("/reservations/" + reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @When("o usuário cancela a reserva com ID {string}")
    public void o_usuário_cancela_a_reserva(String reservationId) throws Exception {
        responseMessage = mockMvc.perform(delete("/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Then("o status da resposta deve ser {int}")
    public void o_status_da_resposta_deve_ser(int status) throws Exception {
        mockMvc.perform(post("/reservations"))
                .andExpect(status().is(status));
    }


    @Then("a resposta deve conter os detalhes da reserva")
    public void a_resposta_deve_conter_os_detalhes_da_reserva() {
        // Verifique se a resposta contém os detalhes esperados da reserva
        assertTrue(responseMessage.contains("\"userId\":\"1\""));
        assertTrue(responseMessage.contains("\"restaurantId\":\"1\""));
        assertTrue(responseMessage.contains("\"numberOfPeople\":4"));
    }

    @Then("a resposta deve conter a lista de reservas do usuário")
    public void a_resposta_deve_conter_a_lista_de_reservas_do_usuário() {
        // Verifique se a resposta contém a lista esperada de reservas do usuário
        assertTrue(responseMessage.contains("\"userId\":\"1\""));
    }

    @Then("a resposta deve conter a lista de reservas do restaurante")
    public void a_resposta_deve_conter_a_lista_de_reservas_do_restaurante() {
        // Verifique se a resposta contém a lista esperada de reservas do restaurante
        assertTrue(responseMessage.contains("\"restaurantId\":\"1\""));
    }
}
