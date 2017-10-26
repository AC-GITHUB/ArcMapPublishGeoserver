package forms;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import service.MainFormProcess;
import service.ShowPrompt;

import java.io.File;


public class MainForm extends Application {
    Scene scene;
    Stage stage;
    TextField txtSLDPath;
    Button btnSave;
    MenuBar menuBar;
    Menu menuFile;
    MenuItem menuItemAll;
    MenuItem menuItemSelect;
    MenuItem menuItemSeparate;
    RadioButton rbtnSDE;
    ComboBox cmbLargeScale;
    ComboBox cmbSmallScale;
    RadioButton rbtnStabdard;
    CheckBox cbxSLDValidation;
    Button btnBuildSLD;
    ListView<String> lstInfoShow;
    MainFormProcess mainFormProcess=new MainFormProcess();
    @Override
    public void start(Stage primaryStage) {
        try{
            stage=primaryStage;
            Parent root = FXMLLoader.load(getClass().getResource("mainForm.fxml"));
            scene = new Scene(root, 610, 400);
            primaryStage.setTitle("ArcMap格式转为SLD");
            primaryStage.setScene(scene);
            primaryStage.show();
            initControls(root);
            registerEvent();
        }catch (Exception ex){
            ex.printStackTrace();

        }
    }
    public void show(){
        launch();

    }
    private void initControls(Parent root){
        txtSLDPath=(TextField) root.lookup("#txtSLDPath");
        btnSave=(Button)root.lookup("#btnSave");
        menuBar=(MenuBar) root.lookup("#menuBar");
        menuFile= menuBar.getMenus()
                .parallelStream()
                .filter((a)->{ return "menuFile".equals(a.getId()); })
                .findFirst()
                .get();
        menuItemAll= menuFile.getItems()
                .parallelStream()
                .filter((a)->{ return "menuItemAll".equals(a.getId()); })
                .findFirst()
                .get();
        menuItemSelect= menuFile.getItems()
                .parallelStream()
                .filter((a)->{ return "menuItemSelect".equals(a.getId()); })
                .findFirst()
                .get();
        menuItemSeparate= menuFile.getItems()
                .parallelStream()
                .filter((a)->{ return "menuItemSeparate".equals(a.getId()); })
                .findFirst()
                .get();
        rbtnSDE=(RadioButton) root.lookup("#rbtnSDE");
        cmbLargeScale=(ComboBox) root.lookup("#cmbLargeScale");
        cmbSmallScale=(ComboBox) root.lookup("#cmbSmallScale");
        rbtnStabdard=(RadioButton) root.lookup("#rbtnStabdard");
        cbxSLDValidation=(CheckBox) root.lookup("#cbxSLDValidation");
        btnBuildSLD=(Button) root.lookup("#btnBuildSLD");
        lstInfoShow=(ListView)root.lookup("#lstInfoShow");
    }
    private void registerEvent(){
        btnSave.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("保存文件");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("SLD","*.sld"));
                File file=fileChooser.showSaveDialog(stage);
                if(file!=null){
                    txtSLDPath.setText(file.getPath());
                    mainFormProcess.setmSLDFilename(txtSLDPath.getText());
                }
        });
        menuItemAll.setOnAction(event -> {
            mainFormProcess.setmAllLayers(true);
        });
        menuItemSelect.setOnAction(event -> {
            mainFormProcess.setmAllLayers(false);
        });
        menuItemSeparate.setOnAction(event -> {
            mainFormProcess.setmSeparateFiles(true);
        });
        btnBuildSLD.setOnAction(event -> {
            mainFormProcess.outputSLDFile();
        });
    }
}
