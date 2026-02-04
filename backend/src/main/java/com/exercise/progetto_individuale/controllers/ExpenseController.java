package com.exercise.progetto_individuale.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.progetto_individuale.dtos.input_dtos.InputExpenseDto;
import com.exercise.progetto_individuale.services.ExpenseService;

@RestController
@RequestMapping("api/groups/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService eServ;

    @PostMapping("{groupId}/add")
    public void addExpense(@PathVariable UUID groupId, @RequestBody InputExpenseDto dto)
    {
        eServ.addExpense(groupId, dto);
    }
}
