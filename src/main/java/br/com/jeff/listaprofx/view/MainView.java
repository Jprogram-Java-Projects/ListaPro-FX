// MainView.java
package br.com.jeff.listaprofx.view;

import br.com.jeff.listaprofx.controller.TaskActionsController;
import br.com.jeff.listaprofx.controller.TaskController;
import br.com.jeff.listaprofx.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainView {

    private BorderPane root;
    private TaskTableView taskTableView;
    private FilterPanel filterPanel;
    private TaskActionsView taskActionsView;
    private ObservableList<Task> taskList;
    private TaskController controller;
    private TaskActionsController actionsController;

    public MainView(Stage primaryStage, TaskController controller) {
        this.controller = controller;

        root = new BorderPane();
        root.setPadding(new Insets(10));

        // --- Tabela ---
        taskTableView = new TaskTableView();
        taskList = FXCollections.observableArrayList(controller.getAllTasks());
        taskTableView.setTasksList(taskList);
        root.setCenter(taskTableView.getTableView());

        // --- Filtros ---
        filterPanel = new FilterPanel(controller, taskList);
        root.setRight(filterPanel);

        // --- Ações ---
        taskActionsView = new TaskActionsView();
        HBox actionsBox = taskActionsView.getHBox();
        actionsBox.setPadding(new Insets(10));
        root.setBottom(actionsBox);
        
        actionsController = new TaskActionsController(controller, taskTableView, taskList);

        // --- Configurar ações ---
        setupActions();
        controller.setupActions(taskTableView, taskActionsView);

        // --- Cena ---
        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        primaryStage.setTitle("ListaPro FX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupActions() {
    		taskActionsView.getAddBtn().setOnAction(e -> actionsController.handleAdd());
    		taskActionsView.getEditBtn().setOnAction(e -> actionsController.handleEdit());   	
    		taskActionsView.getDeleteBtn().setOnAction(e -> actionsController.handleDelete());
    		taskActionsView.getConcludeBtn().setOnAction(e -> actionsController.handleConclude());
    		taskActionsView.getExportBtn().setOnAction(e -> actionsController.handleExport());
    }
}