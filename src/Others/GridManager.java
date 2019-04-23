package Others;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

import java.io.File;
import java.util.LinkedList;

import static java.lang.Math.round;

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
        File[] filesInfo = dir.listFiles();

        //adjusting size
        int numberOfItems = files.length;
        int height = numberOfItems % width == 0 ? (numberOfItems / width) : (numberOfItems / width) + 1;

        gridFiles = generateNewGridPane(width,height);
        scrollPane.setContent(gridFiles);
        gridFiles.setVisible(true);
        scrollPane.setVisible(true);
        gridFiles.getStylesheets().addAll(getClass().getResource("../Others/sample.css").toExternalForm());

        int c = 0;
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (c < files.length) {
                    String fileName = RegexManager.convertNameToReadable(files[c]);
                    File file = filesInfo[c];

                    Pane pane = new Pane(); //main cell body
                    pane.setId(files[c]);
                    pane.setStyle("-fx-background-color: #383844;" +
                            "-fx-border-color:black;" +
                            "-fx-text-fill:white;");
                    pane.resize(175,150);
                    pane.setEffect(new DropShadow());

                    //add file name to cell
                    Label name = new Label(fileName);
                    name.setTranslateX(5);
                    name.setTranslateY(110);
                    name.setMaxWidth(pane.getWidth() - 30);
                    name.setStyle("-fx-background-color: #383844;" +
                            "-fx-border-color:#383844;" +
                            "-fx-font-weight:500;" +
                            "-fx-font-size:14");
                    pane.getChildren().add(name);

                    //add extension field to cell
                    String ext = RegexManager.getExtension(files[c]).toUpperCase();
                    if(ext.length() > 0){
                        Label extension = new Label(ext);
                        extension.setTranslateX(5);
                        extension.setTranslateY(130);
                        extension.setStyle("-fx-background-color: #505062;" +
                                "-fx-border-color:#505062;" +
                                "-fx-font-weight:500;" +
                                "-fx-font-size:14");
                        pane.getChildren().add(extension);
                    }

                    //add size info
                    double fileSize = file.length();
                    String sizeString;
                    if(fileSize < 1024){
                        sizeString = fileSize + " B";
                    } else {
                        sizeString = round(fileSize / 1024 * 100d) / 100d + " KB";
                    }
                    Label size = new Label(sizeString);
                    size.setTranslateY(130);
                    size.setTranslateX(pane.getWidth() - 5 * sizeString.length() - 40);
                    pane.getChildren().add(size);
                    gridFiles.add(pane, j, i);
                    c++;
                }
            }
        }
    }
    private Label[] generate(int width){
        Label[] labels = new Label[width];
        for(int i = 0; i < width; i++){
            labels[i] = new Label();
        }
        return labels;
    }

    private GridPane generateNewGridPane(int width, int height){

        double prefHeight = height <= 5 ? gridFilesFactory.getPrefHeight() : height * 175; //globalne
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
        height = height <= 5 ? 5 : height; //globalne
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
