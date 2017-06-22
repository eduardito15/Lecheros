/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Edu
 */
public class Ref<T> {

    private T value;

    public Ref(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T anotherValue) {
        value = anotherValue;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return value.equals(obj);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
