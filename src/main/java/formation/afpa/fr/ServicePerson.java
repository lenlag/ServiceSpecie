package formation.afpa.fr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicePerson {
	
	@Autowired
	PersonRepository persRep;
	
	public ServicePerson() {
		
	}
	
	public List<Person> findAll() {
		return (List<Person>) persRep.findAll();
	}
	
	public List<Person> findByLastName(String lastName) {
		return persRep.findByLastName(lastName);
	}
	
	public List<Person> findDictinctByLastName (String lastName) {
		return persRep.findDictinctByLastName(lastName);
	}
	
	public List<Person> findByFirstNameOrLastName(String firstName,String lastName) {
		return persRep.findByFirstNameOrLastName(firstName, lastName);
	}
	
	public Person findById(Long id) {
		return persRep.findById(id).get();
	}
	
	public void save(Person p) {
		persRep.save(p);
	}
	
	public void saveAll(List<Person> list) {
		persRep.saveAll(list);
	}
	
	public void deleteById(long id) {
		persRep.deleteById(id);
	}
	
	public void delete(Person an) {
		persRep.delete(an);
	}
	
	public void deleteAll(List<Person> list) {
		persRep.deleteAll(list);
	}
	
	
	
	

}
