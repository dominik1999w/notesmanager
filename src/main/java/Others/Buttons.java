package Others;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Buttons {

    public ImageView setCustomImage(String name){
        Image image = new Image(getClass().getResourceAsStream("/Images/" + name + ".png"));
        ImageView icon = new ImageView(image);
        icon.setPreserveRatio(true);
        icon.setFitHeight(18);
        icon.setFitWidth(50);
        return icon;
    }
}
