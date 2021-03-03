package org.elisha.orm;

@FunctionalInterface
public interface ThrowableCallback<T1, T2 , R> {

    R call(T1 t, T2 t2) throws Throwable;

}