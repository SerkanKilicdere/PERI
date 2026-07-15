package com.serkan.peri.configuration.exceptionconfiguration;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


import java.util.List;

@Getter
@Setter
@Builder
@Slf4j
@Component // <--- Add this to make it injectable
@NoArgsConstructor // Required for @Builder/@Component combinations sometimes
@AllArgsConstructor // Required for @Builder
public class ErrorMessage {
    Integer errorCode;
    String errorMessage;
    Boolean isDone;
    List<String> inputFields;

    public  ResponseEntity<ErrorMessage> errorMessageReceived(Exception exception, ErrorType errorType, HttpStatus httpStatus, List<String> inputFields){
        log.error("Error message : {}", errorType.getErrorMessage()+ " - Fields : " +inputFields);
        return new ResponseEntity<>(ErrorMessage.builder()
                .errorCode(errorType.getApplicationErrorCode())
                .inputFields(inputFields)
                .isDone(false)
                .errorMessage(errorType.getErrorMessage())
                .build(),httpStatus);
    }

}
