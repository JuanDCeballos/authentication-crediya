package co.juan.crediya.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ApiResponseDTO<T> {
    private String status;
    private String message;
    private List<String> errors;
    private T data;
}
