package com.example.script.functional;

/**
 *
 * @param <T>
 * @param <E>
 *
 * @see <a href="https://www.oreilly.com/ideas/handling-checked-exceptions-in-java-streams">Handling checked exceptions in Java streams</a>
 */
@FunctionalInterface
public interface PredicateWithException<T, E extends Throwable> {

    /**
     *
     * @param t
     * @return
     * @throws E
     */
    boolean test(T t) throws E;
}