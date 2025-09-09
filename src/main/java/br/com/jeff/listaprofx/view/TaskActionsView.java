package br.com.jeff.listaprofx.view;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class TaskActionsView {

    private Button addBtn;
    private Button editBtn;
    private Button deleteBtn;
    private Button concludeBtn;
    private Button exportBtn;
    private HBox hBox;

    public TaskActionsView() {
        addBtn    = createButton("Adicionar", "btn-add", "/icons/add.png");
        editBtn   = createButton("Editar", "btn-edit", "/icons/edit.png");
        deleteBtn = createButton("Excluir", "btn-delete", "/icons/delete.png");
        concludeBtn = createButton("Concluir", "btn-conclude", "/icons/check.png");
        exportBtn = createButton("Exportar", "btn-export", "/icons/csv.png");
        
        // Inicialmente: somente Adicionar habilitado
        addBtn.setDisable(false);
        editBtn.setDisable(true);
        deleteBtn.setDisable(true);
        concludeBtn.setDisable(true);
        exportBtn.setDisable(true);
        
        // Espaçador flexível
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Botões à esquerda + export separado à direita
        hBox = new HBox(12, addBtn, editBtn, deleteBtn, concludeBtn, spacer, exportBtn);
        hBox.getStyleClass().add("bottom-toolbar");
    }

    public HBox getHBox() { return hBox; }
    public Button getAddBtn() { return addBtn; }
    public Button getEditBtn() { return editBtn; }
    public Button getDeleteBtn() { return deleteBtn; }
    public Button getConcludeBtn() { return concludeBtn; }
    public Button getExportBtn() { return exportBtn; }

    // Métodos de controle via Controller
    public void setAddDisabled(boolean disabled) { addBtn.setDisable(disabled); }
    public void setEditDisabled(boolean disabled) { editBtn.setDisable(disabled); }
    public void setDeleteDisabled(boolean disabled) { deleteBtn.setDisable(disabled); }
    public void setConcludeDisabled(boolean disabled) { concludeBtn.setDisable(disabled); }
    public void setExportDisabled(boolean disabled) { exportBtn.setDisable(disabled); }
    
    private Button createButton(String text, String styleClass, String iconPath) {
        Button button = new Button(text);

        ImageView icon = new ImageView(getClass().getResource(iconPath).toExternalForm());
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        icon.setPreserveRatio(true);

        button.setGraphic(icon);
        button.getStyleClass().add(styleClass);

        return button;
    }
}
