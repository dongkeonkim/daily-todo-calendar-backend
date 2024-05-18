package com.postitbackend.postIt.todo.repository;

import com.postitbackend.postIt.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

    Optional<Todo> findByTodoIdAndMemberId(Long todoId, Long memberId);

    List<Todo> findAllByMemberId(Long memberId);

}
