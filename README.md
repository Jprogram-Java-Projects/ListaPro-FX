# ListaProFX

## Descrição do Projeto

ListaProFX é um aplicativo desktop desenvolvido em **Java** com **JavaFX**, que permite gerenciar tarefas (to-do list) de forma simples, moderna e intuitiva. O projeto segue boas práticas de desenvolvimento, aplicando **POO**, **MVC**, **DAO** e princípios de **Clean Code**.

## Funcionalidades

* Adicionar tarefas com descrição, prioridade (ALTA, MÉDIA, BAIXA) e status de conclusão
* Listar todas as tarefas em uma tabela com informações de criação, conclusão, prioridade e status
* Marcar tarefas como concluídas
* Editar tarefas existentes
* Excluir tarefas
* Exportar tarefas selecionadas para arquivo CSV
* Persistência de dados em **SQLite**
* Interface gráfica estilizada com **JavaFX CSS**

## Tecnologias Utilizadas

* **Java 21**
* **JavaFX 21** (controls, fxml)
* **SQLite (JDBC)**
* **OpenCSV** para exportação
* **Flyway** para migração do banco de dados
* **JUnit 5** para testes unitários
* **POO**, **MVC**, **DAO**, **Clean Code**

## Estrutura do Projeto

```
ListaProFX/
 ├─ src/main/java/br/com/jeff/listaprofx/
 │   ├─ controller/        # Controladores (TaskController, TaskFormController, TaskActionsController)
 │   ├─ dao/               # DAO e implementações (TaskDAO, TaskJdbcDAO)
 │   ├─ model/             # Modelos (Task, Priority)
 │   ├─ service/           # Serviços (TaskExportCSVService)
 │   ├─ util/              # Utilitários (DateTimeUtils)
 │   └─ view/              # Views JavaFX (TaskTableView, TaskFormView, TaskActionsView)
 ├─ src/main/resources/
 │   ├─ css/               # Arquivos de estilo CSS
 │   ├─ icons/             # Ícones do projeto (svg, png) — ex.: icon-add.png, icon-edit.svg
 │   └─ db/migration/      # Scripts Flyway para migração do banco
 └─ pom.xml                # Configuração Maven
```

## Design e Padrões de Projeto

* **MVC (Model-View-Controller)**: separa lógica de negócio, interface e dados
* **DAO**: abstrai acesso ao banco de dados SQLite
* **SOLID & Clean Code**: código limpo, coeso e de fácil manutenção
* **Enums**: Priority para prioridades seguras
* **Utils**: DateTimeUtils para formatação de datas
* **Service**: TaskExportCSVService centraliza a exportação CSV

## Funcionalidades Visuais

* Interface moderna com **JavaFX CSS**
* Botões de ação (Adicionar, Editar, Excluir, Exportar)
* Formulário modal para criação e edição de tarefas
* Tabela interativa com checkbox de seleção
* Feedback visual em casos de erro/validação
* Ícones customizados para botões e menus

## Como Rodar

### Via IDE (IntelliJ, Eclipse, NetBeans)

1. Clonar o repositório
2. Importar o projeto Maven
3. Garantir que a JDK 21 esteja configurada
4. Executar a classe `App.java`

### Via Maven no Terminal

```bash
mvn clean javafx:run
```

### Gerar JAR executável

```bash
mvn clean package
```

O arquivo será gerado em `target/listaprofx-0.0.1-SNAPSHOT-jar-with-dependencies.jar`.

Para executar:

```bash
java -jar target/listaprofx-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

## Estrutura de Classes

* **Model**: Task.java, Priority.java
* **DAO**: TaskDAO.java, TaskJdbcDAO.java
* **Controller**: TaskController.java, TaskFormController.java, TaskActionsController.java
* **View**: TaskTableView\.java, TaskFormView\.java, TaskActionsView\.java
* **Service**: TaskExportService.java (ou TaskExportCSVService.java) — responsável pela exportação CSV/JSON
* **Utils**: DateTimeUtils.java

## Observações

* Data de conclusão preenchida ao marcar tarefa como concluída
* Exportação CSV com encoding UTF-8 e separador `;`
* Ícones carregados via `src/main/resources/icons/` usando `ImageView`

## Contribuições

Este projeto serve como referência para aprendizado em **JavaFX**, **SQLite** e boas práticas de arquitetura em aplicações desktop. Sugestões de melhorias e novos recursos são bem-vindas.

---

> **ListaProFX** - Aplicativo de gerenciamento de tarefas em Java com JavaFX e SQLite.
