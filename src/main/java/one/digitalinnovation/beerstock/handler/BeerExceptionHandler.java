package one.digitalinnovation.beerstock.handler;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstock.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundException;
import one.digitalinnovation.beerstock.exception.BeerStockExceededException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@ControllerAdvice
public class BeerExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<BeerProblem.Field> fields = new ArrayList<>();

        for (ObjectError error: ex.getBindingResult().getAllErrors()) {
            String name = ((FieldError) error).getField();
            String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());

            fields.add(new BeerProblem.Field(name, message));
        }

        BeerProblem beerProblem = new BeerProblem();
        beerProblem.setStatus(status.value());
        beerProblem.setDateTime(LocalDateTime.now());
        beerProblem.setTitle("One or more fields are invalid. Fill it in correctly and try again");
        beerProblem.setFields(fields);

        return handleExceptionInternal(ex, beerProblem, headers, status, request);
    }

    @ExceptionHandler(BeerNotFoundException.class)
    public ResponseEntity<Object> handlerBeerNotFoundException(BeerNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        BeerProblem beerProblem = new BeerProblem();
        beerProblem.setStatus(status.value());
        beerProblem.setDateTime(LocalDateTime.now());
        beerProblem.setTitle(ex.getMessage());

        return handleExceptionInternal(ex, beerProblem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BeerAlreadyRegisteredException.class)
    public ResponseEntity<Object> handlerBeerAlreadyRegisteredException(BeerAlreadyRegisteredException ex,
                                                                 WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        BeerProblem beerProblem = new BeerProblem();
        beerProblem.setStatus(status.value());
        beerProblem.setDateTime(LocalDateTime.now());
        beerProblem.setTitle(ex.getMessage());

        return handleExceptionInternal(ex, beerProblem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(BeerStockExceededException.class)
    public ResponseEntity<Object> handlerBeerStockExceededException(BeerStockExceededException ex,
                                                                 WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        BeerProblem beerProblem = new BeerProblem();
        beerProblem.setStatus(status.value());
        beerProblem.setDateTime(LocalDateTime.now());
        beerProblem.setTitle(ex.getMessage());

        return handleExceptionInternal(ex, beerProblem, new HttpHeaders(), status, request);
    }

}
