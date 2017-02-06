package awesomeui;

import awesomeui.animation.FadeOutDownTransition;
import awesomeui.animation.ShakeTransition;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Fernando Andrauss
 */
public class LoginController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField tfUsuario;

    @FXML
    private PasswordField tfSenha;

    @FXML
    void close(ActionEvent event) {
         // Aplicando a animação de saida
        new FadeOutDownTransition(rootPane)
                .setOnFinish((e) -> {
                    ((Stage) tfUsuario.getScene().getWindow()).close();
                    Platform.exit();
                })
                .setDelayTime(Duration.ZERO)
                .setDuration(Duration.millis(300))
                .play();

    }

    @FXML
    void help(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ajuda não disponível", ButtonType.OK);
        alert.setTitle("Ajuda");
        alert.setHeaderText("Ajuda");
        alert.show();
    }

    @FXML
    void login(ActionEvent event) {
        
        tfUsuario.setId(null);
        tfSenha.setId(null);
        
        if (tfUsuario.getText().equals("admin") && tfSenha.getText().equals("admin")) {
            
             // Aplicando a animação de entrada
            new FadeOutDownTransition(rootPane)
                    .setOnFinish((e) -> {

                        ((Stage) tfUsuario.getScene().getWindow()).close();
                    })
                    .setDelayTime(Duration.ZERO)
                    .setDuration(Duration.millis(300))
                    .play();

        } else {
            
             // Aplicando a animação de validação
            new ShakeTransition(rootPane)
                    .setDelayTime(Duration.ZERO)
                    .setDuration(Duration.millis(800))
                    .play();

            if (!tfUsuario.getText().equals("admin")) {
                tfUsuario.setId("text-field-validation-error");
            }

            if (!tfSenha.getText().equals("admin")) {
                tfSenha.setId("text-field-validation-error");
            }

        }
    }

    @FXML
    void minimize(ActionEvent event) {
        ((Stage) ((Stage) tfUsuario.getScene().getWindow()).getOwner()).setIconified(true);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Método chamado na leitura do FXML
    }

}
