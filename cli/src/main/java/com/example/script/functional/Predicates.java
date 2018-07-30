package com.example.script.functional;

import lombok.extern.log4j.Log4j2;

import javax.validation.ConstraintViolationException;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * @see <a href="https://www.oreilly.com/ideas/handling-checked-exceptions-in-java-streams">Handling checked exceptions in Java streams</a>
 */
@Log4j2
public class Predicates {

    public static <T, R, E extends Exception>
    Predicate<T> safePredicate(PredicateWithException<T, E> pe, boolean defaultValue) {
        return arg -> {
            try {
                return pe.test(arg);
            } catch (Exception e) {
                if (e instanceof ConstraintViolationException) {
                    log.debug("invalid item '{}'", arg);
                    ((ConstraintViolationException) e).getConstraintViolations().stream()
                            .sorted(Comparator.comparing(v -> v.getPropertyPath().toString()))
                            .forEach(v -> log.debug(v.getMessage()));
                } else {
                    log.debug("an error was encountered while testing item '{}'", arg);
                    log.debug(e.getMessage());
                }
                log.debug("skipping item");
                return defaultValue;
            }
        };
    }
}
