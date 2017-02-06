package awesomeui.components;

import awesomeui.animation.FadeInDownTransition;
import awesomeui.animation.FadeOutDownTransition;
import awesomeui.animation.FadeOutTransition;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Componente de menu flutuante
 *
 * @author Fernando Andrauss
 */
public class FloatMenu extends StackPane implements Initializable {

    @FXML
    private VBox boxBallon;

    @FXML
    private FlowPane paneList;

    @FXML
    private Button btnCancel;

    @FXML
    private Rectangle arrow;

    private Pane root;

    private ChangeListener<Number> listnerTranslateX;

    private List<FloatMenuItem> itens;

    private StackPane paneToShow; // Painel onde o menu será mostrado

    private boolean showing = false;

    public FloatMenu() {
        LoadMenu();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Definindo a ação do botão cancelar
        btnCancel.setOnAction((e) -> {
            hide((evt) -> {
                if (paneToShow != null) {
                    paneToShow.getChildren().remove(this);
                }
            });
        });
    }

    /**
     * Carregar o layout apartir do FXML
     */
    private void LoadMenu() {
        getChildren().removeAll(getChildren());
        try {
            getStylesheets().add("/style.css");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu_float.fxml"));
            loader.setController(this);

            root = loader.load();

            getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(FloatMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Define os itens do menu
     *
     * @param itens
     */
    public void setItems(List<FloatMenuItem> itens) {
        LoadMenu();

        this.itens = itens;

        // Limpa a lista de menus
        paneList.getChildren().clear();

        // Define um tamanho fixo para uma lista de até 2 itens
        if (itens.size() <= 2) {
            boxBallon.setPrefWidth(150);
            root.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        } else {
            boxBallon.setPrefWidth(USE_COMPUTED_SIZE);
            root.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        }

        // Adiciona os itens à lista
        createItens(itens);

        // Fixa o tamanho do balão
        setMaxSize(root.getWidth(), root.getHeight());
    }

    /**
     * Adiciona os itens no balão
     *
     * @param itens
     */
    private void createItens(List<FloatMenuItem> itens) {
        for (FloatMenuItem iten : itens) {
            iten.setMenu(this);
            paneList.getChildren().add(createMenuItem(iten));
        }
    }

    /**
     * Método de criação do layout do Item do balão
     *
     * @param item modelo de item
     * @return
     */
    public Node createMenuItem(FloatMenuItem item) {

        BorderPane bp = new BorderPane();
        bp.setFocusTraversable(true);

        bp.setOnMouseClicked((e) -> {

            if (item.getAction() != null) {
                item.getAction().handle(e);
            }
            item.getMenu().hide(null);

        });

        bp.setOnKeyPressed((e) -> {
            if (e.getCode().equals(KeyCode.ENTER) || e.getCode().equals(KeyCode.ESCAPE)) {
                if (item.getAction() != null) {
                    item.getAction().handle(e);
                }
                item.getMenu().hide(null);
            }

        });

        bp.setPrefSize(40, 40);
        bp.setMaxSize(40, 40);
        bp.getStyleClass().add("btn-sub-menu");

        bp.setCenter(item.getIcon());

        Label lb = new Label(item.getLabel());
        lb.setTextFill(Color.WHITE);

        VBox pane = new VBox(bp, lb);
        pane.setAlignment(Pos.CENTER);

        return (pane);
    }

    /**
     * Método para esconder o balao
     *
     * @param onfinish ação após esconder
     */
    public void hide(EventHandler<ActionEvent> onfinish) {
        showing = false;
        itens = null;
        new FadeOutDownTransition(this, getTranslateY())
                // Evento padrão: remover o balão do painel 
                .setOnFinish((e) -> {
                    
                    if (paneToShow != null) {
                        paneToShow.getChildren().remove(this);
                    }
                    
                    if (onfinish != null) {
                        onfinish.handle(e);
                    }
                })
                .setDelayTime(Duration.ZERO)
                .setDuration(Duration.millis(100))
                .play();
    }

    /**
     * Método para mostrar o balão
     *
     * @param ref botão de referência
     * @param rootPane painel onde o balão será mostrado
     * @param floatMenuItems itens do menu
     */
    public void show(Node ref, StackPane rootPane, List<FloatMenuItem> floatMenuItems) {

        // Se o balão estiver visível e os itens forem os mesmos, ele é escondido
        if (itens != null && itens.containsAll(floatMenuItems) && isShowing()) {
            hide(null);
            return;
        }

        // Se o balão estiver visível, deve-se: escondê-lo depois mostrá-lo na nova posição
        if (isShowing()) {
            hide((e) -> {
                doShow(ref, rootPane, floatMenuItems);
            });
        } else {
            // Caso o balão não esteja visível somente mostra
            doShow(ref, rootPane, floatMenuItems);
        }

    }

    /**
     * Mostra o Balão relativo à posição do botão que o solicitou
     *
     * @param ref botão de referência
     * @param rootPane painel onde o balão será mostrado
     * @param floatMenuItems itens do menu (os itens sempre devem ser passados
     * para que se calcula a área que o balão irá ocupar e a posição que ele
     * deve aparecer)
     */
    private void doShow(Node ref, StackPane rootPane, List<FloatMenuItem> floatMenuItems) {

        // guardando referência do painel pai
        paneToShow = rootPane;

        rootPane.getChildren().remove(this); //Removendo o balão do painel

        // Definindo a posição em que o balão será adicionado
        rootPane.setAlignment(Pos.TOP_LEFT);

        setItems(floatMenuItems);

        // Definindo a posição x e y para 0 (para ser recalculada posteriormente)
        setTranslateX(0);
        setTranslateY(0);

        translateX(ref);

        listnerTranslateX = (v, o, n) -> {
            translateX(ref);

            // removendo o evento após sua execução
            widthProperty().removeListener(listnerTranslateX);
        };

        // Adicionando um evento para a posição X 
        // (quando ela mudar o balão será reajustado para a posição correta)
        widthProperty().addListener(listnerTranslateX);

        // Definindo a posição Y ( +80 para não ficar colado no botão )
        Double translateY
                = (ref.getParent().getBoundsInParent().getMinY() + ref.getBoundsInParent().getMinY() + 80);

        setTranslateY(translateY);

        // Adicionando o balão ao painel pai
        rootPane.getChildren().add(this);

        // Definindo a opacidade zero para o a animação de entrada
        setOpacity(0);

        // Rodando a animação de entrada 
        new FadeInDownTransition(this, translateY)
                .setDelayTime(Duration.ZERO)
                .setOnFinish((e) -> {
                    showing = true;
                })
                .setDuration(Duration.millis(100))
                .play();

    }

    private void translateX(Node ref) {
        Double tX = (ref.getParent().getBoundsInParent().getMinX() + ref.getBoundsInParent().getMinX())
                - ((getWidth() / 2) + (arrow.getBoundsInParent().getMinX() - 40));

        setTranslateX(tX);
    }

    public boolean isShowing() {
        return showing;
    }

    /**
     * Classe para definição de menus
     */
    public static class FloatMenuItem {

        private String label;
        private Node icon;
        private EventHandler action;
        private FloatMenu menu;

        public FloatMenuItem(String label, Node icon) {
            this.label = label;
            this.icon = icon;
        }

        public FloatMenuItem(String label, Node icon, EventHandler action) {
            this.label = label;
            this.icon = icon;
            this.action = action;
        }

        public FloatMenuItem() {
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Node getIcon() {
            return icon;
        }

        public void setIcon(Node icon) {
            this.icon = icon;
        }

        public EventHandler getAction() {
            return action;
        }

        public void setAction(EventHandler action) {
            this.action = action;
        }

        public FloatMenu getMenu() {
            return menu;
        }

        public void setMenu(FloatMenu menu) {
            this.menu = menu;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + Objects.hashCode(this.label);
            hash = 31 * hash + Objects.hashCode(this.icon);
            hash = 31 * hash + Objects.hashCode(this.action);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final FloatMenuItem other = (FloatMenuItem) obj;
            if (!Objects.equals(this.label, other.label)) {
                return false;
            }
            if (!Objects.equals(this.icon, other.icon)) {
                return false;
            }
            if (!Objects.equals(this.action, other.action)) {
                return false;
            }
            return true;
        }

    }

}
