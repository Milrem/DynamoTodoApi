package br.com.letscode.dynamo.controller;

import br.com.letscode.dynamo.model.Tarefa;
import br.com.letscode.dynamo.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/todo")
@RequiredArgsConstructor
public class TarefaController {
    private final TarefaRepository repository;

    @PostMapping()
    public ResponseEntity<String> addTarefa(@RequestBody(required = true) Tarefa entity) {
        Tarefa saved = repository.save(entity);
        return new ResponseEntity<String>(saved.getId().toString(), HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<String> updateTarefa(@RequestBody(required = true) Tarefa entity) {
        Tarefa saved = repository.save(entity);
        return new ResponseEntity<String>(saved.getId().toString(), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> delTarefa(@PathVariable(name = "id", required = true) String id) {
        Optional<Tarefa> Tarefa = repository.findById(UUID.fromString(id));
        if (!Tarefa.isPresent())
            return new ResponseEntity<String>(String.format("Tarefa <%s> not found", id), HttpStatus.NOT_FOUND);

        repository.delete(Tarefa.get());
        return new ResponseEntity<String>(String.format("Tarefa <%s> not found", id), HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Tarefa> findTarefa(@PathVariable(name = "id", required = true) String id) {
        Optional<Tarefa> Tarefa = repository.findById(UUID.fromString(id));
        if (!Tarefa.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(Tarefa.get(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Tarefa>> getAllTarefas() {
        List<Tarefa> TarefaList = new ArrayList<>();
        repository.findAll().forEach(TarefaList::add);
        return new ResponseEntity<>(TarefaList, HttpStatus.OK);
    }
}
