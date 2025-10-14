package br.com.jeff.listaprofx.dao;

import br.com.jeff.listaprofx.model.Priority;
import br.com.jeff.listaprofx.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskJdbcDAOTest {

	private Connection sharedConn;
    private TaskJdbcDAO dao;
    private final String url = "jdbc:sqlite:file:memdb1?mode=memory&cache=shared";;

    @BeforeAll
    void setupDatabase() throws Exception {
    		sharedConn = DriverManager.getConnection(url);
        dao = new TaskJdbcDAO("file:memdb1?mode=memory&cache=shared");
        
        try (Statement stmt = sharedConn.createStatement()) {
            stmt.execute("""
                CREATE TABLE tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    description TEXT NOT NULL,
                    date_creation TIMESTAMP NOT NULL,
                    date_conclusion TIMESTAMP,
                    priority TEXT NOT NULL,
                    done INTEGER NOT NULL
                )
            """);
        }
    }

    @BeforeEach
    void clearTable() throws Exception {
        try (Statement stmt = sharedConn.createStatement()) {
            stmt.execute("DELETE FROM tasks");
        }
    }
    
    @AfterAll
    void tearDown() throws Exception {
        if (sharedConn != null && !sharedConn.isClosed()) {
            sharedConn.close();
        }
    }

    private Task buildTask(String description, boolean done) {
        Task t = new Task();
        t.setDescription(description);
        t.setDateCreation(LocalDateTime.now());
        t.setDateConclusion(null);
        t.setPriority(Priority.ALTA);
        t.setDone(done);
        return t;
    }
    
    private TaskJdbcDAO brokenDaoInvalidDb() {
        return new TaskJdbcDAO("jdbc:sqlite:file:invalid_db?mode=ro");
    }

    private TaskJdbcDAO brokenDaoSameDb() {
        return new TaskJdbcDAO("file:memdb1?mode=memory&cache=shared");
    }


    @Test
    void save_shouldPersistAndReturnTaskWithId() {
        // Arrange
        Task task = buildTask("Test save", false);

        // Act
        Task saved = dao.save(task);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("Test save", saved.getDescription());
    }

    @Test
    void delete_shouldReturnTrueWhenTaskExists() {
        // Arrange
        Task task = dao.save(buildTask("To delete", false));

        // Act
        boolean result = dao.delete(task.getId());

        // Assert
        assertTrue(result);
    }

    @Test
    void delete_shouldReturnFalseWhenTaskDoesNotExist() {
        // Arrange
        int nonExistentId = 999;

        // Act
        boolean result = dao.delete(nonExistentId);

        // Assert
        assertFalse(result);
    }

    @Test
    void update_shouldModifyTaskAndReturnOptional() {
        // Arrange
        Task task = dao.save(buildTask("To update", false));
        task.setDescription("Updated description");
        task.setDone(true);

        // Act
        Optional<Task> updated = dao.update(task);

        // Assert
        assertTrue(updated.isPresent());
        assertEquals("Updated description", updated.get().getDescription());
        assertTrue(updated.get().isDone());
    }

    @Test
    void update_shouldReturnEmptyWhenTaskDoesNotExist() {
        // Arrange
        Task task = buildTask("Non existing", false);
        task.setId(999);

        // Act
        Optional<Task> result = dao.update(task);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void listAll_shouldReturnAllSavedTasks() {
        // Arrange
        dao.save(buildTask("Task 1", false));
        dao.save(buildTask("Task 2", true));

        // Act
        List<Task> tasks = dao.listAll();

        // Assert
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getDescription());
        assertEquals("Task 2", tasks.get(1).getDescription());
    }

    @Test
    void listAll_shouldReturnEmptyListWhenNoTasksExist() {
        // Arrange - database already cleared

        // Act
        List<Task> tasks = dao.listAll();

        // Assert
        assertTrue(tasks.isEmpty());
    }
    
    
    // --------------------
    // Error Handling
    // --------------------

    @Test
    void save_shouldThrowRuntimeExceptionOnSqlError() throws Exception {
        TaskJdbcDAO brokenDao = brokenDaoInvalidDb();

        Task task = buildTask("Broken save", false);

        assertThrows(RuntimeException.class, () -> brokenDao.save(task));
    }
    
    @Test
    void save_shouldThrowRuntimeExceptionWhenNoRowsAffected() {
        // Arrange
        TaskJdbcDAO brokenDao = brokenDaoSameDb();
        Task task = buildTask("No rows affected", false);

        // Criando tabela sem permitir insert (simulando erro de constraint)
        assertThrows(RuntimeException.class, () -> {
            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {
                stmt.execute("DROP TABLE IF EXISTS tasks");
                stmt.execute("""
                    CREATE TABLE tasks (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        description TEXT NOT NULL UNIQUE,
                        date_creation TIMESTAMP NOT NULL,
                        date_conclusion TIMESTAMP,
                        priority TEXT NOT NULL,
                        done INTEGER NOT NULL
                    )
                """);
            }
            // Act + Assert
            brokenDao.save(task); // como description é UNIQUE, segundo insert quebra
            brokenDao.save(task);
        });
    }

    @Test
    void update_shouldThrowRuntimeExceptionOnSqlError() {
        // Arrange
        TaskJdbcDAO brokenDao = brokenDaoInvalidDb();
        Task task = buildTask("Update fails", false);
        task.setId(1);

        // Act + Assert
        assertThrows(RuntimeException.class, () -> brokenDao.update(task));
    }

    @Test
    void listAll_shouldHandleTaskWithConclusionDate() {
        // Arrange
        Task task = buildTask("With conclusion date", false);
        task.setDateConclusion(LocalDateTime.now());
        dao.save(task);

        // Act
        List<Task> tasks = dao.listAll();

        // Assert
        assertEquals(1, tasks.size());
        assertNotNull(tasks.get(0).getDateConclusion());
    }


    @Test
    void delete_shouldThrowRuntimeExceptionOnSqlError() {
        TaskJdbcDAO brokenDao = brokenDaoInvalidDb();

        assertThrows(RuntimeException.class, () -> brokenDao.delete(1));
    }


    @Test
    void listAll_shouldThrowRuntimeExceptionOnSqlError() {
        TaskJdbcDAO brokenDao = brokenDaoInvalidDb();

        assertThrows(RuntimeException.class, brokenDao::listAll);
    }

    @Test
    void constructor_shouldInitializeWithValidUrl() {
        TaskJdbcDAO newDao = brokenDaoSameDb();
        assertNotNull(newDao);
    }
}
