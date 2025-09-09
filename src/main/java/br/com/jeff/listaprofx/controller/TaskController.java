package br.com.jeff.listaprofx.controller;

import br.com.jeff.listaprofx.dao.TaskJdbcDAO;
import br.com.jeff.listaprofx.model.Priority;
import br.com.jeff.listaprofx.model.Task;
import br.com.jeff.listaprofx.view.TaskActionsView;
import br.com.jeff.listaprofx.view.TaskTableView;

import javafx.collections.ListChangeListener;

import java.util.List;
import java.util.Optional;

public class TaskController {

    private final TaskJdbcDAO taskJdbc;

    public TaskController(TaskJdbcDAO taskJdbc) {
        this.taskJdbc = taskJdbc;
    }

    public Task addTask(String description, Priority priority) {
        Task task = new Task(0, description, priority); // id é gerado no DAO
        return taskJdbc.save(task);
    }


    public List<Task> getAllTasks() {
        return taskJdbc.listAll();
    }

    public Optional<Task> markTaskAsDone(int id) {
        Optional<Task> optTask = taskJdbc.listAll()
                .stream()
                .filter(t -> t.getId() == id)
                .findFirst();

        if (optTask.isPresent()) {
            Task task = optTask.get();
            task.markAsDone();
            return taskJdbc.update(task);
        }
        return Optional.empty();
    }

    public Optional<Task> updateTask(Task task) {
        return taskJdbc.update(task);
    }

    public Optional<Task> getTaskById(int id) {
        return taskJdbc.listAll().stream()
                .filter(t -> t.getId() == id)
                .findFirst();
    }

    public boolean deleteTask(int id) {
        return taskJdbc.delete(id);
    }
    
    public void setupActions(TaskTableView taskTableView, TaskActionsView actionsView) {

        // Listener para alterações na seleção da tabela
        //taskTableView.getTableView().getSelectionModel().getSelectedItems()
        //    .addListener((ListChangeListener<Task>) change -> updateButtons(taskTableView, actionsView));
        
        // Listener para checkbox
        taskTableView.getCheckedTasks()
            .addListener((ListChangeListener<Task>) change -> updateButtons(taskTableView, actionsView));
    }

    private void updateButtons(TaskTableView tableView, TaskActionsView actionsView) {
        boolean hasSelection = !tableView.getCheckedTasks().isEmpty();
        actionsView.setAddDisabled(hasSelection);   
        actionsView.setEditDisabled(!hasSelection);
        actionsView.setDeleteDisabled(!hasSelection);
        actionsView.setConcludeDisabled(!hasSelection);
        actionsView.setExportDisabled(!hasSelection);
    }

}
