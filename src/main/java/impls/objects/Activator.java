package main.java.impls.objects;

import main.java.impls.objects.events.ActivationEmitter;

public class Activator extends ActivationEmitter {
    private boolean on;
    private boolean everOn;

    @Override
    public void activate() {
        on = true;
        everOn = true;
        super.activate();
    }

    @Override
    public void deactivate() {
        on = false;
        super.deactivate();
    }

    public boolean switchState() {
        if (on) {
            deactivate();
        } else {
            activate();
        }

        return !on;
    }

    public boolean isOn() {
        return on;
    }

    public boolean wasOn() {
        return everOn;
    }

}
