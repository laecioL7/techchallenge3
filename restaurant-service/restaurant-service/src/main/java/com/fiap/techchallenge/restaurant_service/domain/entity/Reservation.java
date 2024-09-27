package com.fiap.techchallenge.restaurant_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;
    private String restaurantId;
    private String userId;
    private LocalDateTime reservationTime;
    private int numberOfPeople;

    //para não dar problema no teste comparando a data que vem do banco com milisegundos a mais
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        if (numberOfPeople != that.numberOfPeople) return false;
        if (!restaurantId.equals(that.restaurantId)) return false;
        if (!userId.equals(that.userId)) return false;
        return reservationTime.truncatedTo(ChronoUnit.MINUTES).equals(that.reservationTime.truncatedTo(ChronoUnit.MINUTES));
    }

    //hashCode
    @Override
    public int hashCode() {
        int result = restaurantId.hashCode();
        result = 31 * result + userId.hashCode();
        result = 31 * result + reservationTime.hashCode();
        result = 31 * result + numberOfPeople;
        return result;
    }

}
