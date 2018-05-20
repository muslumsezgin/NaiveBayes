package me.cosean;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import me.cosean.model.News;
import me.cosean.model.SuccessModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class JavaFxMain extends Application implements Initializable {

    @FXML
    private TreeView<String> treeView;

    @FXML
    private ChoiceBox<String> categorySelected;

    @FXML
    private Text context;

    @FXML
    private Text context_Analyzed;

    @FXML
    private Text categoryActual;

    @FXML
    private Text categoryPredicted;

    @FXML
    private Text recall;

    @FXML
    private Text precision;

    @FXML
    private Text f_measure;

    List<News> testList;

    Map<String, SuccessModel> suggestSuccessMap;

    @FXML
    private MenuItem open;

    @FXML
    void exitProgram(ActionEvent event) {
        Stage stage = (Stage) treeView.getScene().getWindow();
        stage.close();
    }

    @FXML
    void openFile(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory =
                directoryChooser.showDialog(treeView.getScene().getWindow());

        if(selectedDirectory == null){
            System.out.println("No Directory selected");
        }else{
            System.out.println(selectedDirectory.getAbsolutePath());
            createTree(selectedDirectory.getAbsolutePath());
        }
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cosean Artificial İntelligence!");
        String fxmlFile = "/fxml/template.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = loader.load(getClass().getResourceAsStream(fxmlFile));
        primaryStage.setScene(new Scene(rootNode, 1280, 800));
        primaryStage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void initData(TreeItem rootItem,String path) {
        //String path = "C:\\Users\\anil\\Desktop\\1150haber\\raw_texts";

        String[] stopWords = PreProcessing.getStopWord();
        try {
            Map<String, List<News>> stringArrayListHashMap = PreProcessing.execute(path, stopWords);
            Map<String, List<News>> trainMap = new HashMap<>();
            testList = new ArrayList<>();

            stringArrayListHashMap.forEach((category, newsList) -> {
                Collections.shuffle(newsList);
                int v = (int) (newsList.size() * 0.75);
                trainMap.put(category, newsList.subList(0, v));
                List<News> filterList = newsList.subList(v, newsList.size()).stream()
                        .filter(k -> k.getNgramMap().size() > 0).collect(Collectors.toList());
                testList.addAll(filterList);
            });
            NaiveBayes naiveBayes = new NaiveBayes(trainMap);
            naiveBayes.learn();
            suggestSuccessMap = naiveBayes.suggest(testList);
            testList.forEach(news -> {
                TreeItem<String> item = new TreeItem<>();
                item.setValue(news.getName());
                rootItem.getChildren().add(item);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void createTree(String path){
        TreeItem<String> rootItem = new TreeItem<>("Test List");
        rootItem.setExpanded(true);
        initData(rootItem,path);
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue,
                                Object newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                News selectedNews = testList.stream().filter(x -> x.getName().equalsIgnoreCase(selectedItem.getValue())).findFirst().get();
                context.setText(selectedNews.getData());
                context_Analyzed.setText(selectedNews.getAnalyzeData());
                categoryActual.setText(selectedNews.getType());
                categoryPredicted.setText(selectedNews.getPredictedType());
            }

        });
        treeView.setRoot(rootItem);
        categorySelected.setItems(FXCollections.observableList(new ArrayList<>(suggestSuccessMap.keySet())));
        categorySelected.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    SuccessModel successModel = suggestSuccessMap.get(newValue);
                    recall.setText(String.valueOf(successModel.getRecall()));
                    precision.setText(String.valueOf(successModel.getPrecision()));
                    f_measure.setText(String.valueOf(successModel.getF_Meaure()));
                });
    }
}
