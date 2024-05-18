package com.postitbackend.postIt.todo.controller;

import com.postitbackend.config.security.custom.CustomUser;
import com.postitbackend.postIt.todo.dto.TodoDto;
import com.postitbackend.postIt.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/all")
    public ResponseEntity<?> selectTodos(@AuthenticationPrincipal CustomUser customUser) {
        Map<String, List<TodoDto>> result = new HashMap<>();

        List<TodoDto> todoDtoList = todoService.selectTodos(customUser.getMemberDTO().getId());
        result.put("todos", todoDtoList);

        if (todoDtoList.isEmpty()) {
            log.error("No todos found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        log.info("Selecting all todos");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTodo(@RequestBody TodoDto todoDto, @AuthenticationPrincipal CustomUser customUser) {
        try {
            todoService.createTodo(todoDto, customUser.getMemberDTO().getId());
        } catch (DataAccessException e) {
            log.error("Error creating a new todo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        log.info("Creating a new todo");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTodo(@RequestBody TodoDto todoDto, @AuthenticationPrincipal CustomUser customUser) {
        try {
            todoService.updateTodo(todoDto, customUser.getMemberDTO().getId());
        } catch (DataAccessException e) {
            log.error("Error updating a todo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        log.info("Updating a todo");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDto todoDto, @AuthenticationPrincipal CustomUser customUser) {
        try {
            todoService.deleteTodo(todoDto, customUser.getMemberDTO().getId());
        } catch (DataAccessException e) {
            log.error("Error deleting a todo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        log.info("Deleting a todo");
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
