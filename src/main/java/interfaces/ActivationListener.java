package main.java.interfaces;

import java.util.EventListener;

public interface ActivationListener extends EventListener {
    void onActivate();
    void onDeactivate();
}
