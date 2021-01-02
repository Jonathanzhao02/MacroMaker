package main.java;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    public void start(Stage primaryStage) throws IOException {
        Parent primaryRoot = FXMLLoader.load(getClass().getResource("/main/resources/macro_maker_frontend.fxml"));
        primaryStage.setScene(new Scene(primaryRoot));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            try {
                System.out.println("QUITTING");
                GlobalScreen.unregisterNativeHook();
                Platform.runLater(() -> {
                    System.exit(0);
                });
                Platform.exit();
            } catch (NativeHookException nativeHookException) {
                nativeHookException.printStackTrace();
                System.exit(1);
            }
        });
        System.out.println("Started!");
    }

    public static void main(String[] args) {
        // Disable logging
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        // Global Listener Setup
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(-1);
        }

        launch(args);
    }
}