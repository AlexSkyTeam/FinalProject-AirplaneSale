package org.example.authenticator;

import lombok.AllArgsConstructor;
import org.example.authentication.Authentication;
import org.example.exception.NotAuthenticatedException;
import org.example.exception.PasswordNotMatchesException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@AllArgsConstructor
public class Authenticator { // отвечает за аутонтификациюи авторизацию пользователя.
    public static final String X_LOGIN = "X-Login";
    public static final String X_PASSWORD = "X-Password";
    private final HttpServletRequest request;
    private final JdbcTemplate template;
    private final PasswordEncoder passwordEncoder; //это поле - любая реализация интерфейса PasswordEncoder

    public Authentication authenticate() throws NotAuthenticatedException, PasswordNotMatchesException {
        String login = request.getHeader(X_LOGIN);
        if (login == null) {
            return anonymous();
        }
        String password = request.getHeader(X_PASSWORD);
        if (password == null) {
            throw new NotAuthenticatedException();
        }
        Authentication authentication = template.queryForObject(
                // Language=PostgreSQL
                """
                        select id, login, password, role from users
                        where login = ? and removed = false 
                        """,
                BeanPropertyRowMapper.newInstance(Authentication.class),
                login
        );
        if (!passwordEncoder.matches(password, authentication.getPassword())) {
            throw new PasswordNotMatchesException();
        }

        authentication.setPassword("***");
        return authentication;
    }

    private Authentication anonymous() {
        return new Authentication(
                Authentication.ID_ANONYMOUS,
                "anonymous",
                "***",
                Authentication.ROLE_ANONYMOUS
        );
    }
}
