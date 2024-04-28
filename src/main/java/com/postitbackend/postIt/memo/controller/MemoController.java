package com.postitbackend.postIt.memo.controller;

import com.postitbackend.postIt.memo.dto.MemoDTO;
import com.postitbackend.postIt.memo.service.MemoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController {

    private final MemoService memoService;

    @GetMapping("/findAll")
    public ResponseEntity<List<MemoDTO>> findAll(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("memberEmail");

        List<MemoDTO> memoDTOs = memoService.findAllByEmail(email);

        return ResponseEntity.ok().body(memoDTOs);
    }

    @GetMapping("/findFixedMemos")
    public ResponseEntity<Void> findFixedMemos(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("memberEmail");

        memoService.findFixedMemos(email);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    public ResponseEntity<Void> findOne(@RequestBody MemoDTO memoDTO) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody MemoDTO memoDTO, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String memberId = (String) session.getAttribute("memberId");

        memoDTO.setMemberId(memberId);

        int result = memoService.save(memoDTO);

        if (result != 1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/update")
    public ResponseEntity<Void> update(@RequestBody MemoDTO memoDTO) {
        int result = memoService.save(memoDTO);

        if (result != 1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/delete")
    public ResponseEntity<Void> delete(@RequestBody MemoDTO memoDTO) {
        memoDTO.setDelYn("Y");

        int result = memoService.save(memoDTO);

        if (result != 1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }
}
