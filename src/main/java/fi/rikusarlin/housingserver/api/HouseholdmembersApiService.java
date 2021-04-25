package fi.rikusarlin.housingserver.api;

import fi.rikusarlin.housingserver.api.*;
import fi.rikusarlin.housingserver.model.*;


import fi.rikusarlin.housingserver.model.ErrorMessage;
import fi.rikusarlin.housingserver.model.HouseholdMember;

import java.util.List;
import fi.rikusarlin.housingserver.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public interface HouseholdmembersApiService {
      Response addHouseholdMember(Integer caseId,HouseholdMember householdMember,SecurityContext securityContext)
      throws NotFoundException;
      Response checkHouseholdMemberById(Integer caseId,Integer id,SecurityContext securityContext)
      throws NotFoundException;
      Response deleteHouseholdMember(Integer caseId,Integer id,SecurityContext securityContext)
      throws NotFoundException;
      Response fetchHouseholdMemberById(Integer caseId,Integer id,SecurityContext securityContext)
      throws NotFoundException;
      Response fetchHouseholdMembers(Integer caseId,SecurityContext securityContext)
      throws NotFoundException;
      Response updateHouseholdMember(Integer caseId,Integer id,HouseholdMember householdMember,SecurityContext securityContext)
      throws NotFoundException;
}
