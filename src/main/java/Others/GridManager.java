package Others;

import Controllers.ControllerPrimary;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

import java.io.File;
import java.util.*;

import static java.lang.Math.round;

public class GridManager {

    public GridManager() {}

    public GridManager(GridPane gridFilesFactory, GridPane gridPane, ScrollPane scrollPane, ControllerPrimary controller) {
        this.gridFilesFactory = gridFilesFactory;
        this.gridPane = gridPane;
        this.scrollPane = scrollPane;
        this.controller = controller;
    }

    @FXML
    GridPane gridFilesFactory;
    @FXML
    GridPane gridPane;
    @FXML
    ScrollPane scrollPane;
    private ControllerPrimary controller;

    public void setGridFilesFactory(GridPane gridFilesFactory) {
        this.gridFilesFactory = gridFilesFactory;
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public void adjustGridFilesView(File dir, int width){

        ArrayList<File> filesInfo = new ArrayList<>();
        if(dir != null) {
            Collections.addAll(filesInfo, Objects.requireNonNull(dir.listFiles()));
            filesInfo.sort(Comparator.comparing(File::getName));
        }
        //adjusting size
        int numberOfItems = filesInfo.size();
        int height = numberOfItems % width == 0 ? (numberOfItems / width) : (numberOfItems / width) + 1;

        gridPane = generateNewGridPane(width,height);
        scrollPane.setContent(gridPane);
        gridPane.setVisible(true);
        scrollPane.setVisible(true);
        gridPane.getStylesheets().addAll(getClass().getResource("../Css/sample.css").toExternalForm());

        int c = 0;
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (c < filesInfo.size()) {
                    String fileName = RegexManager.convertNameToReadable(filesInfo.get(c).getName());
                    File file = filesInfo.get(c);

                    Pane pane = new Pane(); //main cell body
                    pane.setId(filesInfo.get(c).getName());
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
                    String ext = RegexManager.getExtension(filesInfo.get(c).getName()).toUpperCase();
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

                    //add state to file
                    Label state = new Label("");
                    int s = controller.getStatesMap().get(RegexManager.convertFullPathToShort(file.getPath()));
                    Image image = new Image(getClass().getResourceAsStream("../States/" + s + ".png"));
                    state.setTranslateX(180);
                    state.setTranslateY(15);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(30);
                    state.setGraphic(imageView);
                    pane.getChildren().add(state);

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
                    size.setTranslateX(pane.getWidth() - 5 * sizeString.length() + 20);
                    pane.getChildren().add(size);
                    gridPane.add(pane, j, i);
                    c++;
                }
            }
        }
    }

    private GridPane generateNewGridPane(int width, int height){

        double prefHeight = height * 185; //globalne
        double prefWidth = gridFilesFactory.getPrefWidth() * ((double)width / 4); //globalne

        System.out.println("CHANGING grid to : prefW - " + prefWidth + " prefH - " + prefHeight);

        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(gridFilesFactory.getLayoutX());
        gridPane.setLayoutY(gridFilesFactory.getLayoutY());
        gridPane.setOnMouseClicked(gridFilesFactory.getOnMouseClicked());
        gridPane.setPrefHeight(prefHeight);
        gridPane.setPrefWidth(prefWidth);

        LinkedList<ColumnConstraints> columnConstraints = new LinkedList<>();
        for(int i = 0; i < width; i++)
            columnConstraints.add(gridFilesFactory.getColumnConstraints().get(0));
        gridPane.getColumnConstraints().addAll(columnConstraints);

        LinkedList<RowConstraints> rowConstraints = new LinkedList<>();
        //height = height <= 5 ? 5 : height; //globaln
        for(int i = 0; i < height; i++)
            rowConstraints.add(gridFilesFactory.getRowConstraints().get(0));
        gridPane.getRowConstraints().addAll(rowConstraints);

        gridPane.setVisible(true);
        gridPane.setHgap(25); //globalne
        gridPane.setVgap(25); //globalne
        return gridPane;
    }

}
