package fi.rikusarlin.housingserver.api;

import fi.rikusarlin.housingserver.api.*;
import fi.rikusarlin.housingserver.model.*;


import fi.rikusarlin.housingserver.model.ErrorMessage;
import fi.rikusarlin.housingserver.model.Income;

import java.util.List;
import fi.rikusarlin.housingserver.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public interface IncomesApiService {
      Response addIncome(Integer caseId,Income income,SecurityContext securityContext)
      throws NotFoundException;
      Response checkIncomeById(Integer caseId,Integer id,SecurityContext securityContext)
      throws NotFoundException;
      Response deleteIncome(Integer caseId,Integer id,SecurityContext securityContext)
      throws NotFoundException;
      Response fetchIncomeById(Integer caseId,Integer id,SecurityContext securityContext)
      throws NotFoundException;
      Response fetchIncomes(Integer caseId,SecurityContext securityContext)
      throws NotFoundException;
      Response updateIncome(Integer caseId,Integer id,Income income,SecurityContext securityContext)
      throws NotFoundException;
}
