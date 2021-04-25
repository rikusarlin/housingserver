package fi.rikusarlin.housingserver.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import fi.rikusarlin.housingserver.model.Expense;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public interface ExpensesApiService {
      Response addExpense(Integer caseId,Expense expense,SecurityContext securityContext)
      throws NotFoundException;
      Response checkExpenseById(Integer caseId,Integer id,SecurityContext securityContext)
      throws NotFoundException;
      Response deleteExpense(Integer caseId,Integer id,SecurityContext securityContext)
      throws NotFoundException;
      Response fetchExpenseById(Integer caseId,Integer id,SecurityContext securityContext)
      throws NotFoundException;
      Response fetchExpenses(Integer caseId,SecurityContext securityContext)
      throws NotFoundException;
      Response updateExpense(Integer caseId,Integer id,Expense expense,SecurityContext securityContext)
      throws NotFoundException;
}
