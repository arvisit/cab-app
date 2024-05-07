package by.arvisit.cabapp.common.persistence.converter;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

import by.arvisit.cabapp.common.util.CommonConstants;
import jakarta.persistence.AttributeConverter;

public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime attribute) {
        return attribute == null ? null : Timestamp.valueOf(attribute.toLocalDateTime());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp dbData) {
        return dbData == null ? null : dbData.toLocalDateTime().atZone(CommonConstants.EUROPE_MINSK_TIMEZONE);
    }

}
