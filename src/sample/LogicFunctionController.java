package sample;

import java.net.URL;
import java.util.*;

import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class LogicFunctionController {

    private TempCircle[] circlesArray;
    private String[] conditionsArray;
    private String[] conditionsNotArray;

    private StringBuffer mainCondition;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    AnchorPane scrollPane;

    @FXML
    Text conditionsText;

    @FXML
    Text conditionsNotText;

    @FXML
    Text logicFormula;


    @FXML
    void initialize() {
        fillCirclesArray();
        fillConditionsArray();
        fillConditionsNotArray();

        conditionsText.setText(getConditions());
        conditionsNotText.setText(getConditionsNot());

        deriveFormula();
        //logicFormula.setText(logicFormula.getText() + " = " + bracketOpener(mainCondition) + ";");

    }

    private void fixError() {
        conditionsArray[5] = "5 * y2 + 5 * y6";
        conditionsNotArray[5] = "( !5 + !y2 ) * ( !5 + !y6 )";
    }

    private String bracketOpener(StringBuffer expression) {
        int[] startEndBracketIndex;
        int[] startEndSubstringIndex;
        String substring;

        while (expression.toString().contains("(")) {

            //Находим индексы скобок самого нижнего уровня основной строки
            startEndBracketIndex = getIndexBrackets(expression.toString());
            if (startEndBracketIndex[0] == 0 && startEndBracketIndex[1] == expression.length() - 1) break;
            //Находим индексы целой строки (с умножениями), в которой содержатся скобки
            startEndSubstringIndex = getIndexSubstring(expression.toString(), startEndBracketIndex[0], startEndBracketIndex[1]);

            //Выносим подстроку в основном выражении, которая является целой
            if (startEndSubstringIndex[1] == expression.length() - 1)
                substring = expression.substring(startEndSubstringIndex[0], startEndSubstringIndex[1] + 1);
            else
                substring = expression.substring(startEndSubstringIndex[0], startEndSubstringIndex[1] - 1);

            //Раскрываем скобки в целой строке и заменяем полученный результат в основной строке
            expression.replace(startEndSubstringIndex[0], startEndSubstringIndex[1] + 1, substringOpener(substring));

        }

        return expression.toString();
    }

    private String substringOpener(String substring) {
        StringBuffer result = new StringBuffer();
        String simpleMembers = getSimpleMembers(substring);
        ArrayList<String> compositeMembers = getCompositeMembers(substring);

        //После выполнения данной инструкции, получим список compositeMembers, состоящий из выражений
        //substring, находящихся в скобках и слитых с простыми членами
        compositeMembers.set(0, mergeSimpleMembersWithCompositeMembers(simpleMembers, compositeMembers.get(0)));

        ArrayList<String> mainCompositeMember = new ArrayList<>();
        if (compositeMembers.size() > 1) {
            mainCompositeMember = getMembers(compositeMembers.get(0));
        } else
            mainCompositeMember.add(compositeMembers.get(0));

        ArrayList<String> tempCompositeMember;
        ArrayList<String> tempMain = new ArrayList<>();
        for (int i = 1; i < compositeMembers.size(); i++) {
            tempCompositeMember = getMembers(compositeMembers.get(i));
            for (int j = 0; j < mainCompositeMember.size(); j++) {
                String mainElement = mainCompositeMember.get(j);
                for (int l = 0; l < tempCompositeMember.size(); l++) {
                    String tempElement = tempCompositeMember.get(l);
                    if (l != tempCompositeMember.size() - 1) {
                        tempMain.add(mainElement + " * " + tempElement);
                    } else
                        tempMain.add(mainElement + " * " + tempElement);
                }
            }
            mainCompositeMember = tempMain;
        }

        for (int i = 0; i < mainCompositeMember.size(); i++) {
            if (i != mainCompositeMember.size() - 1)
                result.append(mainCompositeMember.get(i)).append(" + ");
            else
                result.append(mainCompositeMember.get(i));
        }

        return result.toString();
    }

    //В каждую ячейку массива присваевается одна коньюктивная группа из выражения со слагаемыми
    private ArrayList<String> getMembers(String compositeMember) {
        ArrayList<String> result = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < compositeMember.length(); i++) {
            char s = compositeMember.charAt(i);
            if (s != '+') stringBuffer.append(s);
            else {
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                result.add(stringBuffer.toString());
                stringBuffer = new StringBuffer();
                i++;
            }
        }
        result.add(stringBuffer.toString());

        return result;
    }

    private String mergeSimpleMembersWithCompositeMembers(String simpleMembers, String compositeMember) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < compositeMember.length(); i++) {
            char s = compositeMember.charAt(i);
            if (s != '+') {
                result.append(s);
            } else {
                result.append("* ").append(simpleMembers).append(" +");
            }
            if (i == compositeMember.length() - 1) {
                result.append(" * ").append(simpleMembers);
            }
        }

        return result.toString();
    }

    private ArrayList<String> getCompositeMembers(String substring) {
        ArrayList<String> result = new ArrayList<>();
        boolean brackets = false;
        String compositeMember = "";

        for (int i = 0; i < substring.length(); i++) {

            char s = substring.charAt(i);
            if (s == '(' && !brackets) {
                brackets = true;
                i += 1;
                continue;
            }
            if (s == ')') brackets = false;

            if (brackets) compositeMember = compositeMember + s;

            if (s == ')') {
                compositeMember = compositeMember.substring(0, compositeMember.length() - 1);
                result.add(compositeMember);
                compositeMember = "";
            }

        }

        return result;
    }

    private String getSimpleMembers(String substring) {
        StringBuffer result = new StringBuffer();
        boolean brackets = false;

        for (int i = 0; i < substring.length(); i++) {
            char s = substring.charAt(i);
            if (s == '(') brackets = true;
            if (s == ')') {
                brackets = false;
                if (i + 3 < substring.length())
                    i += 3;
            }

            if (!brackets && s != ')') {
                result.append(s);
            }
        } //end for

        if (result.charAt(result.length() - 2) == '*') result.replace(result.length() - 3, result.length(), "");

        return result.toString();
    }

    private int[] getIndexSubstring(String expression, int startBracket, int endBracket) {
        int[] result = new int[2];
        char s;
        int i = 0;
        int j = 0;
        boolean skipBracket = false;

        if (startBracket != 0) {
            do {
                i++;
                if (i <= startBracket) s = expression.charAt(startBracket - i);
                else break;
                if (s == '(' && skipBracket) skipBracket = false;
                if (s == ')') skipBracket = true;
            } while (s != '+' || skipBracket);
            if (i != startBracket) result[0] = startBracket - i + 2;
        }

        if (endBracket != expression.length() - 1) {
            do {
                j++;
                if (j <= expression.length() - endBracket) s = expression.charAt(endBracket + j);
                else break;
                if (s == ')' && skipBracket) skipBracket = false;
                if (s == '(') skipBracket = true;
            } while (s != '+' || skipBracket);
            if (j != expression.length() - endBracket) result[1] = endBracket + j - 2;
            else result[1] = expression.length() - 1;
        } else result[1] = endBracket;

        return result;
    }

    private int[] getIndexBrackets(String expression) {
        int[] result = new int[2];

        for (int i = 0; i < expression.length(); i++) {
            char s = expression.charAt(i);
            if (s == '(') result[0] = i;
            if (s == ')') {
                result[1] = i;
                return result;
            }
        }

        result[0] = 0;
        result[1] = expression.length() - 1;

        return result;
    }

    private int getBracketCount(String expression) {
        int n = 0;
        for (int i = 0; i < expression.length(); i++) {
            char s = expression.charAt(i);
            if (s == '(' || s == ')') n++;
        }
        return n;
    }

    private void deriveFormula() {
        mainCondition = new StringBuffer(Controller.mainCondition);
        substituteValues();
        logicFormula.setText("Yc = " + Controller.mainCondition + " = " + mainCondition.toString());

        while (mainCondition.toString().contains("y")) {
            substituteValues();
            logicFormula.setText(logicFormula.getText() + " = " + mainCondition.toString());
        }

        logicFormula.setText(logicFormula.getText());
    }

    private void substituteValues() {
        char symbol;
        char not;
        boolean sign;
        StringBuilder stringNumber;
        int number;
        boolean beforeMultiply;
        boolean afterMultiply;
        boolean containsMultiplication;
        String replace;
        StringBuffer tempMainCondition = new StringBuffer();
        for (int i = 0; i < mainCondition.length(); i++) {
            symbol = mainCondition.charAt(i);
            sign = false;
            stringNumber = new StringBuilder();
            beforeMultiply = false;
            afterMultiply = false;
            if (symbol == 'y') {

                //Определяем, является ли y отрицательным
                //И имеется ли знак умножения до y
                if (i != 0) {
                    not = mainCondition.charAt(i - 1);
                    if (not == '!') {
                        sign = true;
                        if (mainCondition.charAt(i - 3) == '*') beforeMultiply = true;
                    } else {
                        if (mainCondition.charAt(i - 2) == '*') beforeMultiply = true;
                    }
                }


                //Определяем номер y
                //И имеется ли знак умножения после y
                do {
                    i++;
                    if (i == mainCondition.length()) break;
                    symbol = mainCondition.charAt(i);
                    if (symbol != ' ') stringNumber.append(symbol);
                } while (symbol != ' ');
                number = Integer.parseInt(stringNumber.toString());
                if (i != mainCondition.length()) {
                    if (mainCondition.charAt(i + 1) == '*') afterMultiply = true;
                }

                //Определяем имеется ли '+' в подставляемом выражении
                //И подставляем
                if (sign) replace = conditionsNotArray[number];
                else replace = conditionsArray[number];

                if ((beforeMultiply || afterMultiply) && replace.contains("+"))
                    tempMainCondition.append("( ").append(replace).append(" )");
                else tempMainCondition.append(replace);

                if (i != mainCondition.length()) {
                    tempMainCondition.append(" ");
                }

            } else if (symbol == '!' && mainCondition.charAt(i + 1) == 'y') {

            } else tempMainCondition.append(symbol);
        } //end for
        mainCondition = tempMainCondition;
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
                        element.delete(j, j + 1);
                        j = searchingLoop(element, j);
                        break;
                    case '*':
                        element.replace(j, j + 1, "+");
                        j++;
                        break;
                    case '+':
                        element.replace(j, j + 1, "*");
                        j++;
                        break;
                    case ' ':
                        break;
                    default:
                        element.insert(j, '!');
                        j = searchingLoop(element, j);
                }
            }

            //Расстановка скобок
            String string = "";
            StringBuilder sb = new StringBuilder();
            ArrayList<String> members = new ArrayList<>();
            for (int j = 0; j < element.length(); j++) {

                char s = element.charAt(j);
                if (s != '*') {
                    sb.append(s);
                } else {
                    string = sb.toString();
                    string = string.trim();
                    if (string.length() > 4) {
                        string = "( " + string + " )";
                    }
                    members.add(string);
                    sb = new StringBuilder();
                }

                if (j == element.length() - 1)
                    if (members.size() == 0)
                        members.add(sb.toString());
                    else {
                        String s1 = sb.toString();
                        s1 = s1.trim();
                        if (s1.length() > 4)
                            members.add("( " + s1 + " )");
                        else
                            members.add(s1);
                    }
            }

            element = new StringBuffer();
            for (String str :
                    members) {
                element.append(str).append(" * ");
            }

            if (element.charAt(element.length() - 2) == '*') {
                String s = element.substring(0, element.length() - 3);
                element.replace(0, element.length(), s);
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

        return j - 1;
    }


    private void fillCirclesArray() {
        int size = Controller.tempCircleArray.size();
        circlesArray = new TempCircle[size + 1];
        for (int i = 0; i < size; i++) {
            circlesArray[Integer.parseInt(Controller.tempCircleArray.get(i).getNumber().getCharacters().toString())] = Controller.tempCircleArray.get(i);
        }
    }

    //Если ты разберешься в этом алгоритме, ты - ГЕНИЙ
    private void fillConditionsArray() {
        conditionsArray = new String[Controller.tempCircleArray.size() + 1];

        for (int i = 1; i <= Controller.tempCircleArray.size(); i++) {
            StringBuilder condition;
            boolean numberIs = false;
            TempCircle tc = circlesArray[i];

            if (tc.getTypeCircle().equals("circle")) {
                numberIs = true;
                condition = new StringBuilder(tc.getNumber().getCharacters().toString());
            } else {
                condition = new StringBuilder();
            }

            int counter = 0;
            for (Map.Entry<Integer, String> item : tc.getArrowContainer().entrySet()) {
                if (numberIs) {

                    if (counter == 0)
                        if (item.getValue().equals("logicOrWithNot") || item.getValue().equals("logicAndWithNot"))
                            condition.append(" * !y").append(item.getKey());
                        else
                            condition.append(" * y").append(item.getKey());

                    else if (condition.charAt(condition.length() - 2) == '+')
                        if (item.getValue().equals("logicOrWithNot") || item.getValue().equals("logicAndWithNot"))
                            condition.append(counter).append(" * !y").append(item.getKey());
                        else
                            condition.append(counter).append(" * y").append(item.getKey());
                    else if (item.getValue().equals("logicOrWithNot") || item.getValue().equals("logicAndWithNot"))
                        condition.append("!y").append(item.getKey());
                    else
                        condition.append("y").append(item.getKey());
                    counter = Integer.parseInt(tc.getNumber().getCharacters().toString());

                } else {

                    if (item.getValue().equals("logicOrWithNot") || item.getValue().equals("logicAndWithNot"))
                        condition.append("!y").append(item.getKey());
                    else
                        condition.append("y").append(item.getKey());
                    //condition.append(" * ").append("y").append(item.getKey());

                }

                if (item.getValue().equals("logicOr") || item.getValue().equals("logicOrWithNot"))
                    condition.append(" + ");
                else
                    condition.append(" * ");

            }

            String sub;
            if (condition.charAt(condition.length() - 1) == ' ') {
                sub = condition.substring(0, condition.length() - 3);
                condition.replace(0, condition.length(), sub);
            }

            if (condition.charAt(condition.length() - 1) == '!') {
                sub = condition.substring(0, condition.length() - 4);
                condition.replace(0, condition.length(), sub);
            }

            conditionsArray[i] = condition.toString();

        }
    }
}