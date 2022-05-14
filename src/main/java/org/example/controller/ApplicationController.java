package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.*;
import org.example.manager.ApplicationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationManager manager;

    @RequestMapping("/create")
    public ApplicationCreateResponseDTO create(ApplicationCreateRequestDTO requestDTO) {
        return manager.create(requestDTO);
    }

    @RequestMapping("/getAll")
    public List<ApplicationGetAllResponseDTO> getAll(int limit, long offset) {
        return manager.getAll(limit, offset);
    }

    @RequestMapping("/getAllByStatus")
    public List<ApplicationGetAllByStatusResponseDTO> getAllByStatus(int limit, long offset, boolean status) {
        return manager.getAllByStatus(limit, offset, status);
    }

    @RequestMapping("/update")
    public ApplicationUpdateResponseDTO update(ApplicationUpdateRequestDTO requestDTO) {
        return manager.update(requestDTO);
    }

    @RequestMapping("/getById")
    public ApplicationGetByIdResponseDTO getById(long id) {
        return manager.getById(id);
    }
}
