package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApplicationUpdateRequestDTO {
    private long id;
    private long airplaneId;
    private boolean isProcessed;
    private String email;
}
