package fi.rikusarlin.housingserver.api;

import fi.rikusarlin.housingserver.api.*;
import fi.rikusarlin.housingserver.model.*;


import fi.rikusarlin.housingserver.model.ErrorMessage;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.model.NewHousingBenefitCase;

import java.util.List;
import fi.rikusarlin.housingserver.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public interface CasesApiService {
      Response addHousingBenefitCase(NewHousingBenefitCase newHousingBenefitCase,SecurityContext securityContext)
      throws NotFoundException;
      Response checkHousingBenefitCase(Integer caseId,SecurityContext securityContext)
      throws NotFoundException;
      Response deleteHousingBenefitCase(Integer caseId,SecurityContext securityContext)
      throws NotFoundException;
      Response fetchHousingBenefitCaseById(Integer caseId,SecurityContext securityContext)
      throws NotFoundException;
      Response fetchHousingBenefitCases(SecurityContext securityContext)
      throws NotFoundException;
      Response fetchHousingBenefitCasesByPersonNumber(String personNumber,SecurityContext securityContext)
      throws NotFoundException;
      Response updateHousingBenefitCase(Integer caseId,HousingBenefitCase housingBenefitCase,SecurityContext securityContext)
      throws NotFoundException;
}
