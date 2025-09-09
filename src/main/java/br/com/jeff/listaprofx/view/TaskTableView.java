package br.com.jeff.listaprofx.view;

import br.com.jeff.listaprofx.controller.TaskController;
import br.com.jeff.listaprofx.model.Task;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;

public class TaskTableView {

    private final TableView<Task> tableView;
    private final TableColumn<Task, Boolean> selectCol;
    private final TableColumn<Task, Integer> idCol;
    private final TableColumn<Task, String> descCol;
    private final TableColumn<Task, String> priorityCol;
    private final TableColumn<Task, String> dateCreationCol;
    private final TableColumn<Task, String> dateConclusionCol;
    private final TableColumn<Task, String> statusCol;
    private final ObservableList<Task> checkedTasks = FXCollections.observableArrayList();

    public TaskTableView() {
        tableView = new TableView<>();
        tableView.getStyleClass().add("task-table");

        // Coluna de seleção (CheckBox)
        selectCol = new TableColumn<>("✔");
        selectCol.setCellValueFactory(param -> new SimpleObjectProperty<>(false));
        selectCol.setCellFactory(col -> actionCheckBox());
        selectCol.setEditable(true);

        // ID (escondido por padrão)
        idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setVisible(false);

        // Descrição
        descCol = new TableColumn<>("Descrição");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setMinWidth(300);

        // Prioridade
        priorityCol = new TableColumn<>("Prioridade");
        priorityCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getPriority().name()));

        // Data de criação
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        dateCreationCol = new TableColumn<>("Criada em");
        dateCreationCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getDateCreation() != null
                        ? cell.getValue().getDateCreation().format(formatter)
                        : ""));

        // Data de conclusão (oculta)
        dateConclusionCol = new TableColumn<>("Concluída em");
        dateConclusionCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getDateConclusion() != null
                        ? cell.getValue().getDateConclusion().format(formatter)
                        : ""));
        dateConclusionCol.setVisible(true);

        // Status
        statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().isDone() ? "Concluída" : "Em andamento"));

        tableView.getColumns().addAll(selectCol, idCol, descCol, priorityCol, dateCreationCol, dateConclusionCol, statusCol);
        tableView.setEditable(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    
    private TableCell<Task, Boolean> actionCheckBox() {
        return new TableCell<Task, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(e -> {
                    Task task = getTableView().getItems().get(getIndex());
                    boolean checked = checkBox.isSelected();

                    if (checked) {
                        if (!checkedTasks.contains(task)) {
                            checkedTasks.add(task);
                        }
                    } else {
                        checkedTasks.remove(task);
                    }

                    // System.out.println("Checkbox da tarefa '" + task.getDescription() + "' -> " + checked);
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                    Task task = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(checkedTasks.contains(task));
                }
            }
        };
    }

    public TableView<Task> getTableView() {
        return tableView;
    }

    public void setTasksList(ObservableList<Task> tasksList) {
        tableView.setItems(tasksList);
    }

    // Mostrar/ocultar coluna ID
    public void toggleIdColumn(boolean visible) {
        idCol.setVisible(visible);
    }

    // Mostrar/ocultar coluna Conclusão
    public void toggleConclusionDateColumn(boolean visible) {
        dateConclusionCol.setVisible(visible);
    }

    public ObservableList<Task> getCheckedTasks() {
        return checkedTasks;
    }
}
