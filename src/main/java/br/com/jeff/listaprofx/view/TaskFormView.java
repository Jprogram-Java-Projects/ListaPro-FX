package br.com.jeff.listaprofx.view;

import br.com.jeff.listaprofx.model.Priority;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TaskFormView {

	private TextArea descriptionField;
    private ComboBox<Priority> priorityCombo;
    private CheckBox doneCheck;
    private Button saveBtn;
    private Button cancelBtn; 
    private VBox layout;

    public TaskFormView() {
        // Campo de descrição
        descriptionField = new TextArea();
        descriptionField.setPromptText("Digite a descrição da tarefa...");
        descriptionField.getStyleClass().add("task-description");

        // Combo de prioridade
        priorityCombo = new ComboBox<>();
        priorityCombo.getItems().setAll(Priority.values());
        priorityCombo.getStyleClass().add("task-priority");

        // Checkbox
        doneCheck = new CheckBox("Concluída");
        doneCheck.getStyleClass().add("task-checkbox");

        // Botões
        saveBtn = new Button("Salvar");
        saveBtn.setDefaultButton(true);
        saveBtn.getStyleClass().add("task-save-btn");

        cancelBtn = new Button("Cancelar");
        cancelBtn.getStyleClass().add("task-cancel-btn");
        
        // Linha de botões
        HBox buttonsBox = new HBox(12, saveBtn, cancelBtn);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getStyleClass().add("dialog-buttons");

        // Conteúdo do formulário
        VBox content = new VBox(18);
        content.setPadding(new Insets(20, 24, 20, 24)); // <-- padding em volta
        content.getStyleClass().add("add-task-content");
        
        // Linha de prioridade + concluída
        HBox priorityRow = new HBox(12, priorityCombo, doneCheck);
        priorityRow.setAlignment(Pos.CENTER_LEFT);
        
        content.getChildren().addAll(
        	    new Label("Descrição:"), descriptionField,
        	    new Label("Prioridade:"), priorityRow,
        	    buttonsBox
        	);

        content.getStyleClass().add("add-task-content");

        // Layout principal (junta cabeçalho + conteúdo)
        layout = new VBox();
        layout.getChildren().add(content);
        layout.getStyleClass().add("add-task-dialog");
    }
    
    public VBox getLayout() { return layout; }
    public Button getSaveBtn() { return saveBtn; }
    public Button getCancelBtn() { return cancelBtn; } 
    public String getDescription() { return descriptionField.getText().trim(); }
    public Priority getPriority() { return priorityCombo.getValue(); }
    public boolean isDone() { return doneCheck.isSelected(); }

    public void clearFields() {
        descriptionField.clear();
        priorityCombo.setValue(null);
        doneCheck.setSelected(false);
    }
    
    public void setDescription(String description) {
        descriptionField.setText(description == null ? "" : description);
    }

    public void setPriority(Priority priority) {
        priorityCombo.setValue(priority);
    }

    public void setDone(boolean done) {
        doneCheck.setSelected(done);
    }
}
