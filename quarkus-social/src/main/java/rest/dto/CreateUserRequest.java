package rest.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Name is Required!") // verifica se é nula ou vazia
    private String name;
    @NotNull(message = "Age is Required!")
    private Integer age;
}
