package fi.rikusarlin.housingserver.api;

import fi.rikusarlin.housingserver.api.*;
import fi.rikusarlin.housingserver.model.*;


import fi.rikusarlin.housingserver.model.ErrorMessage;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;

import java.util.List;
import fi.rikusarlin.housingserver.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public interface ApplicationsApiService {
      Response addApplication(Integer caseId,HousingBenefitApplication housingBenefitApplication,SecurityContext securityContext)
      throws NotFoundException;
      Response checkApplication(Integer caseId,SecurityContext securityContext)
      throws NotFoundException;
      Response deleteApplication(Integer caseId,SecurityContext securityContext)
      throws NotFoundException;
      Response fetchApplication(Integer caseId,SecurityContext securityContext)
      throws NotFoundException;
      Response updateApplication(Integer caseId,HousingBenefitApplication housingBenefitApplication,SecurityContext securityContext)
      throws NotFoundException;
}
