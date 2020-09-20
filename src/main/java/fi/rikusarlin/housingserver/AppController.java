package fi.rikusarlin.housingserver;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.data.HouseholdMember;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;

@RestController
public class AppController {
    @Autowired
    HouseholdMemberRepository householdMemberRepo;

    @RequestMapping(value="/householdmembers", method = RequestMethod.GET, headers = "Accept=application/json")
    public @ResponseBody Iterable<HouseholdMember> findHouseholdMembers() {
        return householdMemberRepo.findAll();
    }
     
	@RequestMapping(value = "/householdmember/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Optional<HouseholdMember> findHouseholdMemberById(@PathVariable int id) {
		return householdMemberRepo.findById(id);
	}
 
	@RequestMapping(
			value = "/householdmember", 
			method = RequestMethod.POST, 
			headers = "Accept=application/json")
	public HouseholdMember addHouseholdMember(@RequestBody HouseholdMember householdMember) {
		return householdMemberRepo.save(householdMember);
	}

}
