package main.java.interfaces;

import java.util.HashSet;
import java.util.Set;

public class Emitter<T> {
    protected final Set<T> listeners;

    public Emitter() {
        listeners = new HashSet<>();
    }

    public void addListener(T listener) {
        listeners.add(listener);
    }

    public void removeListener(T listener) {
        listeners.remove(listener);
    }
}
