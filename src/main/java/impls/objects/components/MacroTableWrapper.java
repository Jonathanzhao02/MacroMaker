package main.java.impls.objects.components;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import main.java.impls.objects.Activator;
import main.java.impls.objects.events.SelectionEvent;
import main.java.impls.objects.hotkeys.MacroHotkey;
import main.java.interfaces.ActivationListener;
import org.jnativehook.keyboard.NativeKeyEvent;

public class MacroTableWrapper {
    private final TableView<MacroHotkey> table;

    public MacroTableWrapper(TableView<MacroHotkey> table) {
        this.table = table;
    }

    public void initialize(Activator inhibitor, Label statusLabel) {
        TableColumn<MacroHotkey, String> nameCol = new TableColumn<>("Name");
        TableColumn<MacroHotkey, String> keyCol = new TableColumn<>("Key");
        TableColumn<MacroHotkey, String> loopCol = new TableColumn<>("Loops");

        nameCol.setEditable(true);
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getName()));
        nameCol.setOnEditStart(v -> {
            inhibitor.activate();
        });
        nameCol.setOnEditCancel(v -> {
            inhibitor.deactivate();
        });
        nameCol.setOnEditCommit(v -> {
            inhibitor.deactivate();

            for (MacroHotkey m : table.getItems()) {
                if (m.getName().equals(v.getNewValue())) {
                    table.refresh();
                    return;
                }
            }

            v.getRowValue().setName(v.getNewValue());
            table.getSelectionModel().clearSelection();
            table.fireEvent(v);
        });

        keyCol.setEditable(true);
        keyCol.setCellFactory(v -> {
            MacroKeyTableCell cell = new MacroKeyTableCell(statusLabel);
            cell.addListener(new ActivationListener() {
                @Override
                public void onActivate() {
                    inhibitor.activate();
                }

                @Override
                public void onDeactivate() {
                    inhibitor.deactivate();
                }
            });
            return cell;
        });
        keyCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(NativeKeyEvent.getKeyText(p.getValue().getKey())));

        loopCol.setEditable(true);
        loopCol.setCellFactory(TextFieldTableCell.forTableColumn());
        loopCol.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(Integer.toString(p.getValue().getLoops())));
        loopCol.setOnEditStart(v -> {
            inhibitor.activate();
        });
        loopCol.setOnEditCancel(v -> {
            inhibitor.deactivate();
        });
        loopCol.setOnEditCommit(v -> {
            inhibitor.deactivate();

            try {
                int newLoops = Integer.parseInt(v.getNewValue());
                if (newLoops >= 0) v.getRowValue().setLoops(newLoops);
            } catch (Exception e) {
                table.refresh();
            }
        });

        table.setRowFactory(t -> {
            final TableRow<MacroHotkey> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem removeMenuItem = new MenuItem("Remove");
            removeMenuItem.setOnAction(e -> {
                row.getItem().setKey(-1);
                table.getItems().remove(row.getItem());
            });
            contextMenu.getItems().add(removeMenuItem);
            row.contextMenuProperty().bind(
                Bindings.when(row.emptyProperty())
                    .then((ContextMenu) null)
                    .otherwise(contextMenu)
            );

            row.setOnMouseClicked(e -> {
                if (row.isEmpty()) return;

                if (e.getButton() == MouseButton.PRIMARY) {
                    table.fireEvent(new SelectionEvent(row.getItem(), row, table.getParent()));
                }
            });

            return row;
        });

        table.getColumns().add(nameCol);
        table.getColumns().add(keyCol);
        table.getColumns().add(loopCol);
        table.setEditable(true);
        // getSelectionModel().setCellSelectionEnabled(true);
    }
}
