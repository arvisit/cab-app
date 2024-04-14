package by.arvisit.cabapp.paymentservice.persistence.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import by.arvisit.cabapp.common.persistence.converter.ZonedDateTimeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "driver_payments")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class DriverPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Exclude
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(name = "driver_id", nullable = false)
    private UUID driverId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "operation", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationTypeEnum operation;

    @Column(name = "card_number", length = 16, nullable = false)
    private String cardNumber;

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum status;

    @Convert(converter = ZonedDateTimeConverter.class)
    @Column(name = "timestamp", nullable = false)
    private ZonedDateTime timestamp;
}
