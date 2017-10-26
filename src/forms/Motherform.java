package forms;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Motherform extends Application {
    @Override
    public void start(Stage primaryStage) {
        MenuItem menuItemAll=new MenuItem();
        menuItemAll.setText("提取所有图层");
        MenuItem menuItemSelect=new MenuItem();
        menuItemSelect.setText("提取选择的图层");
        MenuItem menuItemSeparate=new MenuItem();
        menuItemSeparate.setText("分开图层到各个文件");
        Menu menu=new Menu();
        menu.setText("提取SLD");
        menu.getItems().add(menuItemAll);
        menu.getItems().add(menuItemSelect);
        menu.getItems().add(menuItemSeparate);
        MenuBar menuBar=new MenuBar();
        menuBar.getMenus().add(menu);
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(event.getX()+":"+event.getY());
            }
        });
        VBox vBox=new VBox();
        vBox.getChildren().add(btn);

        FlowPane root = new FlowPane();
        root.getChildren().add(menuBar) ;
        root.getChildren().add(vBox);
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("ArcMAP样式转SLD文件");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void show(){
        launch();
    }
}
