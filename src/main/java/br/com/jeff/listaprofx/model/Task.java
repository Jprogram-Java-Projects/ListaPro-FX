package br.com.jeff.listaprofx.model;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private String description;
    private LocalDateTime dateCreation;
    private LocalDateTime dateConclusion;
    private Priority priority;
    private boolean done;
    
    public Task() {}
    
    public Task(int id, String description, Priority priority) {
        this.id = id;
        this.description = description;
        this.dateCreation = LocalDateTime.now();
        this.priority = priority;
        this.done = false;
    }
    
    public int getId() {
    		return this.id;
    }
    
    public void setId(int newId) {
    		this.id = newId;
    }
    
    public String getDescription() {
    		return this.description;
    }
    
    public void setDescription(String description) {
    		this.description = description;
    }
    
    public boolean isDone() {
    		return this.done;
    }
    
    public void setDone(boolean done) {
    		this.done = done;
    }
    
    public LocalDateTime getDateCreation() {
        return this.dateCreation;
    }
    
    public void setDateCreation(LocalDateTime d) {
    		this.dateCreation = d;
    }

    public LocalDateTime getDateConclusion() {
        return this.dateConclusion;
    }
    
    public void setDateConclusion(LocalDateTime d) {
        this.dateConclusion = d;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void markAsDone() {
        this.done = true;
        this.dateConclusion = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "[" + (this.done ? "✔" : "✘") + "] "
                + this.description + " (Prioridade: " + this.priority + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

}
