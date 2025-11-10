package com.example.demo.scrim.controller;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.scrim.dto.CreateScrimRequest;
import com.example.demo.scrim.dto.PlayerConfirmationInfo;
import com.example.demo.scrim.dto.ScrimResponse;
import com.example.demo.scrim.dto.ScrimSearchRequest;
import com.example.demo.scrim.service.ScrimService;
import com.example.demo.shared.dto.ErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scrim")
public class ScrimController {

    @Autowired
    private ScrimService scrimService;

    @GetMapping
    public ResponseEntity<?> searchScrims(
            @RequestParam(required = false) Long gameId,
            @RequestParam(required = false) String formatType,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long minTierId,
            @RequestParam(required = false) Long maxTierId,
            @RequestParam(required = false) ScrimStatus status) {
        try {
            ScrimSearchRequest searchRequest = new ScrimSearchRequest();
            searchRequest.setGameId(gameId);
            searchRequest.setFormatType(formatType);
            searchRequest.setRegionId(regionId);
            searchRequest.setMinTierId(minTierId);
            searchRequest.setMaxTierId(maxTierId);
            searchRequest.setStatus(status);

            List<ScrimResponse> scrims = scrimService.searchScrims(searchRequest);
            return ResponseEntity.ok(scrims);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> createScrim(@Valid @RequestBody CreateScrimRequest request) {
        try {
            ScrimResponse scrim = scrimService.createScrim(
                    request.getFormatType(),
                    request.getMinTierId(),
                    request.getMaxTierId(),
                    request.getRegionId(),
                    request.getScheduledTime());
            return ResponseEntity.status(HttpStatus.CREATED).body(scrim);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScrimById(@PathVariable Long id) {
        try {
            ScrimResponse scrim = scrimService.getScrimById(id);
            return ResponseEntity.ok(scrim);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<?> applyToScrim(@PathVariable Long id) {
        try {
            ScrimResponse scrim = scrimService.applyToScrim(id);
            return ResponseEntity.ok(scrim);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirmParticipation(@PathVariable Long id) {
        try {
            ScrimResponse scrim = scrimService.confirmParticipation(id);
            return ResponseEntity.ok(scrim);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/{id}/confirmations")
    public ResponseEntity<?> getConfirmations(@PathVariable Long id) {
        try {
            List<PlayerConfirmationInfo> confirmations = scrimService.getConfirmations(id);
            return ResponseEntity.ok(confirmations);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelScrim(@PathVariable Long id) {
        try {
            ScrimResponse scrim = scrimService.cancelScrim(id);
            return ResponseEntity.ok(scrim);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<?> startScrim(@PathVariable Long id) {
        try {
            ScrimResponse scrim = scrimService.startScrim(id);
            return ResponseEntity.ok(scrim);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<?> finishScrim(@PathVariable Long id) {
        try {
            ScrimResponse scrim = scrimService.finishScrim(id);
            return ResponseEntity.ok(scrim);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Invalid input data",
                errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
