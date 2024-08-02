package kg.bektur.todoapp;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SecurityScheme(
        name = "keycloak",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(authorizationCode = @OAuthFlow(
                authorizationUrl = "${keycloak.uri}/realms/todo/protocol/openid-connect/auth",
                tokenUrl = "${keycloak.uri}/realms/todo/protocol/openid-connect/token",
                scopes = {
                        @OAuthScope(name = "openid"),
                        @OAuthScope(name = "microprofile-jwt"),
                        @OAuthScope(name = "task_view"),
                        @OAuthScope(name = "task_edit")
                }
        ))
)
@SpringBootApplication
public class TodoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);
    }

}
