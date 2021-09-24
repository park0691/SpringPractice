package springpractice.template.test;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
