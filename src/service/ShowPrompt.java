package service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ShowPrompt {
    public static void  show(String info){
        Alert alert=new Alert(
                Alert.AlertType.INFORMATION);
        alert.setTitle("信息提示");
        alert.setHeaderText(info);
        alert.showAndWait();
        System.out.println(info);
    }
}
