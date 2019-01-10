package formation.afpa.fr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceSpecie {

	@Autowired
	SpecieRepository srepo;
	
	
	public ServiceSpecie() {
		
	}
	
	
	public List<Specie> findAll() {
		return (List<Specie>) srepo.findAll();
	}
	
	public List<Specie> findByBeginLatinName(String name) {
		return srepo.findByBeginLatinName(name);
	}
	
	public List<Specie> findByCommonName(String commonName) {
		return srepo.findByCommonName(commonName);
	}
	
	public Specie findById(Long id) {
		return srepo.findById(id).get();
	}
	
	public List<Specie> findByLatinName(String latinName) {
		return srepo.findByLatinName(latinName);
	}
	
	public void save(Specie sp) {
		srepo.save(sp);
	}
	
	public void saveAll(List<Specie>list) {
		srepo.saveAll(list);
	}
	
	public void deleteById(long id) {
		srepo.deleteById(id);
	}
	
	public void delete(Specie sp) {
		srepo.delete(sp);
	}
	
	public void deleteAll(List<Specie> list) {
		srepo.deleteAll(list);
	}
	
}
