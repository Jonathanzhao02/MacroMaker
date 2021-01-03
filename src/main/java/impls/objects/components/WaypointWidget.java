package main.java.impls.objects.components;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.java.impls.objects.Activator;
import main.java.impls.objects.Waypoint;
import main.java.impls.objects.controllers.WaypointEditMenuController;
import main.java.impls.objects.events.SelectionEvent;

import java.awt.*;
import java.io.IOException;

public class WaypointWidget extends Circle {
    private Waypoint waypoint;
    private ContextMenu waypointEditMenu = new ContextMenu();
    private Menu add = new Menu("Add new waypoint");
    private MenuItem addBefore = new MenuItem("Before");
    private MenuItem addAfter = new MenuItem("After");
    private MenuItem edit = new MenuItem("Edit");
    private MenuItem delete = new MenuItem("Delete");
    private Stage editStage = new Stage();
    private int before;
    private int after;
    private Activator inhibitor;

    public WaypointWidget(Waypoint waypoint, int before, int after, int x, int y, double r, Color fill, Activator inhibitor) {
        super(x, y, r, fill);
        this.waypoint = waypoint;
        this.before = before;
        this.after = after;
        this.inhibitor = inhibitor;
        setCursor(Cursor.HAND);
        setOnContextMenuRequested(e -> {
            waypointEditMenu.show(this, getCenterX(), getCenterY());
            e.consume();
        });

        addBefore.setOnAction(e -> {
            fireEvent(new SelectionEvent(before + 1, this, getParent()));
            e.consume();
        });

        addAfter.setOnAction(e -> {
            fireEvent(new SelectionEvent(after, this, getParent()));
            e.consume();
        });

        addBefore.setDisable(this.before == -1);
        addAfter.setDisable(this.after == -1);

        add.getItems().addAll(addBefore, addAfter);

        edit.setOnAction(e -> {
            showEditMenu();
            e.consume();
        });

        delete.setOnAction(e -> {
            fireEvent(new SelectionEvent(this.waypoint, this, getParent()));
            fireEvent(new ActionEvent());
            e.consume();
        });

        setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                showEditMenu();
            }

            e.consume();
        });

        setOnMouseDragged(e -> {
            setCenterX(e.getScreenX());
            setCenterY(e.getScreenY());
            waypoint.setLocation(new Point((int) e.getScreenX(), (int) e.getScreenY()));
            fireEvent(new ActionEvent());
            e.consume();
        });

        waypointEditMenu.getItems().addAll(add, edit, delete);
    }

    public void showEditMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/main/resources/waypoint_editor.fxml")
            );
            Scene scene = new Scene(loader.load());
            editStage.setScene(scene);

            WaypointEditMenuController controller = loader.getController();
            controller.initData(waypoint, this, inhibitor);

            editStage.setTitle("Waypoint Editor");
            editStage.show();
        } catch (IOException e) {
            System.out.println("COULD NOT LOAD EDIT MENU");
            e.printStackTrace();
        }
    }

    public void closeEditMenu() {
        editStage.close();
        setCenterX(waypoint.getX());
        setCenterY(waypoint.getY());
        fireEvent(new ActionEvent());
    }
}
