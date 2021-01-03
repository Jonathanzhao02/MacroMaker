package main.java.impls.objects.components;

import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import org.jnativehook.keyboard.NativeKeyEvent;

public class WaypointKeyCell extends ListCell<Integer> {
    private ContextMenu menu = new ContextMenu();
    private MenuItem delete = new MenuItem("Delete");

    public WaypointKeyCell() {
        super();
        delete.setOnAction(e -> {
            getListView().getItems().remove(getItem());
        });
        menu.getItems().addAll(delete);
        contextMenuProperty().bind(
                Bindings.when(emptyProperty())
                .then((ContextMenu) null)
                .otherwise(menu)
        );
    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            setText(NativeKeyEvent.getKeyText(item));
        } else {
            setText(null);
        }
    }
}
