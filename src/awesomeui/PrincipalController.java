package awesomeui;

import awesomeui.components.FloatMenu;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Fernando Andrauss
 */
public class PrincipalController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnCheck;

    @FXML
    private Button btnConfig;

    @FXML
    private Button btnExit;

    FloatMenu floatMenu;

    private List<FloatMenu.FloatMenuItem> cadastroItens;
    private List<FloatMenu.FloatMenuItem> checkItens;
    private List<FloatMenu.FloatMenuItem> configItens;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cadastroItens = getRandomMenus();
        checkItens = getRandomMenu3();
        configItens = getRandomMenu2();

        floatMenu = new FloatMenu();

        btnAdd.setOnAction((e) -> {
            showFloatMenu(btnAdd, cadastroItens);
        });

        btnCheck.setOnAction((e) -> {
            showFloatMenu(btnCheck, checkItens);
        });

        btnConfig.setOnAction((e) -> {
            showFloatMenu(btnConfig, configItens);
        });

        btnExit.setOnAction((e) -> Sair());

//        Mostrar menu no hover        
//        btnCheck.setOnMouseEntered((e) -> {
//            showFloatMenu(btnCheck, getCadastroItems3());
//        });
//
//        btnConfig.setOnMouseEntered((e) -> {
//            showFloatMenu(btnConfig, getCadastroItems2());
//        }); 
//        
//        btnAdd.setOnMouseEntered((e) -> {
//            
//            showFloatMenu(btnAdd, getCadastroItems());
//        });
    }

    private void Sair() {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Deseja realmente sair?",
                ButtonType.YES, ButtonType.NO);

        alert.setHeaderText("Sair");

        alert.showAndWait()
                .ifPresent((btn) -> {
                    if (btn.getButtonData().equals(ButtonBar.ButtonData.YES)) {
                        ((Stage) btnExit.getScene().getWindow()).close();
                        Platform.exit();
                    }
                });
    }

    private void showAlert() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText("Informação");
        a.setContentText("Ação não definida");
        a.show();
    }

    private List<FloatMenu.FloatMenuItem> getRandomMenus() {
        MaterialDesignIconView iconProduto = new MaterialDesignIconView(MaterialDesignIcon.PACKAGE_VARIANT);
        iconProduto.setSize("30");

        MaterialDesignIconView iconClientes = new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_MULTIPLE);
        iconClientes.setSize("30");

        FloatMenu.FloatMenuItem menuProduto
                = new FloatMenu.FloatMenuItem("Produto", iconProduto, (a) -> showAlert());

        FloatMenu.FloatMenuItem menuCliente
                = new FloatMenu.FloatMenuItem("Cliente", iconClientes, (a) -> showAlert());

        return Arrays.asList(menuProduto, menuCliente);
    }

    private List<FloatMenu.FloatMenuItem> getRandomMenu2() {
        MaterialDesignIconView iconProduto = new MaterialDesignIconView(MaterialDesignIcon.PACKAGE_VARIANT);
        iconProduto.setSize("30");

        MaterialDesignIconView iconClientes = new MaterialDesignIconView(MaterialDesignIcon.ACCOUNT_MULTIPLE);
        iconClientes.setSize("30");

        List<FloatMenu.FloatMenuItem> menus = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            menus.add(new FloatMenu.FloatMenuItem("Menu " + (i + 1), getRandomIcon()));
        }

        return menus;
    }

    private GlyphIcon getRandomIcon() {
        Random r = new Random();
        MaterialDesignIconView iconDefault = new MaterialDesignIconView(MaterialDesignIcon.APPLE);
        iconDefault.setSize("30");
        switch (r.nextInt((4 - 1) + 1) + 1) {
            case 1: {
                System.out.println("1");
                MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.STAR);
                icon.setSize("30");
                return icon;
            }
            case 2: {
                System.out.println("2");
                MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.CASH_MULTIPLE);
                icon.setSize("30");
                return icon;
            }

            case 3: {
                System.out.println("3");
                MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.CLOUD_OUTLINE);
                icon.setSize("30");
                return icon;
            }

            case 4: {
                System.out.println("4");
                MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.CAMERA_PARTY_MODE);
                icon.setSize("30");
                return icon;
            }

            default: {
                return iconDefault;
            }
        }
    }

    private List<FloatMenu.FloatMenuItem> getRandomMenu3() {
        MaterialDesignIconView iconProduto = new MaterialDesignIconView(MaterialDesignIcon.CASH);
        iconProduto.setSize("30");

        MaterialDesignIconView iconClientes = new MaterialDesignIconView(MaterialDesignIcon.BARCODE);
        iconClientes.setSize("30");

        MaterialDesignIconView icContas = new MaterialDesignIconView(MaterialDesignIcon.BANK);
        icContas.setSize("30");

        return Arrays.asList(
                new FloatMenu.FloatMenuItem("Financeiro", iconProduto),
                new FloatMenu.FloatMenuItem("Faturamento", iconClientes),
                new FloatMenu.FloatMenuItem("Contas", icContas)
        );
    }

    private void showFloatMenu(Node ref, List<FloatMenu.FloatMenuItem> cadastroItems) {
        floatMenu.show(ref, rootPane, cadastroItems);
    }

}
