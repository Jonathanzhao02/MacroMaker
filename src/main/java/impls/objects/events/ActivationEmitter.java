package main.java.impls.objects.events;

import main.java.interfaces.ActivationListener;
import main.java.interfaces.Emitter;

public class ActivationEmitter extends Emitter<ActivationListener> {
    public void activate() {
        for (ActivationListener listener : listeners) {
            listener.onActivate();
        }
    }

    public void deactivate() {
        for (ActivationListener listener : listeners) {
            listener.onDeactivate();
        }
    }
}
