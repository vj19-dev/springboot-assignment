package com.weatherforecast.city.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "City Not Found")
public class Messages extends RuntimeException {
    private String message;
    public Messages(String message){
        this.message = message;
    }
}
