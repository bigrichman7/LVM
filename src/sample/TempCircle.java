package sample;

import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;


public class TempCircle {

    private ImageView tempCircle;
    private String typeCircle;

    private TextField number;
    private TextField index;

    private Map<Integer, String> arrowContainer = new HashMap<Integer, String>();

    boolean isNumber = false;
    boolean isIndex = false;

    public TempCircle(String typeCircle) {
        switch (typeCircle){
            case "circle": tempCircle = new ImageView("sample/img/elements/circl.png"); this.typeCircle = typeCircle; break;
            case "fictitiousCircle": tempCircle = new ImageView("sample/img/elements/fictitiousCircl.png"); this.typeCircle = typeCircle; break;
            case "propagatedCircle": tempCircle = new ImageView("sample/img/elements/propagatedCircl.png"); this.typeCircle = typeCircle;
        }
        tempCircle.setX(-60);
        tempCircle.setY(0);
    }

    public void setXY(double X, double Y){
        tempCircle.setX(X - 25);
        tempCircle.setY(Y - 25);

        if (isNumber){
            number.setLayoutX(X - 5);
            number.setLayoutY(Y - 6);
        }

        if (isIndex){
            index.setLayoutX(X + 10);
            index.setLayoutY(Y + 6);
        }
    }

    public ImageView getTempCircle() {
        return tempCircle;
    }

    public void setNumber(TextField number) {
        this.number = number;
        isNumber = true;
    }

    public void setIndex(TextField index) {
        this.index = index;
        isIndex = true;
    }

    public TextField getNumber() {
        return number;
    }

    public int getIndex() {
        return Integer.parseInt(index.getCharacters().toString());
    }

    public TextField getIndexTextField() {
        return index;
    }

    public String getTypeCircle() {
        return typeCircle;
    }

    public void setArrowContainer(int key, String value){
        arrowContainer.put(key, value);
    }

    public Map<Integer, String> getArrowContainer() {
        return arrowContainer;
    }
}
