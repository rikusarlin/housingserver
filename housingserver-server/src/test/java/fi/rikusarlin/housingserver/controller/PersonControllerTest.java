package fi.rikusarlin.housingserver.controller;

import static org.mockito.Mockito.clearInvocations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.google.gson.Gson;

import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.topdown.controller.PersonsControllerImpl;

@ExtendWith(MockitoExtension.class)
@WebFluxTest(PersonsControllerImpl.class)
class PersonControllerTest {

	@Mock
    PersonRepository mockPersonRepo;

    @InjectMocks
    PersonsControllerImpl personService;
    
    Gson gson;
    
    @Autowired
    WebTestClient webTestClient;
    
    @BeforeEach
    public void setUp() {
    	gson = new Gson();
    }
    
    
    @AfterEach
    public void tearDown() {
        clearInvocations(mockPersonRepo);
    }

    /*
    @Test
    public void testAddNewPerson(){
    	Person person1 = PersonData.getPerson1();
        Mono<PersonEntity> person1outEntity = Mono.just(MappingUtil.modelMapper.map(person1, PersonEntity.class));
        Person person1out = MappingUtil.modelMapper.map(person1outEntity, Person.class);

        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/api/v2/housing/person").contentType(MediaType.APPLICATION_JSON)
                        .body(gson.toJson(PersonData.getPerson1())));

    	when(mockPersonRepo.save(any())).thenReturn(person1outEntity);
    	when(mockPersonRepo.findByPersonNumber(Mono.just(person1.getPersonNumber()))).thenReturn(Mono.empty());
    	
        Mono<ResponseEntity<Person>> result = personService.addPerson(Mono.just(person1), exchange);

        Assertions.assertTrue(result.equals(Mono.just(ResponseEntity.ok(person1out))));
        verify(mockPersonRepo).findByPersonNumber(Mono.just(person1.getPersonNumber()));
        verify(mockPersonRepo).save(any(PersonEntity.class));
    }
    */
    
    /*
    @Test
    public void testTryAddingAlreadyExistingPerson(){
    	Person person1 = PersonData.getPerson1();
        PersonEntity person1outEntity = MappingUtil.modelMapper.map(person1, PersonEntity.class);

    	when(mockPersonRepo.findByPersonNumber(person1.getPersonNumber())).thenReturn(Optional.of(person1outEntity));
    	
    	Exception e = Assertions.assertThrows(DuplicateNotAllowedException.class, () -> {
            personService.addPerson(person1);
        });
    	Assertions.assertTrue(e.getMessage().equals("personNumber "+person1.getPersonNumber()));
        verify(mockPersonRepo).findByPersonNumber(person1.getPersonNumber());        
    }
    
    @Test
    public void testTryAddingPersonWithBadPersonNumber(){
    	Person person2 = PersonData.getPerson2();
    	person2.setPersonNumber("010170-901L");

    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.addPerson(person2);
        });
        Assertions.assertTrue(e.getMessage().equals("personNumber: invalid person number '"+person2.getPersonNumber()+"'"));
        verify(mockPersonRepo, times(0)).findByPersonNumber(person2.getPersonNumber());
        verify(mockPersonRepo, times(0)).save(any(PersonEntity.class));

    }
    */

    /*
    @Test
    public void testFetchPersonById_found(){
    	Person person1 = PersonData.getPerson1();
    	ResponseEntity<Person> re1 = ResponseEntity.ok().body(person1);
        Mono<ResponseEntity<Person>> person1outEntity = Mono.just(re1);
        
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v2/housing/person/1"));

        when(personService.fetchPersonById(1, exchange)).thenReturn(person1outEntity);
    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Mono.just(person1));

    	webTestClient.get()
        	.uri("/api/v2/housing/person/1")
        	.accept(MediaType.APPLICATION_JSON)
        	.exchange()
        	.expectStatus().isOk()
        	.expectBody(Person.class)
        	.value(p -> p.getFirstName(), equalTo(person1.getFirstName()));    	
    }

    @Test
    public void testFetchPersonById_notFound(){
    	Person person1 = PersonData.getPerson1();
    	ResponseEntity<Person> re1 = ResponseEntity.ok().body(person1);
        Mono<ResponseEntity<Person>> person1outEntity = Mono.just(re1);
        
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v2/housing/person/1"));

        when(personService.fetchPersonById(1, exchange)).thenReturn(person1outEntity);

    	webTestClient.get()
        	.uri("/api/v2/housing/person/1")
        	.accept(MediaType.APPLICATION_JSON)
        	.exchange()
        	.expectStatus().isNotFound();
    }
    */
    

    /*
    @Test
    public void testCheckPerson_foundButFailsValidation(){
    	Person person1 = PersonData.getPerson1();
    	person1.setEmail("username@yahoo..com");
        PersonEntity person1outEntity = MappingUtil.modelMapper.map(person1, PersonEntity.class);
    	
    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.of(person1outEntity));

    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.checkPersonById(person1.getId());
        });
        Assertions.assertTrue(e.getMessage().equals("email: invalid emailAddress 'username@yahoo..com'"));
        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testCheckById_foundAndIsValid(){
    	Person person1 = PersonData.getPerson1();
        PersonEntity person1outEntity = MappingUtil.modelMapper.map(person1, PersonEntity.class);
        Person person1out = MappingUtil.modelMapper.map(person1outEntity, Person.class);

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.of(person1outEntity));
    	
        ResponseEntity<Person> result = personService.checkPersonById(person1.getId());

        Assertions.assertTrue(result.equals(ResponseEntity.ok(person1out)));
        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testCheckById_notFound(){
    	Person person1 = PersonData.getPerson1();

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.checkPersonById(person1.getId());
        });

        Assertions.assertTrue(e.getMessage().equals("Person not found : "+person1.getId()));
        verify(mockPersonRepo).findById(person1.getId());
    }
    
    @Test
    public void testUpdatePerson_ok(){
    	Person person1 = PersonData.getPerson1();
        PersonEntity person1Entity = MappingUtil.modelMapper.map(person1, PersonEntity.class);
        Person person1out = MappingUtil.modelMapper.map(person1Entity, Person.class);

    	when(mockPersonRepo.save(any(PersonEntity.class))).thenReturn(person1Entity);
    	
        ResponseEntity<Person> result = personService.updatePerson(person1.getId(), person1);

        Assertions.assertTrue(result.equals(ResponseEntity.ok(person1out)));
        verify(mockPersonRepo).save(any(PersonEntity.class));
    }

    @Test
    public void testUpdatePerson_failsValidation(){
    	Person person1 = PersonData.getPerson1();
    	person1.setEmail("username@yahoo..com");
    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.updatePerson(person1.getId(), person1);
        });
        Assertions.assertTrue(e.getMessage().equals("email: invalid emailAddress 'username@yahoo..com'"));
    }

    @Test
    public void testDeletePerson_found(){
    	Person person1 = PersonData.getPerson1();
        PersonEntity person1outEntity = MappingUtil.modelMapper.map(person1, PersonEntity.class);
 
    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.of(person1outEntity));
    	
        personService.deletePerson(person1.getId());

        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testDeletePerson_notFound(){
    	Person person1 = PersonData.getPerson1();

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.deletePerson(person1.getId());
        });

        Assertions.assertTrue(e.getMessage().equals("Person not found : "+person1.getId()));
        verify(mockPersonRepo).findById(person1.getId());
    }
	*/

}