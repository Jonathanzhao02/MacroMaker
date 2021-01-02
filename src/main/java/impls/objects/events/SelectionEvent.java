package main.java.impls.objects.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class SelectionEvent extends Event {
    public static final EventType<SelectionEvent> SELECTION_EVENT = new EventType<>("SELECTION_EVENT");
    private Object obj;


    public SelectionEvent(Object obj, Object source, EventTarget target) {
        super(source, target, SELECTION_EVENT);
        this.obj = obj;
    }

    public Object getObject() {
        return obj;
    }
}
