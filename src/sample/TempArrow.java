package sample;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class TempArrow {

    private Group arrow = new Group();

    double startX;
    double startY;

    double endX;
    double endY;

    String typeOfArrow;

    public TempArrow(String typeOfArrow) {
        this.typeOfArrow = typeOfArrow;
    }

    public void startArrow(double X, double Y) {
        startX = X + 30;
        startY = Y + 30;

    }

    public void endArrow(double X, double Y) {
        endX = X + 30;
        endY = Y + 30;


        startX = changeStartOnBorder(startX, startY, endX, endY, 30)[0];
        startY = changeStartOnBorder(startX, startY, endX, endY, 30)[1];

        endX = changeStartOnBorder(endX, endY, startX, startY, 30)[0];
        endY = changeStartOnBorder(endX, endY, startX, startY, 30)[1];

        arrow.getChildren().add(newLine());

        switch (typeOfArrow) {
            case "logicOr":
                arrow.getChildren().add(addRisochka(1));
                arrow.getChildren().add(addRisochka(2));
                break;
            case "logicAnd":
                arrow.getChildren().add(addLogicTypeOnArrow(0));
                break;
            case "logicOrWithNot":
                arrow.getChildren().add(addRisochka(1));
                arrow.getChildren().add(addRisochka(2));
                arrow.getChildren().add(addLogicTypeOnArrow(0));
                break;
            case "logicAndWithNot":
                arrow.getChildren().add(addLogicTypeOnArrow(0));
                arrow.getChildren().add(addLogicTypeOnArrow(1));
                break;
        }

    }

    private double[] changeStartOnBorder(double Xa, double Ya, double Xb, double Yb, int len) {
        double rab = java.lang.Math.sqrt(java.lang.Math.pow((Xb - Xa), 2) + java.lang.Math.pow((Yb - Ya), 2));
        double k = len / rab;
        Xa = Xa + (Xb - Xa) * k;
        Ya = Ya + (Yb - Ya) * k;
        double[] XY = new double[2];
        XY[0] = Xa;
        XY[1] = Ya;

        return XY;
    }

    private Line addRisochka(int i) {
        double Ax = changeStartOnBorder(endX, endY, startX, startY, 15)[0];
        double Ay = changeStartOnBorder(endX, endY, startX, startY, 15)[1];

        //Находим вектор AB
        double ABx = endX - Ax;
        double ABy = endY - Ay;

        //Нормализуем вектор AB
        ABx = ABx / 5;
        ABy = ABy / 5;

        //Получение перпендикулярного вектора
        double tempABx = ABx;
        if (i == 1) {
            ABx = ABy * (-1);
            ABy = tempABx;
        } else {
            ABx = ABy;
            ABy = tempABx * (-1);
        }

        //Умножение перпендикулярного вектора на его длину
        ABx = ABx * 2;
        ABy = ABy * 2;

        //Получение координат точки C
        double Cx = Ax + ABx;
        double Cy = Ay + ABy;

        Line line = new Line(endX, endY, Cx, Cy);
        line.setStroke(Color.BLACK);

        return line;
    }

    private ImageView addLogicTypeOnArrow(int l) {
        ImageView arrowCircle = new ImageView();
        Image image;

        if (typeOfArrow.equals("logicAnd")) {
            image = new Image("sample/img/arrowIcons/logicOrArrowIcon.png");
            arrowCircle.setImage(image);
            arrowCircle.setX(endX - 5);
            arrowCircle.setY(endY - 5);
        }

        if (typeOfArrow.equals("logicAndWithNot")) {
            if (l == 0) {
                image = new Image("sample/img/arrowIcons/logicNotArrowIcon.png");
                arrowCircle.setImage(image);
                arrowCircle.setX(startX - 5);
                arrowCircle.setY(startY - 5);
            } else {
                image = new Image("sample/img/arrowIcons/logicOrArrowIcon.png");
                arrowCircle.setImage(image);
                arrowCircle.setX(endX - 5);
                arrowCircle.setY(endY - 5);
            }
        }

        if (typeOfArrow.equals("logicOrWithNot")) {
            image = new Image("sample/img/arrowIcons/logicNotArrowIcon.png");
            arrowCircle.setImage(image);
            arrowCircle.setX(startX - 5);
            arrowCircle.setY(startY - 5);
        }

        return arrowCircle;
    }

    private Line newLine() {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.BLACK);

        return line;
    }

    public Group getArrow() {
        return arrow;
    }

    public String getTypeOfArrow() {
        return typeOfArrow;
    }
}
