// FilterPanel.java
package br.com.jeff.listaprofx.view;

import br.com.jeff.listaprofx.controller.TaskController;
import br.com.jeff.listaprofx.model.Priority;
import br.com.jeff.listaprofx.model.Task;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class FilterPanel extends VBox {

    private TextField descriptionFilter;
    private ComboBox<Priority> priorityFilter;
    private CheckBox doneFilter;
    private Button applyBtn;
    private ObservableList<Task> filteredTasks;
    private TaskController controller;

    public FilterPanel(TaskController controller, ObservableList<Task> tasks) {
        this.controller = controller;
        this.filteredTasks = tasks;

        descriptionFilter = new TextField();
        descriptionFilter.setPromptText("Filtrar por descrição...");
        descriptionFilter.getStyleClass().add("filter-field");

        priorityFilter = new ComboBox<>();
        priorityFilter.getItems().setAll(Priority.values());
        priorityFilter.getStyleClass().add("filter-combo");
        priorityFilter.setPromptText("Todas as prioridades");

        doneFilter = new CheckBox("Concluídas");
        doneFilter.getStyleClass().add("filter-checkbox");

        applyBtn = new Button("Aplicar");
        applyBtn.getStyleClass().add("button");
        applyBtn.setOnAction(e -> applyFilters());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
        	    new Label("Descrição:") {{ getStyleClass().add("filter-label"); }}, descriptionFilter,
        	    new Label("Prioridade:") {{ getStyleClass().add("filter-label"); }}, priorityFilter,
        	    doneFilter,
        	    applyBtn
        	);

        layout.getStyleClass().add("filter-panel");

        getChildren().add(layout);
    }

    public void applyFilters() {
        String desc = descriptionFilter.getText().trim();
        Priority priority = priorityFilter.getValue();
        boolean isDone = doneFilter.isSelected();

        // Filtrar tarefas
        filteredTasks.clear();
        for (Task t : controller.getAllTasks()) {
            boolean matchesDesc = desc.isEmpty() || t.getDescription().toLowerCase().contains(desc.toLowerCase());
            boolean matchesPriority = priority == null || t.getPriority() == priority;
            boolean matchesStatus = !isDone || t.isDone();

            if (matchesDesc && matchesPriority && matchesStatus) {
                filteredTasks.add(t);
            }
        }
    }

    public void clearFilters() {
        descriptionFilter.clear();
        priorityFilter.setValue(null);
        doneFilter.setSelected(false);
        applyFilters();
    }
}