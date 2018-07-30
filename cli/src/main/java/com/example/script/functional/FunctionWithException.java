package com.example.script.functional;

/**
 *
 * @param <T>
 * @param <R>
 * @param <E>
 *
 * @see <a href="https://www.oreilly.com/ideas/handling-checked-exceptions-in-java-streams">Handling checked exceptions in Java streams</a>
 */
@FunctionalInterface
public interface FunctionWithException<T, R, E extends Exception> {

    /**
     *
     * @param t
     * @return
     * @throws E
     */
    R apply(T t) throws E;
}