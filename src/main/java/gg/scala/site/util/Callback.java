package gg.scala.site.util;

public interface Callback<T, R> {

    R accept(T t);

}