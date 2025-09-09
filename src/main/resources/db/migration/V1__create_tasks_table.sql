-- V1__create_tasks_table.sql
-- Criação da tabela tasks

CREATE TABLE IF NOT EXISTS tasks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,       -- ID único da tarefa
    description TEXT NOT NULL,                  -- Descrição da tarefa
    date_creation DATETIME NOT NULL,            -- Data/hora de criação
    date_conclusion DATETIME,                   -- Data/hora de conclusão (pode ser NULL)
    priority TEXT NOT NULL,                     -- Prioridade: ALTA, MÉDIA, BAIXA
    done INTEGER NOT NULL                       -- Status concluído: 0 = não, 1 = sim
);

-- Índices para otimizar consultas
CREATE INDEX IF NOT EXISTS idx_tasks_done ON tasks(done);
CREATE INDEX IF NOT EXISTS idx_tasks_priority ON tasks(priority);
CREATE INDEX IF NOT EXISTS idx_tasks_date_creation ON tasks(date_creation);
