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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String PASSENGER_ROLE = "PASSENGER";
    private static final String EMAIL_ALREADY_IN_USE_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.passengerservice.persistence.model.service.impl.KeycloakService.UsernameAlreadyExistsException.template";

    private final Keycloak keycloak;
    private final MessageSource messageSource;

    @Value("${keycloak.realm}")
    private String realm;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String client;

    public void addUser(PassengerRequestDto dto) {
        String username = dto.email();
        CredentialRepresentation credential = createPasswordCredentials(dto.password());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(username);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        // FIXME replace user name stubbing with actual implementation
        user.setFirstName("j");
        user.setLastName("d");

        UsersResource usersResource = getUsersResource();
        if (usersResource.search(username).isEmpty()) {
            usersResource.create(user);
        } else {
            String errorMessage = messageSource.getMessage(
                    EMAIL_ALREADY_IN_USE_MESSAGE_TEMPLATE_KEY,
                    new Object[] { dto.email() }, null);
            throw new UsernameAlreadyExistsException(errorMessage);
        }

        addClientRoleToUser(username, PASSENGER_ROLE);
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

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

}
