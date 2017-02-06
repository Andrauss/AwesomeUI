/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package awesomeui;

import awesomeui.animation.FadeInUpTransition;
import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static javafx.scene.input.KeyEvent.KEY_RELEASED;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Fernando Andrauss
 */
public class AwesomeUI extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Inicialização necessária para pressionar o botão ao teclar ENTER
        new EnableButtonEnterKey();

        // Carregando a tela principal
        StackPane root = new FXMLLoader(getClass().getResource("/principal.fxml")).load();
        root.setEffect(new BoxBlur()); // Aplicando o efeito de desfoque na principal

        Scene scene = new Scene(root);

        primaryStage.setTitle("Aesome UI FX!");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);

        // Carregando a tela de login
        StackPane rootLogin = new FXMLLoader(getClass().getResource("/login.fxml")).load();
        Scene sceneLogin = new Scene(rootLogin);

        Stage stageLogin = new Stage(StageStyle.TRANSPARENT);
        stageLogin.initModality(Modality.APPLICATION_MODAL);

        stageLogin.initOwner(primaryStage);
        stageLogin.setScene(sceneLogin);

        // Deixando o painel transparente para a sombra ficar visível
        stageLogin.getScene().getRoot().setStyle("-fx-background-color: transparent;");
        stageLogin.getScene().setFill(null);

        // Definindo a ação para quando o login fechar: remover o desfoque da principal
        stageLogin.setOnHiding((e) -> root.setEffect(null));

        stageLogin.show();

        // Definindo opacidade para zero inicilmente para dar o  efeito de entrada
        rootLogin.setOpacity(0);

        // Animação de entrada com delay de 200ms
        new FadeInUpTransition(rootLogin)
                .setDelayTime(Duration.millis(200))
                .setDuration(Duration.millis(500))
                .play();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public class EnableButtonEnterKey extends ButtonBehavior<Button> {

        public EnableButtonEnterKey() {
            super(new Button());
            BUTTON_BINDINGS.add(new KeyBinding(ENTER, KEY_PRESSED, "Press"));
            BUTTON_BINDINGS.add(new KeyBinding(ENTER, KEY_RELEASED, "Release"));
        }
    }

}
