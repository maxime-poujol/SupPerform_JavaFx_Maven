package fr.montpellier.supperform.javaFX;

import fr.montpellier.supperform.Main;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class SceneFX extends Scene {

    public SceneFX(Parent parent) {
        super(new StackPaneFX(parent), Main.WIDTH, Main.HEIGHT);
        this.setFill(Color.LIGHTGRAY);
        this.getStylesheets().add("/Button.css");
    }


}
