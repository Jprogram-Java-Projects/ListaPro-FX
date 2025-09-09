package br.com.jeff.listaprofx.service;

import br.com.jeff.listaprofx.model.Task;
import br.com.jeff.listaprofx.util.DateTimeUtils;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class TaskExportCSVService {

    /**
     * Exporta uma lista de tarefas para um arquivo CSV.
     * @param tasks lista de tarefas selecionadas
     */
    public static void exportToCsv(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            showAlert("Exportação", "Nenhuma tarefa para exportar.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar tarefas em CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        
        String timestamp = DateTimeUtils.formatForFileName(java.time.LocalDateTime.now());
        fileChooser.setInitialFileName("ListaPRO_FX_tasks_" + timestamp + ".csv");


        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) {
            return; // usuário cancelou
        }

        try (FileOutputStream fos = new FileOutputStream(file);
        	     OutputStreamWriter osw = new OutputStreamWriter(fos, java.nio.charset.StandardCharsets.UTF_8);
        	     BufferedWriter writer = new BufferedWriter(osw)) {

        	    // BOM (marca de ordem de bytes para Excel abrir certo)
        	    writer.write('\uFEFF');

        	    // cabeçalho
        	    writer.write("ID;Descrição;Prioridade;Criada em;Concluída em;Concluída\n");

        	    for (Task task : tasks) {
        	        writer.write(task.getId() + ";" +
        	                     escape(task.getDescription()) + ";" +
        	                     task.getPriority() + ";" +
        	                     DateTimeUtils.format(task.getDateCreation()) + ";" +
        	                     DateTimeUtils.format(task.getDateConclusion()) + ";" +
        	                     (task.isDone() ? "Sim" : "Não") + "\n");
        	    }

            showAlert("Exportação concluída", "Arquivo salvo em:\n" + file.getAbsolutePath());

        } catch (IOException e) {
            showAlert("Erro ao exportar", "Não foi possível salvar o arquivo:\n" + e.getMessage());
        }
    }

    // escapa possíveis ; e quebras de linha
    private static String escape(String text) {
        if (text == null) return "";
        return "\"" + text.replace("\"", "\"\"") + "\"";
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}

