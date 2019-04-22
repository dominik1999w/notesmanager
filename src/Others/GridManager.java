package Others;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.File;
import java.util.LinkedList;

public class GridManager {

    public GridManager() {}

    public GridManager(File selectedDir, GridPane gridFilesFactory, GridPane gridFiles, ScrollPane scrollPane) {
        this.selectedDir = selectedDir;
        this.gridFilesFactory = gridFilesFactory;
        this.gridFiles = gridFiles;
        this.scrollPane = scrollPane;
    }

    File selectedDir;
    @FXML
    GridPane gridFilesFactory;
    @FXML
    GridPane gridFiles;
    @FXML
    ScrollPane scrollPane;

    public void adjustGridFilesView(File dir, int width){

        String[] files = dir.list(); //list files in dir

        //adjusting size
        int numberOfItems = files.length;
        int height = (numberOfItems / width) + 1;

        gridFiles = generateNewGridPane(width,height);
        scrollPane.setContent(gridFiles);
        gridFiles.setVisible(true);
        scrollPane.setVisible(true);
        gridFiles.getStylesheets().addAll(getClass().getResource("../Others/sample.css").toExternalForm());

        Label[][] label = new Label[height][width];
        int c = 0;
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (c < files.length){
                    label[i][j] = new Label(files[c]);
                    Label l = label[i][j];
                    l.setId(files[c]);
                    //l.getStylesheets().addAll(getClass().getResource("../Others/sample.css").toExternalForm());
                    l.setStyle("-fx-background-color: #000033;" +
                            "-fx-border-color:black;" +
                            "-fx-text-fill:white;" +
                            "-fx-padding:50px;" +
                            "-fx-vertical-align:middle;" +
                            "-fx-text-vertical-align:middle;" +
                            "-fx-text-horizontal-align:middle;" +
                            "-fx-text-alignment:center;");
                    l.setWrapText(true);
                    l.setPrefSize(175, 150); //globalne
                    l.setEffect(new DropShadow());
                    gridFiles.add(l, j, i);
                    c++;
                }
            }
        }
        //@@make gridFiles dynamically
    }

    private Label[] generate(int width){
        Label[] labels = new Label[width];
        for(int i = 0; i < width; i++){
            labels[i] = new Label();
        }
        return labels;
    }

    private GridPane generateNewGridPane(int width, int height){

        double prefHeight = height <= 4 ? gridFilesFactory.getPrefHeight() : height * 175; //globalne
        double prefWidth = gridFilesFactory.getPrefWidth() * ((double)width / 4); //globalne

        System.out.println("CHANGING grid to : prefW - " + prefWidth + " prefH - " + prefHeight);

        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(gridFilesFactory.getLayoutX());
        gridPane.setLayoutY(gridFilesFactory.getLayoutY());
        gridPane.setOnMouseClicked(gridFilesFactory.getOnMouseClicked());
        gridPane.setPrefHeight(prefHeight);
        gridPane.setPrefWidth(prefWidth);
        gridPane.setGridLinesVisible(true);

        LinkedList<ColumnConstraints> columnConstraints = new LinkedList<>();
        for(int i = 0; i < width; i++)
            columnConstraints.add(gridFilesFactory.getColumnConstraints().get(0));
        gridPane.getColumnConstraints().addAll(columnConstraints);

        LinkedList<RowConstraints> rowConstraints = new LinkedList<>();
        height = height <= 4 ? 4 : height; //globalne
        for(int i = 0; i < height; i++)
            rowConstraints.add(gridFilesFactory.getRowConstraints().get(0));
        gridPane.getRowConstraints().addAll(rowConstraints);

        gridPane.setId("gridFiles");
        gridPane.setVisible(true);
        gridPane.setHgap(25); //globalne
        gridPane.setVgap(25); //globalne
        return gridPane;
    }

}
