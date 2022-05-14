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

import java.util.*;

@Component
@AllArgsConstructor
public class AirplaneManager {
    private final NamedParameterJdbcTemplate template;
    private final Authenticator authenticator;

    public List<AirplaneGetAllResponseDTO> getAll(int limit, long offset) throws InvalidDataException {
        if (limit > 100) {
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
                // админ может получать все самолеты.
                """
                        select id, name, maxSpeed, weight, year, price from airplanes
                        where removed = false
                        order by id
                        limit :limit offset :offset
                        """,
                Map.of(
                        "limit", limit,
                        "offset", offset
                ),
                BeanPropertyRowMapper.newInstance(AirplaneGetAllResponseDTO.class)
        );
    }

    public AirplaneGetByIdResponseDTO getById(long id) {
        return template.queryForObject(
                // language=PostgreSQL
                """
                        select id, name, maxspeed, weight, year, photo, price from airplanes
                        where removed = false and id = :id  
                        """,
                Map.of("id", id),
                BeanPropertyRowMapper.newInstance(AirplaneGetByIdResponseDTO.class)
        );
    }

    public AirplaneCreateResponseDTO create(AirplaneCreateRequestDTO requestDTO) {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        return template.queryForObject(
                // language=PostgreSQL
                """
                        insert into airplanes (name, maxSpeed, weight, year, photo, price)
                        values (:name, :maxSpeed, :weight, :year, :photo, :price)
                        returning id, name, maxSpeed, weight, year, photo, price
                        """,
                Map.of(
                        "name", requestDTO.getName(),
                        "maxSpeed", requestDTO.getMaxSpeed(),
                        "weight", requestDTO.getWeight(),
                        "year", requestDTO.getYear(),
                        "photo", requestDTO.getPhoto(),
                        "price", requestDTO.getPrice()
                ),
                BeanPropertyRowMapper.newInstance(AirplaneCreateResponseDTO.class)
        );
    }

    public AirplaneUpdateResponseDTO update(AirplaneUpdateRequestDTO requestDTO) {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        return template.queryForObject(
                // language=PostgreSQL
                """
                        update airplanes
                        set name = :name, year = :year, photo = :photo, price = :price
                        where removed = false and id = :id
                        returning id, name, year, photo, price
                        """,
                Map.of(
                        "id", requestDTO.getId(),
                        "name", requestDTO.getName(),
                        "year", requestDTO.getYear(),
                        "photo", requestDTO.getPhoto(),
                        "price", requestDTO.getPrice()
                ),
                BeanPropertyRowMapper.newInstance(AirplaneUpdateResponseDTO.class)
        );
    }

    public AirplaneRemoveByIdResponseDTO removeById(long id) {
        Authentication authentication = authenticator.authenticate();
        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            return template.queryForObject(
                    // language=PostgreSQL
                    // админ удаляет навсегда! Все, кто не admin - exception!
                    """
                            delete from airplanes 
                            where id = :id
                            returning id, name, maxspeed, weight, year, photo, price
                            """,
                    Map.of("id", id),
                    BeanPropertyRowMapper.newInstance(AirplaneRemoveByIdResponseDTO.class)
            );
        }
        if (authentication.getRole().equals(Authentication.ROLE_USER)) {
            return template.queryForObject(
                    // language=PostgreSQL
                    // пользователь помещает в корзину! Все, кто не admin - exception!
                    """
                             update airplanes 
                             set removed = true 
                             where id = :id  
                             returning id, name, maxspeed, weight, year, photo, price
                            """,
                    Map.of("id", id),
                    BeanPropertyRowMapper.newInstance(AirplaneRemoveByIdResponseDTO.class)
            );
        }
        throw new ForbiddenException();
    }

    public AirplaneRestoreByIdResponseDTO restoreById(long id) {
        Authentication authentication = authenticator.authenticate();
        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            return template.queryForObject(
                    // language=PostgreSQL
                    // админ востанавливает по id. Все, кто не admin - exception!
                    """
                            update airplanes set removed = false where id = :id
                            returning id, name, maxspeed, weight, year, photo, price
                            """,
                    Map.of("id", id),
                    BeanPropertyRowMapper.newInstance(AirplaneRestoreByIdResponseDTO.class)
            );
        }
        throw new ForbiddenException();
    }

    public List<AirplaneGetAllRemovedResponseDTO> getAllRemoved(int limit, long offset) {
        final Authentication authentication = authenticator.authenticate();
        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            return template.query(
                    //language=PostgreSQL
                    // админ может запрашивать удаленные объекты. Все, кто не admin - exception!
                    """
                            select id, name, maxSpeed, weight, year, photo, price from airplanes
                            where removed = true
                            order by  id
                            limit :limit offset :offset
                            """,
                    Map.of(
                            "limit", limit,
                            "offset", offset
                    ),
                    BeanPropertyRowMapper.newInstance(AirplaneGetAllRemovedResponseDTO.class)
            );
        }
        throw new ForbiddenException();
    }
}
