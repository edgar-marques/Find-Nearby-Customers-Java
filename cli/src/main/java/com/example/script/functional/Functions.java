package com.example.script.functional;

import lombok.extern.log4j.Log4j2;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Comparator;
import java.util.function.Function;

/**
 * @see <a href="https://www.oreilly.com/ideas/handling-checked-exceptions-in-java-streams">Handling checked exceptions in Java streams</a>
 */
@Log4j2
public class Functions {

    /**
     *
     * @param fe
     * @param defaultValue
     * @param <T>
     * @param <R>
     * @param <E>
     * @return
     */
    public static <T, R, E extends Exception>
    Function<T, R> safeCall(FunctionWithException<T, R, E> fe, R defaultValue) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (Exception e) {
                if (e instanceof ConstraintViolationException) {
                    log.debug("invalid item '{}'", arg);
                    ((ConstraintViolationException) e).getConstraintViolations().stream()
                            .sorted(Comparator.comparing(v -> v.getPropertyPath().toString()))
                            .forEach(v -> log.debug(v.getMessage()));
                } else {
                    log.debug("an error was encountered while processing item '{}'", arg);
                    log.debug(e.getMessage());
                }
                log.debug("skipping item");
                return defaultValue;
            }
        };
    }
}
