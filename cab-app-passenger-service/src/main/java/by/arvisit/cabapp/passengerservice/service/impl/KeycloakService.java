package by.arvisit.cabapp.passengerservice.service.impl;

import java.util.Collections;
import java.util.List;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import by.arvisit.cabapp.exceptionhandlingstarter.exception.UsernameAlreadyExistsException;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {

    private static final int FULLNAME_PARTS_COUNT = 2;
    private static final String PASSENGER_ROLE = "PASSENGER";
    private static final String EMAIL_ALREADY_IN_USE_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.passengerservice.service.impl.KeycloakService.UsernameAlreadyExistsException.template";
    private static final String NOT_FULLNAME_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.passengerservice.service.impl.KeycloakService.IllegalArgumentException.template";

    private final Keycloak keycloak;
    private final MessageSource messageSource;

    @Value("${keycloak.realm}")
    private String realm;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String client;

    public String addUser(PassengerRequestDto dto) {
        String username = dto.email();
        CredentialRepresentation credential = createPasswordCredentials(dto.password());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(username);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        setName(user, dto.name());

        UsersResource usersResource = getUsersResource();
        String id;
        if (usersResource.search(username).isEmpty()) {
            try (Response response = usersResource.create(user)) {
                String path = response.getLocation().getPath();
                id = path.substring(path.lastIndexOf('/') + 1);
            }
        } else {
            String errorMessage = messageSource.getMessage(
                    EMAIL_ALREADY_IN_USE_MESSAGE_TEMPLATE_KEY,
                    new Object[] { dto.email() }, null);
            throw new UsernameAlreadyExistsException(errorMessage);
        }

        addClientRoleToUser(username, PASSENGER_ROLE);
        log.debug("User with id {} was registered", id);
        return id;
    }

    private void addClientRoleToUser(String userName, String roleName) {
        RealmResource realmResource = keycloak.realm(realm);
        // In order to correctly get client and its roles ClientRepresentation should be
        // created first after that initialize ClientResource otherwise HTTP 404 will be
        // returned on attempt to get client roles
        ClientRepresentation clientRepresentation = realmResource.clients().findByClientId(client).get(0);
        ClientResource clientResource = realmResource.clients().get(clientRepresentation.getId());
        List<UserRepresentation> users = realmResource.users().search(userName);
        UserResource userResource = realmResource.users().get(users.get(0).getId());
        RoleRepresentation role = clientResource.roles().get(roleName).toRepresentation();
        RoleMappingResource roleMappingResource = userResource.roles();
        roleMappingResource.clientLevel(clientRepresentation.getId()).add(Collections.singletonList(role));
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private void setName(UserRepresentation user, String name) {
        String[] parts = name.split(" ");
        String firstName;
        String lastName;
        if (parts.length != FULLNAME_PARTS_COUNT) {
            String errorMessage = messageSource.getMessage(
                    NOT_FULLNAME_MESSAGE_TEMPLATE_KEY,
                    new Object[] { }, null);
            throw new IllegalArgumentException(errorMessage);
        } else {
            firstName = parts[0];
            lastName = parts[1];
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
    }
}
