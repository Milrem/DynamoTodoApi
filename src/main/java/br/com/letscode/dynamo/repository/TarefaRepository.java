package br.com.letscode.dynamo.repository;

import br.com.letscode.dynamo.model.Tarefa;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@EnableScan
public interface TarefaRepository extends CrudRepository<Tarefa, UUID> {
}
