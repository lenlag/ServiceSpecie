package formation.afpa.fr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceAnimal {
	
	@Autowired
	AnimalRepository animRep;
	
	public ServiceAnimal() {
		
	}
	
	public List<Animal> findAll() {
		return (List<Animal>) animRep.findAll();
	}
	
	public List<Animal> findByName(String name) {
		return animRep.findByName(name);
	}
	
	public List<Animal> findDictinctBycoatColor (String coatColor) {
		return animRep.findDictinctBycoatColor(coatColor);
	}
	
	public List<Animal> findBySexOrName(String sex,String name) {
		return animRep.findBySexOrName(sex, name);
	}
	
	public Animal findById(Long id) {
		return animRep.findById(id).get();
	}
	
	public void save(Animal an) {
		animRep.save(an);
	}
	
	public void saveAll(List<Animal> list) {
		animRep.saveAll(list);
	}
	
	public void deleteById(long id) {
		animRep.deleteById(id);
	}
	
	public void delete(Animal an) {
		animRep.delete(an);
	}
	
	public void deleteAll(List<Animal> list) {
		animRep.deleteAll(list);
	}
	
	
	
}
