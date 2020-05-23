package sample;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class LogicFunctionController {

    private TempCircle[] circlesArray;
    private String[] conditionsArray;
    private String[] conditionsNotArray;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    ScrollPane scrollPane;

    @FXML
    Text conditionsText;

    @FXML
    Text conditionsNotText;


    @FXML
    void initialize() {
        fillCirclesArray();
        fillConditionsArray();
        fillConditionsNotArray();
        conditionsText.setText(getConditions());
        conditionsNotText.setText(getConditionsNot());
    }

    private String getConditions() {
        StringBuilder str = new StringBuilder();
        for (int i = 1; i < conditionsArray.length; i++) {
            str.append("y").append(i).append(" = ").append(conditionsArray[i]).append("\n");
        }

        return str.toString();
    }

    private String getConditionsNot() {
        StringBuilder str = new StringBuilder();
        for (int i = 1; i < conditionsNotArray.length; i++) {
            str.append("!y").append(i).append(" = ").append(conditionsNotArray[i]).append("\n");
        }

        return str.toString();
    }

    private void fillConditionsNotArray() {
        conditionsNotArray = new String[conditionsArray.length];

        StringBuffer element;
        char symbol;
        for (int i = 1; i < conditionsArray.length; i++) {
            element = new StringBuffer(conditionsArray[i]);

            for (int j = 0; j < element.length(); j++) {
                symbol = element.charAt(j);
                switch (symbol) {
                    case '!':
                        element.delete(j, j+1);
                        j = searchingLoop(element, j);
                        break;
                    case '*':
                        element.replace(j, j+1, "+");
                        j++;
                        break;
                    case '+':
                        element.replace(j, j+1, "*");
                        j++;
                        break;
                    case ' ':
                        break;
                    default:
                        element.insert(j, '!');
                        j = searchingLoop(element, j);
                }
            }
            conditionsNotArray[i] = element.toString();
        }
    }

    private int searchingLoop(StringBuffer element, int j) {
        char symbol;
        do {
            j++;
            if (j == element.length() - 1) return j;
            symbol = element.charAt(j);
        } while (symbol != '+' && symbol != '*');

        return j-1;
    }


    private void fillCirclesArray() {
        int size = Controller.tempCircleArray.size();
        circlesArray = new TempCircle[size + 1];
        for (int i = 0; i < size; i++) {
            circlesArray[Integer.parseInt(Controller.tempCircleArray.get(i).getNumber().getCharacters().toString())] = Controller.tempCircleArray.get(i);
        }
    }

    private void fillConditionsArray() {
        conditionsArray = new String[Controller.tempCircleArray.size() + 1];

        for (int i = 1; i <= Controller.tempCircleArray.size(); i++) {
            StringBuilder condition;
            int stupidInt = 0;
            TempCircle tc = circlesArray[i];

            if (tc.getTypeCircle().equals("circle")) {
                condition = new StringBuilder(tc.getNumber().getCharacters().toString());
                stupidInt++;
            } else {
                condition = new StringBuilder();
            }

            for (Map.Entry<Integer, String> item : tc.getArrowContainer().entrySet()) {
                switch (item.getValue()) {
                    case "logicOr":
                        if (!condition.toString().isEmpty() && stupidInt == 1) {
                            condition.append(" * y").append(item.getKey());
                            stupidInt++;
                        } else condition.append(" + y").append(item.getKey());
                        break;
                    case "logicAnd":
                        condition.append(" * y").append(item.getKey());
                        break;
                    case "logicOrWithNot":
                        if (!condition.toString().isEmpty() && stupidInt == 1) {
                            condition.append(" * !y").append(item.getKey());
                            stupidInt++;
                        } else condition.append(" + !y").append(item.getKey());
                        break;
                    case "logicAndWithNot":
                        condition.append(" * !y").append(item.getKey());
                }
            }

            try {
                char ch = condition.toString().charAt(1);
                char plus = '+';
                char multiply = '*';
                if (ch == plus || ch == multiply) {
                    conditionsArray[i] = condition.toString().substring(3);
                } else {
                    conditionsArray[i] = condition.toString();
                }
            } catch (Exception e) {
                conditionsArray[i] = condition.toString();
            }
        }
    }
}