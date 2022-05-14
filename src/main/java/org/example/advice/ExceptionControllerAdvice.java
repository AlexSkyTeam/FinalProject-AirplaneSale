package org.example.advice;

import org.example.dto.ExceptionDTO;
import org.example.exception.ForbiddenException;
import org.example.exception.InvalidDataException;
import org.example.exception.NotAuthenticatedException;
import org.example.exception.PasswordNotMatchesException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401 - проблема с аутентификацией или авторизацией на сайте.Указанные имя пользователя и пароль не верны.
    public ExceptionDTO handle(NotAuthenticatedException e) {
        e.printStackTrace();
        return new ExceptionDTO("item.not_authenticated");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400 - некорректный запрос серверу.
    public ExceptionDTO handle(PasswordNotMatchesException e) {
        e.printStackTrace();
        return new ExceptionDTO("item.password_not_matches");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED) //509 - логическая ошибка.
    public ExceptionDTO handle(InvalidDataException e) {
        e.printStackTrace();
        return new ExceptionDTO("item.data_limit_violated");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //404 - не найдено.
    public ExceptionDTO handle(IOException e) {
        e.printStackTrace();
        return new ExceptionDTO("item.io_is_broken");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN) //403 - ограничение или отсутствие доступа к материалу на загружаемой странице.
    public ExceptionDTO handle(ForbiddenException e) {
        e.printStackTrace();
        return new ExceptionDTO("item.access_restrictions");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500 - внутренняя серверная проблема, запрос клиента не может быть обработан.
    public ExceptionDTO handle(Exception e) {
        e.printStackTrace();
        return new ExceptionDTO("global.internal_error");
    }
}
