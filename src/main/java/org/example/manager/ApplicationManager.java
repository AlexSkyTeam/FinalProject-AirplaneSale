package org.example.manager;

import lombok.AllArgsConstructor;
import org.example.authentication.Authentication;
import org.example.authenticator.Authenticator;
import org.example.dto.*;
import org.example.exception.ForbiddenException;
import org.example.exception.InvalidDataException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class ApplicationManager {

    private final NamedParameterJdbcTemplate template;
    private final Authenticator authenticator;

    public ApplicationCreateResponseDTO create(ApplicationCreateRequestDTO requestDTO) {
        return template.queryForObject(
                // language=PostgreSQL
                // создать заявку.
                """
                        insert into applications (airplane_id, email)
                        values (:airplane_id, :email)
                        returning id, airplane_id, email, processed
                        """,
                Map.of(
                        "airplane_id", requestDTO.getAirplaneId(),
                        "email", requestDTO.getEmail()
                ),
                BeanPropertyRowMapper.newInstance(ApplicationCreateResponseDTO.class)
        );
    }

    public List<ApplicationGetAllResponseDTO> getAll(int limit, long offset) throws InvalidDataException {
        Authentication authentication = authenticator.authenticate();
        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
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
                    // получить все заявки, только админ.
                    """
                            select id, airplane_id, email, processed from applications
                            order by id
                            limit :limit offset :offset
                            """,
                    Map.of(
                            "limit", limit,
                            "offset", offset
                    ),
                    BeanPropertyRowMapper.newInstance(ApplicationGetAllResponseDTO.class)
            );
        }
        throw new ForbiddenException();
    }

    public List<ApplicationGetAllByStatusResponseDTO> getAllByStatus(int limit, long offset, boolean status) throws InvalidDataException {
        Authentication authentication = authenticator.authenticate();
        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
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
                    //получить все заявки со статусом
                    """
                            select id, airplane_id, email, processed from applications
                            where processed = :status
                            order by id
                            limit :limit offset :offset 
                            """,
                    Map.of(
                            "limit", limit,
                            "offset", offset,
                            "status", status
                    ),
                    BeanPropertyRowMapper.newInstance(ApplicationGetAllByStatusResponseDTO.class)
            );
        }
        throw new ForbiddenException();
    }

    public ApplicationUpdateResponseDTO update(ApplicationUpdateRequestDTO requestDTO) {
        return template.queryForObject(
                // language=PostgreSQL
                // обновить заявку по id
                """
                        update applications
                        set airplane_id = :airplane_id, email = :email, processed = :processed
                        where id = :id
                        returning id, airplane_id, email, processed
                        """,
                Map.of(
                        "airplane_id", requestDTO.getAirplaneId(),
                        "email", requestDTO.getEmail(),
                        "processed", requestDTO.isProcessed(),
                        "id", requestDTO.getId()
                ),
                BeanPropertyRowMapper.newInstance(ApplicationUpdateResponseDTO.class)
        );
    }

    public ApplicationGetByIdResponseDTO getById(long id) {
        return template.queryForObject(
                // language=PostgreSQL
                // запрос завки по id заявки.
                """
                        select id, airplane_id, email, processed from applications
                        where id = :id  
                        """,
                Map.of("id", id),
                BeanPropertyRowMapper.newInstance(ApplicationGetByIdResponseDTO.class)
        );
    }
}