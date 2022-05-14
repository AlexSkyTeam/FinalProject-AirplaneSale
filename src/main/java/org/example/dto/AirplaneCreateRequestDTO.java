package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AirplaneCreateRequestDTO {
    private String name;
    private int maxSpeed;
    private int weight;
    private int year;
    private String photo;
    private int price;
}
