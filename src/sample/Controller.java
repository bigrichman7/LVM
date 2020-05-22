package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Controller {

    @FXML
    Button highlighting;
    @FXML
    Button logicOr;

    @FXML
    Button logicAnd;

    @FXML
    Button logicNot;

    @FXML
    Button arrow;

    @FXML
    Button circle;

    @FXML
    Button fictitiousCircle;

    @FXML
    Button propagatedCircle;

    @FXML
    Button back;

    @FXML
    Button forward;

    @FXML
    TextField condition;


    boolean circleDown = false;
    boolean fictitiousCircleDown = false;
    boolean propagatedCircleDown = false;
    boolean logicOrDown = false;
    boolean logicAndDown = false;
    boolean logicNotDown = false;

    TempCircle tempCircle;
    TempArrow tempArrow;
    static List<TempCircle> tempCircleArray = new ArrayList<>();

    int interNumber;

    ContextMenu contextMenu;

    int pressedCircleCounter = 0;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane scrollPane;

    @FXML
    void logicFunction(ActionEvent event) throws IOException {
        Stage logicFunctionStage = new Stage();
        ScrollPane root = FXMLLoader.load(getClass().getResource("logicFunction.fxml"));
        logicFunctionStage.setTitle("Вывод логической функции");
        logicFunctionStage.setScene(new Scene(root, 1000, 600));
        logicFunctionStage.show();
    }

    @FXML
    void probabilityFunction(ActionEvent event) {

    }

    @FXML
    void relevanceFunction(ActionEvent event) {

    }

    @FXML
    void contributionFunction(ActionEvent event) {

    }

    @FXML
    void calculateProbability(ActionEvent event) {

    }

    @FXML
    void getReport(ActionEvent event) {

    }

    @FXML
    void arrow(ActionEvent event) {

    }

    @FXML
    void back(ActionEvent event) {

    }

    @FXML
    void getAllCircles(ActionEvent event) {
        for (int i = 0; i < tempCircleArray.size(); i++) {
            TempCircle tc = tempCircleArray.get(i);
            System.out.println("Номер круга: " + Integer.parseInt(tc.getNumber().getCharacters().toString()));
            System.out.println("        Тип круга: " + tc.getTypeCircle());
            if (tc.getTypeCircle().equals("propagatedCircle")) {
                System.out.println("        Индекс круга: " + tc.getIndex());
            }
            if (tc.getArrowContainer().size() > 0) {
                System.out.println("        Свзязанные вершины:");
                System.out.println("        " + tc.getArrowContainer().toString());
            }
            System.out.println();
            System.out.println();
        }
    }

    @FXML
    void circl(ActionEvent event) {
        if (!circleDown) {
            circleDown = true;
            buttonDisabler(circle);
            tempCircle = new TempCircle("circle");
            scrollPane.getChildren().add(tempCircle.getTempCircle());
        } else {
            scrollPane.getChildren().remove(tempCircle.getTempCircle());
            circleDown = false;
            buttonEnabler();
        }
    }

    @FXML
    void fictitiousCircle(ActionEvent event) {
        if (!fictitiousCircleDown) {
            fictitiousCircleDown = true;
            buttonDisabler(fictitiousCircle);
            tempCircle = new TempCircle("fictitiousCircle");
            scrollPane.getChildren().add(tempCircle.getTempCircle());
        } else {
            scrollPane.getChildren().remove(tempCircle.getTempCircle());
            fictitiousCircleDown = false;
            buttonEnabler();
        }
    }

    @FXML
    void forward(ActionEvent event) {

    }

    @FXML
    void highlighting(ActionEvent event) {

    }

    @FXML
    void logicAnd(ActionEvent event) {
        if (!logicAndDown) {
            logicAndDown = true;
            buttonDisabler(logicAnd);
            if (!logicNotDown) {
                tempArrow = new TempArrow("logicAnd");
            } else {
                tempArrow = new TempArrow("logicAndWithNot");
            }

        } else {
            logicAndDown = false;
            buttonEnabler();
        }
    }

    @FXML
    void logicNot(ActionEvent event) {
        logicNotDown = true;
    }

    @FXML
    void logicOr(ActionEvent event) {
        if (!logicOrDown) {
            logicOrDown = true;
            buttonDisabler(logicOr);
            if (!logicNotDown) {
                tempArrow = new TempArrow("logicOr");
            } else {
                tempArrow = new TempArrow("logicOrWithNot");
            }
        } else {
            logicOrDown = false;
            buttonEnabler();
        }
    }

    @FXML
    void propagatedCircle(ActionEvent event) {
        if (!propagatedCircleDown) {
            propagatedCircleDown = true;
            buttonDisabler(propagatedCircle);
            tempCircle = new TempCircle("propagatedCircle");
            scrollPane.getChildren().add(tempCircle.getTempCircle());
        } else {
            scrollPane.getChildren().remove(tempCircle.getTempCircle());
            propagatedCircleDown = false;
            buttonEnabler();
        }
    }

    @FXML
    void initialize() {

    }

    private void setContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem number = new MenuItem("Присвоить номер");
        number.setOnAction(actionEvent -> {
            TextField textField = new TextField();
            textField.setLayoutX(tempCircle.getTempCircle().getX() + 20);
            textField.setLayoutY(tempCircle.getTempCircle().getY() + 18);
            textField.setMaxWidth(30);
            textField.setStyle("-fx-border-color: white; -fx-background-color: white; -fx-font-size: 13; -fx-padding: 0;");
            tempCircle.setNumber(textField);
            scrollPane.getChildren().add(textField);
            textField.requestFocus();
        });

        MenuItem index = new MenuItem("Присвоить индекс");
        index.setOnAction(actionEvent -> {
            TextField textField = new TextField();
            textField.setLayoutX(tempCircle.getTempCircle().getX() + 35);
            textField.setLayoutY(tempCircle.getTempCircle().getY() + 30);
            textField.setMaxWidth(13);
            textField.setStyle("-fx-border-color: white; -fx-background-color: white; -fx-font-size: 8; -fx-padding: 0;");
            tempCircle.setIndex(textField);
            scrollPane.getChildren().add(textField);
            textField.requestFocus();
        });

        MenuItem delete = new MenuItem("Удалить элемент");
        delete.setOnAction(actionEvent -> {
            scrollPane.getChildren().remove(tempCircle.getNumber());
            scrollPane.getChildren().remove(tempCircle.getIndex());
            scrollPane.getChildren().remove(tempCircle.getTempCircle());
            scrollPane.getChildren().remove(tempCircle.getTempCircle());
        });

        contextMenu.getItems().addAll(number, index, delete);
    }

    private void addListener(TempCircle tempCircle) {
        tempCircle.getTempCircle().setOnMouseDragged(event -> {
            tempCircle.setXY(event.getX(), event.getY());
        });

        tempCircle.getTempCircle().setOnMouseClicked(event -> {
            if (logicOrDown || logicAndDown) {
                if (pressedCircleCounter == 0) {
                    tempArrow.startArrow(tempCircle.getTempCircle().getX(), tempCircle.getTempCircle().getY());
                    interNumber = Integer.parseInt(tempCircle.getNumber().getCharacters().toString());
                    pressedCircleCounter++;
                } else if (pressedCircleCounter == 1) {
                    tempArrow.endArrow(tempCircle.getTempCircle().getX(), tempCircle.getTempCircle().getY());
                    tempCircle.setArrowContainer(interNumber, tempArrow.getTypeOfArrow());
                    scrollPane.getChildren().add(tempArrow.getArrow());
                    pressedCircleCounter = 0;
                    logicOrDown = false;
                    logicNotDown = false;
                    buttonEnabler();
                }
            }
        });

        tempCircle.getTempCircle().setOnContextMenuRequested(contextMenuEvent -> {
            setContextMenu();
            contextMenu.show(tempCircle.getTempCircle(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        });
    }

    @FXML
    private void mouseMoved(MouseEvent event) {
        double X = event.getX();
        double Y = event.getY();
        if (circleDown || fictitiousCircleDown || propagatedCircleDown) drawCircle(X, Y);
    }

    @FXML
    private void mouseClick(MouseEvent event) {
        if (circleDown) {
            circleDown = false;
            addListener(tempCircle);
            tempCircleArray.add(tempCircle);
            buttonEnabler();
        }

        if (fictitiousCircleDown) {
            fictitiousCircleDown = false;
            addListener(tempCircle);
            tempCircleArray.add(tempCircle);
            buttonEnabler();
        }

        if (propagatedCircleDown) {
            propagatedCircleDown = false;
            addListener(tempCircle);
            tempCircleArray.add(tempCircle);
            buttonEnabler();
        }
    }

    private void drawCircle(double X, double Y) {
        tempCircle.setXY(X, Y);
    }

    private void buttonDisabler(Button button) {
        highlighting.setDisable(true);
        logicOr.setDisable(true);
        logicAnd.setDisable(true);
        logicNot.setDisable(true);
        arrow.setDisable(true);
        circle.setDisable(true);
        fictitiousCircle.setDisable(true);
        propagatedCircle.setDisable(true);
        back.setDisable(true);
        forward.setDisable(true);

        button.setDisable(false);
    }

    private void buttonEnabler() {
        highlighting.setDisable(false);
        logicOr.setDisable(false);
        logicAnd.setDisable(false);
        logicNot.setDisable(false);
        arrow.setDisable(false);
        circle.setDisable(false);
        fictitiousCircle.setDisable(false);
        propagatedCircle.setDisable(false);
        back.setDisable(false);
        forward.setDisable(false);
    }
}
