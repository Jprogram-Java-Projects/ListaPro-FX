package br.com.jeff.listaprofx;

import br.com.jeff.listaprofx.controller.TaskController;
import br.com.jeff.listaprofx.dao.TaskJdbcDAO;
import br.com.jeff.listaprofx.view.MainView;
import org.flywaydb.core.Flyway;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // --- Caminho do banco SQLite ---
            String dbPath = "listaProFX.db";

            // --- Inicializar Flyway ---
            Flyway flyway = Flyway.configure()
                    .dataSource("jdbc:sqlite:" + dbPath, null, null)
                    .locations("classpath:db/migration") // pasta onde estão os SQLs
                    .load();

            // Aplica migrations pendentes
            flyway.migrate();

            // --- Inicializar DAO e Controller ---
            TaskJdbcDAO taskDAO = new TaskJdbcDAO(dbPath);
            TaskController controller = new TaskController(taskDAO);

            // --- Inicializar a MainView ---
            MainView mainView = new MainView(primaryStage, controller);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
