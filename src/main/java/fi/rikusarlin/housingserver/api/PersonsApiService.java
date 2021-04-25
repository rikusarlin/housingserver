package fi.rikusarlin.housingserver.api;

import fi.rikusarlin.housingserver.api.*;
import fi.rikusarlin.housingserver.model.*;


import fi.rikusarlin.housingserver.model.ErrorMessage;
import fi.rikusarlin.housingserver.model.Person;

import java.util.List;
import fi.rikusarlin.housingserver.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public interface PersonsApiService {
      Response addPerson(Person person,SecurityContext securityContext)
      throws NotFoundException;
      Response checkPersonById(Integer id,SecurityContext securityContext)
      throws NotFoundException;
      Response deletePerson(Integer id,SecurityContext securityContext)
      throws NotFoundException;
      Response fetchPersonById(Integer id,SecurityContext securityContext)
      throws NotFoundException;
      Response fetchPersons(SecurityContext securityContext)
      throws NotFoundException;
      Response updatePerson(Integer id,Person person,SecurityContext securityContext)
      throws NotFoundException;
}
