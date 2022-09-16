package koredebank.example.bank.Email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class EmailResponse {

    private String message;

    private String status;
}
