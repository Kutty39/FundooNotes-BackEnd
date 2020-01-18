package com.blbz.fundoonotebackend.exception;

import com.blbz.fundoonotebackend.responce.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import javax.validation.ValidationException;

@RestControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> generalExceptionResponse(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<?> invalidException(InvalidUserException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(HeaderMissingException.class)
    public ResponseEntity<?> headerMissing(HeaderMissingException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> invalidToken(InvalidTokenException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> tokenExpired(TokenExpiredException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<?> tokenExpired(MessagingException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<?> illegalAccess(IllegalAccessException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<?> noteNotFound(NoteNotFoundException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, e.getMessage()));
    }

    @ExceptionHandler(InvalidNoteStatus.class)
    public ResponseEntity<?> noteNotFound(InvalidNoteStatus e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(LabelNotFoundException.class)
    public ResponseEntity<?> labelNotFound(LabelNotFoundException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, e.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> jwtExpired(ExpiredJwtException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(NoteStatusNotFoundException.class)
    public ResponseEntity<?> noteStatusNotFound(NoteStatusNotFoundException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT, e.getMessage()));
    }

    @ExceptionHandler(ParameterEmptyException.class)
    public ResponseEntity<?> noteStatusNotFound(ParameterEmptyException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> validationException(ValidationException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> validationException(SignatureException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, e.getMessage()));
    }
}
