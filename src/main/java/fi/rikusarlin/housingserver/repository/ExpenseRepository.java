package fi.rikusarlin.housingserver.repository;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.Expense;

public interface ExpenseRepository extends CrudRepository<Expense, Integer> {
}
