package br.com.jeff.listaprofx.controller;

import br.com.jeff.listaprofx.model.Task;
import br.com.jeff.listaprofx.view.TaskFormView;
import br.com.jeff.listaprofx.controller.TaskController;

import javafx.collections.ObservableList;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TaskFormController {

    private final TaskController taskController;

    public TaskFormController(TaskController taskController) {
        this.taskController = taskController;
    }

    public void openTaskFormDialog(Task task, ObservableList<Task> taskList) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);

        boolean isEdit = (task != null);
        dialog.setTitle(isEdit ? "Editar Tarefa" : "Adicionar Tarefa");

        TaskFormView formView = new TaskFormView();
        Scene scene = new Scene(formView.getLayout());
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialog.setScene(scene);

        if (isEdit) {
            formView.setDescription(task.getDescription());
            formView.setPriority(task.getPriority());
            formView.setDone(task.isDone());
        }

        // Botão salvar
        formView.getSaveBtn().setOnAction(e -> handleSave(formView, task, taskList, isEdit, dialog));

        // Botão cancelar
        formView.getCancelBtn().setOnAction(e -> dialog.close());

        dialog.showAndWait();
    }
    
    private void handleSave(TaskFormView formView, Task task, ObservableList<Task> taskList, boolean isEdit, Stage dialog) {
        if (!isFormValid(formView)) {
            showValidationAlert();
            return;
        }

        if (isEdit) {
            updateExistingTask(formView, task, taskList);
        } else {
            createNewTask(formView, taskList);
        }

        dialog.close();
    }

    private boolean isFormValid(TaskFormView formView) {
        return !formView.getDescription().isEmpty() && formView.getPriority() != null;
    }

    private void showValidationAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Descrição e prioridade são obrigatórias.");
        alert.showAndWait();
    }

    private void updateExistingTask(TaskFormView formView, Task task, ObservableList<Task> taskList) {
        task.setDescription(formView.getDescription());
        task.setPriority(formView.getPriority());
        task.setDone(formView.isDone());
        taskController.updateTask(task);
        taskList.set(taskList.indexOf(task), task); // Atualiza na tabela
    }

    private void createNewTask(TaskFormView formView, ObservableList<Task> taskList) {
        Task newTask = taskController.addTask(formView.getDescription(), formView.getPriority());
        if (formView.isDone()) {
            newTask.markAsDone();
            taskController.updateTask(newTask);
        }
        taskList.add(newTask);
    }

}
