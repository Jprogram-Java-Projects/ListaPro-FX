package br.com.jeff.listaprofx.controller;

import br.com.jeff.listaprofx.view.TaskTableView;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.List;

import br.com.jeff.listaprofx.controller.TaskController;
import br.com.jeff.listaprofx.model.Task;
import br.com.jeff.listaprofx.service.TaskExportCSVService;

public class TaskActionsController {

    private final TaskController taskController;
    private final TaskFormController taskFormController;
    private final TaskTableView taskTableView;
    private final ObservableList<Task> taskList;

    
    public TaskActionsController(TaskController taskController,
					            TaskTableView taskTableView,
					            TaskFormController taskFormController,
					            ObservableList<Task> taskList) {
		this.taskController = taskController;
		this.taskFormController = taskFormController;
		this.taskTableView = taskTableView;
		this.taskList = taskList;
	}

    
    // ADD
    public void handleAdd() {
        taskFormController.openTaskFormDialog(null, taskList);
    }

    // EDIT
    public void handleEdit() {
        ObservableList<Task> checked = taskTableView.getCheckedTasks();
        if (checked.isEmpty() || checked.size() > 1) {
            showInfo("Editar tarefa", "Selecione uma tarefa para editar.");
            return;
        }

        Task toEdit = checked.get(0);
        taskFormController.openTaskFormDialog(toEdit, taskList);
    }


    // DELETE
    /** Remove todas as tarefas marcadas (confirmação mínima). */
    public void handleDelete() {
        ObservableList<Task> checked = taskTableView.getCheckedTasks();
        if (checked.isEmpty()) {
            showInfo("Excluir tarefas", "Nenhuma tarefa selecionada.");
            return;
        }

        // copia lista para evitar ConcurrentModification
        List<Task> toDelete = new ArrayList<>(checked);

        int removedCount = 0;
        for (Task t : toDelete) {
            boolean ok = taskController.deleteTask(t.getId());
            if (ok) {
                taskList.remove(t);            // remove da lista visível
                taskTableView.getCheckedTasks().remove(t); // limpe checkbox também
                removedCount++;
            }
        }

        showInfo("Excluir tarefas", removedCount + " tarefa(s) removida(s).");
    }
    
 // CONCLUDE
    public void handleConclude() {
        ObservableList<Task> checked = taskTableView.getCheckedTasks();
        if (checked.isEmpty()) {
            showInfo("Concluir tarefas", "Nenhuma tarefa selecionada.");
            return;
        }

        List<Task> toConclude = new ArrayList<>(checked);
        int concludedCount = 0;

        for (Task t : toConclude) {
            var updated = taskController.markTaskAsDone(t.getId());
            if (updated.isPresent()) {
                Task updatedTask = updated.get();

                // substitui na lista observável
                int index = taskList.indexOf(t);
                if (index >= 0) {
                    taskList.set(index, updatedTask);
                }

                // desmarca da lista de "checked"
                taskTableView.getCheckedTasks().remove(t);

                concludedCount++;
            }
        }

        showInfo("Concluir tarefas", concludedCount + " tarefa(s) concluída(s).");
    }


    // EXPORT
    public void handleExport() {
        var selectedTasks = taskTableView.getCheckedTasks();
        if (selectedTasks.isEmpty()) {
            showInfo("Exportar tarefas", "Nenhuma tarefa selecionada para exportação.");
            return;
        }

        TaskExportCSVService.exportToCsv(new ArrayList<>(selectedTasks));
    }
    
    // utilitário para mensagens simples
    protected void showInfo(String title, String message) {
        Alert a = new Alert(AlertType.INFORMATION, message);
        a.setTitle(title);
        a.setHeaderText(null);
        a.showAndWait();
    }
}

