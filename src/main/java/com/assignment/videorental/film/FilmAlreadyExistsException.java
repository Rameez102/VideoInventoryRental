package com.assignment.videorental.film;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class FilmAlreadyExistsException extends RuntimeException{

    private Map<String, String> params;
}
