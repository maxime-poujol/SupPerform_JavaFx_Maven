package fr.montpellier.supperform.Affichage;
import fr.montpellier.supperform.FenetreAlert;
import fr.montpellier.supperform.Main;
import fr.montpellier.supperform.javaFX.ButtonFX;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


public class Accueil extends Group {

    public Accueil (Main main){

        double width = 150, height = 40, transX = main.getWidth()/2 - width /2, transY = main.getHeight()/2 - height /2;

        ButtonFX boutonNotationNormale = new ButtonFX("Notation normale", width, height, transX, transY - 120);
        ButtonFX boutonNotationUE5 = new ButtonFX("Notation UE5", width, height, transX, transY - 60);
        ButtonFX boutonId = new ButtonFX("Identifiants", width, height, transX, transY);
        ButtonFX support = new ButtonFX("Support", width, height, transX, transY + 60);
        ButtonFX boutonQuitter = new ButtonFX("Quitter", width, height, transX, transY + 120);


        boutonId.setOnAction(actionEvent -> {
            Scene identifiant = new Scene(new StackPane(new Identifiant(main)), main.getWidth(), main.getHeight());
            identifiant.setFill(Color.LIGHTGRAY);
            identifiant.getStylesheets().add("/Button.css");
            main.getStage().setTitle("Sup'Perform Identifiant");
            main.getStage().setScene(identifiant);
        });

        boutonNotationNormale.setOnAction(actionEvent -> {
            Scene notationNormale = new Scene(new StackPane(new Notation(main, 0.25)), main.getWidth(), main.getHeight());
            notationNormale.setFill(Color.LIGHTGRAY);
            notationNormale.getStylesheets().add("/Button.css");
            main.getStage().setTitle("Sup'Perform Notation Normale");
            main.getStage().setScene(notationNormale);
        });

        boutonNotationUE5.setOnAction(actionEvent -> {
            Scene notationUE5 = new Scene(new StackPane(new Notation(main, 0.5)), main.getWidth(), main.getHeight());
            notationUE5.setFill(Color.LIGHTGRAY);
            notationUE5.getStylesheets().add("/Button.css");
            main.getStage().setTitle("Sup'Perform Notation UE5");
            main.getStage().setScene(notationUE5);
        });

        support.setOnAction(actionEvent -> {
            FenetreAlert.info("Pour tout problème ou question, vous pouvez me contacter à l'adresse mail suivante : \n\nmax.poujol21@gmail.com \n");
        });


        boutonQuitter.setOnAction(actionEvent -> System.exit(0));


        this.getChildren().addAll(boutonNotationNormale, boutonNotationUE5, boutonId, support, boutonQuitter);


    }


}
