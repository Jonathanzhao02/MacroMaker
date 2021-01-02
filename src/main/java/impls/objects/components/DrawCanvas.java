package main.java.impls.objects.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.awt.*;
import java.util.List;

public class DrawCanvas extends Canvas {
    public DrawCanvas() {
        super();
        getGraphicsContext2D().setFont(new Font("Arial Regular", 12));
        getGraphicsContext2D().setTextAlign(TextAlignment.CENTER);
    }

    public void drawPoints(List<Point> points, Color color) {
        for (Point point : points) {
            drawPixel(point.x, point.y, color);
        }
    }

    public void drawOvals(List<Point> points, Color color) {
        for (Point point : points) {
            drawOval(point.x, point.y, color);
        }
    }

    public void drawTexts(List<String> texts, List<Point> points, Color ovalFill, Color textFill) {
        for (int i = 0; i < texts.size(); i++) {
            String text = texts.get(i);
            Point point = points.get(i);
            drawText(text, point.x, point.y, ovalFill, textFill);
        }
    }

    public void drawPixel(int x, int y, Color color) {
        getGraphicsContext2D().setFill(color);
        getGraphicsContext2D().fillRect(x, y, 1, 1);
    }

    public void drawOval(int x, int y, Color color) {
        getGraphicsContext2D().setFill(color);
        getGraphicsContext2D().fillOval(x - 8, y - 8, 17, 17);
    }

    public void drawText(String text, int x, int y, Color ovalFill, Color textFill) {
        drawOval(x, y, ovalFill);
        getGraphicsContext2D().setFill(textFill);
        getGraphicsContext2D().fillText(text, x, y + 4, 17);
    }

    public void clear() {
        getGraphicsContext2D().clearRect(0,0,getWidth(),getHeight());
    }
}
