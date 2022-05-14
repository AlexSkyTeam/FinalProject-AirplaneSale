package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.*;
import org.example.exception.ForbiddenException;
import org.example.exception.InvalidDataException;
import org.example.exception.NotAuthenticatedException;
import org.example.exception.PasswordNotMatchesException;
import org.example.manager.UserManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private UserManager manager;

    @RequestMapping("/getAll")
    public List<UserGetAllResponseDTO> getAll(int limit, long offset) throws InvalidDataException { // -это checked Exception! Для чистоты кода trows... можно убрать,т.е. сделать unchecked Exception, но я оставил для пояснения.
        return manager.getAll(limit, offset);
    }

    @RequestMapping("/getById")
    public UserGetByIdResponseDTO getById(long id) {
        return manager.getById(id);
    }

    @RequestMapping("/register")
    public UserRegisterResponseDTO register(UserRegisterRequestDTO requestDTO) {
        return manager.register(requestDTO);
    }

    @RequestMapping("/me")
    public UserMeResponseDTO me() throws NotAuthenticatedException, PasswordNotMatchesException {
        return manager.me();
    }

    @RequestMapping("/removeById")
    public UserRemoveByIdResponseDTO removeById(long id) throws ForbiddenException, NotAuthenticatedException, PasswordNotMatchesException {
        return manager.removeById(id);
    }

    @RequestMapping("/restoreById")
    public UserRestoreByIdResponseDTO restoreById(long id) throws ForbiddenException, NotAuthenticatedException, PasswordNotMatchesException {
        return manager.restoreById(id);
    }
}