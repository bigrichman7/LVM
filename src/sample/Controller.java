package sample;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import sample.animations.Shake;


public class Controller {

    @FXML
    Button openSFC;
    @FXML
    Button logicOr;

    @FXML
    Button logicAnd;

    @FXML
    Button logicNot;

    @FXML
    Button saveSFC;

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

    static String mainCondition;

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
        if (!condition.getText().isEmpty()) {
            mainCondition = condition.getText();
            Stage logicFunctionStage = new Stage();
            ScrollPane root = FXMLLoader.load(getClass().getResource("logicFunction.fxml"));
            logicFunctionStage.setTitle("Вывод логической функции");
            logicFunctionStage.setScene(new Scene(root, 1000, 600));
            logicFunctionStage.setResizable(false);
            logicFunctionStage.show();
        } else {
            Shake emptyCondition = new Shake(condition);
            emptyCondition.playAnim();
        }
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
    void saveSFC(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранение");
        File file = fileChooser.showOpenDialog(scrollPane.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file, false)) {
                StringBuilder text = new StringBuilder();
                for (int i = 0; i < tempCircleArray.size(); i++) {
                    TempCircle tempCircle = tempCircleArray.get(i);
                    if (tempCircle.isNumber) text.append(tempCircle.getNumber().getCharacters());
                    else text.append("null");
                    text.append(" ");
                    if (tempCircle.isIndex) text.append(String.valueOf(tempCircle.getIndex()));
                    else text.append("null");
                    text.append(" ");
                    text.append(tempCircle.getTypeCircle());
                    text.append(" ");
                    text.append(tempCircle.getTempCircle().getX());
                    text.append(" ");
                    text.append(tempCircle.getTempCircle().getY());
                    text.append(" ");
                    text.append(tempCircle.getArrowContainer());
                    text.append('\n');
                }
                writer.write(text.toString());
                // запись по символам
                writer.append('E');

                writer.flush();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
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
    void openSFC(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(scrollPane.getScene().getWindow());
        if (file != null) {
            scrollPane.getChildren().clear();
            tempCircleArray.clear();

            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (!line.equals("E")) {
                TextField number = new TextField();
                TextField index = new TextField();
                String typeOfCircle = "";
                double X = 0;
                double Y = 0;
                int key = 0;
                String value;
                StringBuilder element = new StringBuilder();
                int elementCounter = 0;
                for (int i = 0; i < line.length(); i++) {

                    char symbol = line.toCharArray()[i];
                    if (elementCounter < 5) {
                        //Считываем до фигурных скобок
                        if (symbol != ' ') element.append(symbol);
                        else {
                            switch (elementCounter) {
                                case 0:
                                    number = new TextField(element.toString());
                                    break;
                                case 1:
                                    index = new TextField(element.toString());
                                    break;
                                case 2:
                                    typeOfCircle = element.toString();
                                    break;
                                case 3:
                                    X = Double.parseDouble(element.toString());
                                    break;
                                case 4:
                                    Y = Double.parseDouble(element.toString());
                                    break;
                            }
                            elementCounter++;
                            element = new StringBuilder();
                        }
                    } else {
                        TempCircle tempCircle = new TempCircle(typeOfCircle);
                        tempCircle.setNumber(number);
                        tempCircle.setIndex(index);
                        tempCircle.setXY(X, Y);
                        //Считываем то, что в фигурных скобках
                        String substring = line.substring(i + 1, line.length() - 1);
                        if (substring.length() == 0) {
                            tempCircleArray.add(tempCircle);
                            break;
                        } else {
                            StringBuilder el = new StringBuilder();
                            boolean keyIs = false;
                            for (int j = 0; j < substring.length(); j++) {
                                char s = substring.charAt(j);
                                if (!keyIs) {
                                    if (s != '=') {
                                        el.append(s);
                                    } else {
                                        key = Integer.parseInt(el.toString());
                                        keyIs = true;
                                        el = new StringBuilder();
                                    }
                                } else {
                                    if (s != ',' && j != substring.length() - 1) {
                                        el.append(s);
                                    } else {
                                        if (j == substring.length() - 1) el.append(s);
                                        value = el.toString();
                                        tempCircle.setArrowContainer(key, value);
                                        keyIs = false;
                                        el = new StringBuilder();
                                        j++;
                                    }
                                }
                            } //end for
                            tempCircleArray.add(tempCircle);
                            break;
                        }
                    }

                }

                line = reader.readLine();
            }
        }

        drawSFCFromFile();
    }

    private void drawSFCFromFile() {
        for (int i = 0; i < tempCircleArray.size(); i++) {
            TempCircle tempCircle = tempCircleArray.get(i);
            scrollPane.getChildren().add(tempCircle.getTempCircle());
            addListener(tempCircle);
            if (!tempCircle.getNumber().getText().equals("null")) {
                TextField textField = tempCircle.getNumber();
                textField.setLayoutX(tempCircle.getTempCircle().getX() + 20);
                textField.setLayoutY(tempCircle.getTempCircle().getY() + 18);
                textField.setMaxWidth(30);
                textField.setStyle("-fx-border-color: white; -fx-background-color: white; -fx-font-size: 13; -fx-padding: 0;");
                scrollPane.getChildren().add(textField);
            }

            if (!tempCircle.getIndexTextField().getText().equals("null")) {
                TextField textField = tempCircle.getIndexTextField();
                textField.setLayoutX(tempCircle.getTempCircle().getX() + 35);
                textField.setLayoutY(tempCircle.getTempCircle().getY() + 30);
                textField.setMaxWidth(13);
                textField.setStyle("-fx-border-color: white; -fx-background-color: white; -fx-font-size: 8; -fx-padding: 0;");
                scrollPane.getChildren().add(textField);
            }

            //Добавление стрелок
            for (Integer key : tempCircle.getArrowContainer().keySet()) {
                TempArrow ta = new TempArrow(tempCircle.getArrowContainer().get(key));
                TempCircle startTempCircle = null;
                for (TempCircle tc :
                        tempCircleArray) {
                    if (Integer.parseInt(tc.getNumber().getCharacters().toString()) == key) {
                        startTempCircle = tc;
                    }
                }
                ta.startArrow(startTempCircle.getTempCircle().getX(), startTempCircle.getTempCircle().getY());
                ta.endArrow(tempCircle.getTempCircle().getX(), tempCircle.getTempCircle().getY());
                scrollPane.getChildren().add(ta.getArrow());
            }
        }
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
        openSFC.setDisable(true);
        logicOr.setDisable(true);
        logicAnd.setDisable(true);
        logicNot.setDisable(true);
        saveSFC.setDisable(true);
        circle.setDisable(true);
        fictitiousCircle.setDisable(true);
        propagatedCircle.setDisable(true);
        back.setDisable(true);
        forward.setDisable(true);

        button.setDisable(false);
    }

    private void buttonEnabler() {
        openSFC.setDisable(false);
        logicOr.setDisable(false);
        logicAnd.setDisable(false);
        logicNot.setDisable(false);
        saveSFC.setDisable(false);
        circle.setDisable(false);
        fictitiousCircle.setDisable(false);
        propagatedCircle.setDisable(false);
        back.setDisable(false);
        forward.setDisable(false);
    }
}
