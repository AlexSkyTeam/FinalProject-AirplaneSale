package org.example.manager;

import lombok.AllArgsConstructor;
import org.example.authentication.Authentication;
import org.example.authenticator.Authenticator;
import org.example.dto.*;
import org.example.exception.ForbiddenException;
import org.example.exception.InvalidDataException;
import org.example.exception.NotAuthenticatedException;
import org.example.exception.PasswordNotMatchesException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class UserManager {
    private final NamedParameterJdbcTemplate template; // не изменяемые поля-final.
    private final Authenticator authenticator;
    private final PasswordEncoder passwordEncoder;

    public List<UserGetAllResponseDTO> getAll(int limit, Long offset) throws InvalidDataException {
        Authentication authentication = authenticator.authenticate();
        if (!authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            throw new ForbiddenException();
        }
        if (limit > 50) {
            throw new InvalidDataException();
        }
        if (limit <= 0) {
            throw new InvalidDataException();
        }
        if (offset < 0) {
            throw new InvalidDataException();
        }
        return template.query(
                // language=PostgreSQL
                """
                        select id, login, role from users
                        where removed = false
                        order by id
                        limit :limit offset :offset
                         """,
                Map.of(
                        "limit", limit,
                        "offset", offset
                ),
                BeanPropertyRowMapper.newInstance(UserGetAllResponseDTO.class)
        );
    }

    public UserGetByIdResponseDTO getById(long id) {
        Authentication authentication = authenticator.authenticate();
        if (!authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            throw new ForbiddenException();
        }
        return template.queryForObject(
                // language=PostgreSQL
                """
                        select id, login, role from users
                        where removed = false and id = :id  
                        """,
                Map.of("id", id),
                BeanPropertyRowMapper.newInstance(UserGetByIdResponseDTO.class)
        );
    }

    public UserRegisterResponseDTO register(UserRegisterRequestDTO requestDTO) {
        final String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());
        return template.queryForObject(
                // language=PostgreSQL
                """
                        insert into users (login, password, role)
                        values (:login, :password, :role)
                        returning id, login, role
                         """,
                Map.of(
                        "login", requestDTO.getLogin(),
                        "password", encodedPassword,
                        "role", Authentication.ROLE_USER
                ),
                BeanPropertyRowMapper.newInstance(UserRegisterResponseDTO.class)
        );
    }

    public UserMeResponseDTO me() throws NotAuthenticatedException, PasswordNotMatchesException { // - unchecked-не обязаны проверять,когда наследуется от RTE.
        Authentication authentication = authenticator.authenticate();
        return new UserMeResponseDTO(
                authentication.getId(),
                authentication.getLogin(),
                authentication.getRole()
        );
    }

    public UserRemoveByIdResponseDTO removeById(long id) throws ForbiddenException, NotAuthenticatedException, PasswordNotMatchesException {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            return template.queryForObject(
                    // language=PostgreSQL
                    // админ может пометить "удален" - любого пользователя.
                    """
                            update users set removed = true where id = :id
                            returning id, login, role
                            """,
                    Map.of("id", id),
                    BeanPropertyRowMapper.newInstance(UserRemoveByIdResponseDTO.class)
            );
        }
        if (authentication.getId() == id) {
            return template.queryForObject(
                    // language=PostgreSQL
                    // пользователь может сам себя пометить на удаление.
                    """
                            update users set removed = true where id = :id
                            returning id, login, role
                            """,
                    Map.of("id", id),
                    BeanPropertyRowMapper.newInstance(UserRemoveByIdResponseDTO.class)
            );
        }
        throw new ForbiddenException();
    }

    public UserRestoreByIdResponseDTO restoreById(long id) throws ForbiddenException, NotAuthenticatedException, PasswordNotMatchesException {
        Authentication authentication = authenticator.authenticate();
        if (!authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            throw new ForbiddenException();
        }
        return template.queryForObject(
                // language=PostgreSQL
                """
                        update users set removed = false where id = :id
                        returning id, login, role        
                        """,
                Map.of("id", id),
                BeanPropertyRowMapper.newInstance(UserRestoreByIdResponseDTO.class)
        );
    }
}