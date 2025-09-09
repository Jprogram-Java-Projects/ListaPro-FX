package br.com.jeff.listaprofx.dao;

import br.com.jeff.listaprofx.model.Task;

import java.util.List;
import java.util.Optional;


public interface TaskDAO {

	Task save(Task t);
	boolean delete(int id);
	Optional<Task> update(Task t);
	List<Task> listAll();
	
}
