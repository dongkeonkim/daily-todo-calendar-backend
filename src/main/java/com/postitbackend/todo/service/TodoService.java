package com.postitbackend.todo.service;

import com.postitbackend.todo.dto.TodoDto;
import com.postitbackend.todo.entity.Todo;
import com.postitbackend.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    @Transactional(readOnly = true)
    public List<TodoDto> selectTodos(Long memberId) {
        return todoRepository.findAllByMemberId(memberId).stream()
                .map(Todo::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createTodo(TodoDto todoDto, Long memberId) {
        todoDto.setMemberId(memberId);
        todoDto.setCreatedAt(LocalDateTime.now());
        todoDto.setUpdatedAt(LocalDateTime.now());

        todoRepository.save(todoDto.toEntity());
        log.info("Creating a new todo");
    }

    @Transactional
    public void updateTodo(TodoDto todoDto, Long memberId) {
        Todo todo = todoRepository.findByTodoIdAndMemberId(todoDto.getTodoId(), memberId)
                .orElseThrow(() -> new NoSuchElementException("No such todo exists"));

        todo.update(todoDto);

        log.info("Updating a todo");
    }

    @Transactional
    public void deleteTodo(TodoDto todoDto, Long memberId) {
        Todo todo = todoRepository.findByTodoIdAndMemberId(todoDto.getTodoId(), memberId)
                .orElseThrow(() -> new NoSuchElementException("No such todo exists"));

        todoRepository.delete(todo);
        log.info("Deleting a todo");
    }

}
