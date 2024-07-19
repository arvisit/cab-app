package by.arvisit.cabapp.ridesservice.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import by.arvisit.cabapp.common.security.AppUser;
import by.arvisit.cabapp.common.util.AppUserRole;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RideSecurityVerifier {

    private static final String NOT_FOUND_FOR_ILLEGAL_PRINCIPAL_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.principal.EntityNotFoundException.template";

    private final MessageSource messageSource;

    public void verifyAllowedForTheRidePassenger(Ride ride) {
        AppUser principal = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!UUID.fromString(principal.getId()).equals(ride.getPassengerId())) {
            throwExceptionForIllegalPrincipal(ride);
        }
    }

    public void verifyAllowedForTheRideDriver(Ride ride) {
        AppUser principal = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (ride.getDriverId() != null && !UUID.fromString(principal.getId()).equals(ride.getDriverId())) {
            throwExceptionForIllegalPrincipal(ride);
        }
    }

    public void verifyAllowedForTheRideDriverOrMessageBroker(Ride ride) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }
        AppUser principal = (AppUser) authentication.getPrincipal();
        if (ride.getDriverId() != null && !UUID.fromString(principal.getId()).equals(ride.getDriverId())) {
            throwExceptionForIllegalPrincipal(ride);
        }
    }

    public void verifyAllowedForRidePassengerOrDriverOrAdmin(Ride ride) {
        AppUser principal = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> authorities = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        if (!authorities.contains(AppUserRole.ROLE_ADMIN.toString())
                && (!UUID.fromString(principal.getId()).equals(ride.getPassengerId())
                        || !UUID.fromString(principal.getId()).equals(ride.getDriverId()))) {
            throwExceptionForIllegalPrincipal(ride);
        }
    }

    private void throwExceptionForIllegalPrincipal(Ride ride) {
        String errorMessage = messageSource.getMessage(
                NOT_FOUND_FOR_ILLEGAL_PRINCIPAL_MESSAGE_TEMPLATE_KEY,
                new Object[] { ride.getId() }, null);
        throw new EntityNotFoundException(errorMessage);
    }
}
