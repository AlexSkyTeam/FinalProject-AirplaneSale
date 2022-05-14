package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AirplaneUpdateRequestDTO {
    private long id;
    private String name;
    private int year;
    private String photo;
    private int price;
}
