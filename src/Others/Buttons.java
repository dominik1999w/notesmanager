package Others;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Buttons {
    public ImageView setFullSizeButton(){
        Image image=new Image(getClass().getResourceAsStream("../Images/icon.png"));
        ImageView fullSizeIcon= new ImageView(image);
        fullSizeIcon.setPreserveRatio(true);
        fullSizeIcon.setFitHeight(18);
        fullSizeIcon.setFitWidth(50);
        return fullSizeIcon;
    }
    public ImageView setSaveButton(){
        Image image=new Image(getClass().getResourceAsStream("../Images/save.png"));
        ImageView saveButton= new ImageView(image);
        saveButton.setPreserveRatio(true);
        saveButton.setFitHeight(18);
        saveButton.setFitWidth(50);
        return saveButton;
    }
    public ImageView setEditButton(){
        Image image=new Image(getClass().getResourceAsStream("../Images/edit.png"));
        ImageView editButton= new ImageView(image);
        editButton.setPreserveRatio(true);
        editButton.setFitHeight(18);
        editButton.setFitWidth(50);
        return editButton;
    }
    public ImageView setRenameButton(){
        Image image=new Image(getClass().getResourceAsStream("../Images/rename.png"));
        ImageView rename= new ImageView(image);
        rename.setPreserveRatio(true);
        rename.setFitHeight(18);
        rename.setFitWidth(50);
        return rename;
    }
}
