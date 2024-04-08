package by.arvisit.cabapp.ridesservice.persistence.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rides")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(name = "initial_cost", nullable = false)
    private BigDecimal initialCost;

    @Column(name = "final_cost", nullable = false)
    private BigDecimal finalCost;

    @Column(name = "passenger_id", nullable = false)
    private UUID passengerId;

    @Column(name = "driver_id", nullable = true)
    private UUID driverId;

    @ManyToOne
    @JoinColumn(name = "promo_code_id", nullable = true)
    private PromoCode promoCode;

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private RideStatusEnum status;

    @Column(name = "payment_method", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid;

    @Column(name = "start_address", length = 100, nullable = false)
    private String startAddress;

    @Column(name = "destination_address", length = 100, nullable = false)
    private String destinationAddress;

    @Column(name = "passenger_score", nullable = true)
    private Integer passengerScore;

    @Column(name = "driver_score", nullable = true)
    private Integer driverScore;

    @Column(name = "book_ride", nullable = false)
    private ZonedDateTime bookRide;

    @Column(name = "cancel_ride", nullable = true)
    private ZonedDateTime cancelRide;

    @Column(name = "accept_ride", nullable = true)
    private ZonedDateTime acceptRide;

    @Column(name = "begin_ride", nullable = true)
    private ZonedDateTime beginRide;

    @Column(name = "end_ride", nullable = true)
    private ZonedDateTime endRide;

    @Column(name = "finish_ride", nullable = true)
    private ZonedDateTime finishRide;
}
