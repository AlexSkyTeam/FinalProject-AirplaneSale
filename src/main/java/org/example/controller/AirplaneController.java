package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.*;
import org.example.manager.AirplaneManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/airplane")
public class AirplaneController {
    private final AirplaneManager manager;

    @RequestMapping("/getAll")
    public List<AirplaneGetAllResponseDTO> getAll(int limit, long offset) {
        return manager.getAll(limit, offset);
    }

    @RequestMapping("/getById")
    public AirplaneGetByIdResponseDTO getById(long id) {
        return manager.getById(id);
    }

    @RequestMapping("/create")
    public AirplaneCreateResponseDTO create(AirplaneCreateRequestDTO requestDTO) {
        return manager.create(requestDTO);
    }

    @RequestMapping("/update")
    public AirplaneUpdateResponseDTO update(AirplaneUpdateRequestDTO requestDTO) {
        return manager.update(requestDTO);
    }

    @RequestMapping("/removeById")
    public AirplaneRemoveByIdResponseDTO removeById(long id) {
        return manager.removeById(id);
    }

    @RequestMapping("/restoreById")
    public AirplaneRestoreByIdResponseDTO restoreById(long id) {
        return manager.restoreById(id);
    }

    @RequestMapping("/getAllRemoved")
    public List<AirplaneGetAllRemovedResponseDTO> getAllRemoved(int limit, long offset) {
        return manager.getAllRemoved(limit, offset);
    }
}