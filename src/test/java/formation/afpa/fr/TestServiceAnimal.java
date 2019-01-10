package formation.afpa.fr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class) // On indique qu’on utilise Mockito
public class TestServiceAnimal {

	@Mock // Ici on indique qu’on a un mock qui simule le repository d'Animal
	private AnimalRepository animRepMock;

	@Mock
	private SpecieRepository spRepMock;

	@InjectMocks // Ici, on indique que le mock est injecté/utilisé par le service à tester
	private ServiceAnimal animalService;

	// on va utiliser le mock comme si c’était un repository.

	List<Animal> animList = new ArrayList<>();
	List<Specie> specieList = new ArrayList<>();
	int speciesSize;
	int animalsSize;

	String animalName = "Simba";
	String coatColor = "black";
	String sex = "f";
	long id = 1l;
	String checkName = "Messi";
	

	@Before
	public void setUp() {
		Specie specie1 = new Specie(0l, "Chat", "Catus");
		Specie specie2 = new Specie(1l, "Chien", "Canis");

		specieList.add(specie1);
		specieList.add(specie2);
		speciesSize = specieList.size();

		animList.add(new Animal(0l, specie1, "Lucky", "gris", "f"));
		animList.add(new Animal(1l, specie2, "Messi", "black", "m"));
		animList.add(new Animal(2l, specie1, "Simba", "red", "m"));
		animList.add(new Animal(3l, specie2, "Jordy", "black", "m"));
		animList.add(new Animal(4l, specie2, "Maugli", "white", "m"));
		animalsSize = animList.size();

	}

	@Test
	public void findAll() {
		when(animRepMock.findAll()).thenReturn(animList);
		assertEquals(animalsSize, animalService.findAll().size());
	}

	@Test
	public void findByName() {
		when(animRepMock.findByName(animalName)).thenReturn(getAnimalByName(animalName));
		assertEquals(1, animalService.findByName(animalName).size());
		assertTrue(animalService.findByName(animalName).get(0).getName().contains(animalName));
	}

	private List<Animal> getAnimalByName(String s) {
		List<Animal> listToReturn = new ArrayList<>();

		for (Animal animal : animList) {
			if (animal.getName().equals(animalName)) {
				listToReturn.add(animal);
			}
		}
		return listToReturn;
	}

	@Test
	public void findDictinctBycoatColor() {
		when(animRepMock.findDictinctBycoatColor(coatColor)).thenReturn(findDictinctAnimalBycoatColor(coatColor));
		assertEquals(animalService.findDictinctBycoatColor(coatColor).size(), 2);
		assertEquals(animalService.findDictinctBycoatColor(coatColor).get(1).getCoatColor(), coatColor);
		assertNotEquals(animalService.findDictinctBycoatColor(coatColor).get(1).getName(), animalService.findDictinctBycoatColor(coatColor).get(0).getName());
	
	}

	private List<Animal> findDictinctAnimalBycoatColor(String s) {

		List<Animal> list = new ArrayList<>();
		for (Animal animal : animList) {
			if (animal.getCoatColor().equals(s)) {
				list.add(animal);
			}
		}
		return list;

	}

	@Test
	public void findBySexOrName() {
		when(animRepMock.findBySexOrName(sex, animalName)).thenReturn(findAnimalBySexOrName(sex, animalName));
		assertEquals(animalService.findBySexOrName(sex, animalName).size(),2);
		assertTrue(animalService.findBySexOrName(sex, animalName).get(1).getName().equals(animalName));
	}
	
	private List<Animal> findAnimalBySexOrName (String s, String y) {
		
		List<Animal> myList = new ArrayList<>();
		for (Animal animal : animList) {
			if((animal.getSex().equals(s)) ||(animal.getName().equals(y))){
				myList.add(animal);
			}
		}
		return myList;
		
	}

	@Test
	public void findById() {
		when(animRepMock.findById(id)).thenReturn(Optional.of(findAnimalById(id)));
		assertNotNull(animalService.findById(id));
		assertTrue(animalService.findById(id).getName().equals(checkName));
		
	}

	private Animal findAnimalById(long id) {
		
		Animal an = null;
		
		for (Animal animal : animList) {
			if(animal.getId() == id) {
				an = animal;
			}
		}
		return an;
		
	}
	
	@Test
	public void create() {
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					
					Animal an = (Animal) os[0];
					animList.add(an);
					System.out.println(animList.size());

				}
				return null;
			}
		}).when(animRepMock).save(Mockito.any(Animal.class));

		Animal newAnimal = new Animal(5l, specieList.get(0), "Marusya", "multi", "f");
		animalService.save(newAnimal);
		
		assertEquals(animalsSize + 1, animList.size());
		assertEquals(5L, animList.get(animalsSize).getId().longValue()); //on prend le dernier element de la liste, sa position = animalsSize
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createAll() {
		doAnswer(new Answer<Void>() {
			
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					List<Animal> animalsToAdd = (List<Animal>) os[0];

					for (Animal animal : animalsToAdd) {
						long newId = (long) animList.size(); //nous avons 2 éléments dans la liste, au 1er tour de la boucle, newId = 5l, 2ème tour = 6l
						animal.setId(newId);				
						animList.add(animal);
					}
				}
				return null;
			}
		}).when(animRepMock).saveAll(Mockito.any(Iterable.class));
		
		List<Animal> animalsToAdd = new ArrayList<>();
		Animal newAnimal = new Animal(5l, specieList.get(0), "Marusya", "multi", "f");
		Animal newAnima2 = new Animal(6l, specieList.get(1), "Mishka", "brown", "m");

		animalsToAdd.add(newAnimal);
		animalsToAdd.add(newAnima2);

		animalService.saveAll(animalsToAdd);

		int newSize = animalsSize + animalsToAdd.size();
		assertEquals(newSize, animList.size());
		Animal animal = animList.get(newSize - 1);
		assertEquals(6l, animal.getId().longValue());
	}
	
	@Test
	public void update() {

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					Animal an = (Animal) os[0];
					Integer indexOf = animList.indexOf(an);

					if (indexOf != null) {
						Animal oldAnimal = animList.get(indexOf); // on extrait l'acien objet
						oldAnimal.setCoatColor(an.getCoatColor());	//on set des valeurs de lui-même
						oldAnimal.setName(an.getName());
						oldAnimal.setSex(an.getSex());
						oldAnimal.setSpecie(an.getSpecie());
					}
				}
				return null;
			}
		}).when(animRepMock).save(Mockito.any(Animal.class));

		Animal an = animList.get(0); //on extrait le nouvel objet
		an.setName("AA"); // on modif une valeur d'un nouvel objet
		animalService.save(an);

		assertEquals(animalsSize, animList.size());
		assertEquals(0L, animList.get(0).getId().longValue());
		assertEquals("AA", animList.get(0).getName());
	}


	@Test
	public void deleteById() {
		
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					Long id = (Long) os[0];

					for (Animal animal : animList) {
						if(animal.getId() == id) {
							animList.remove(animal);
							break;
						}
					}
				}
				return null;
			}
		}).when(animRepMock).deleteById(Mockito.any(Long.class));

		animalService.deleteById(0l);

		assertEquals(animalsSize- 1, animList.size());
	
	}

	@Test
	public void delete() {

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					Animal an = (Animal) os[0];

					if (animList.contains(an)) {
						animList.remove(an);
					}
				}
				return null;
			}
		}).when(animRepMock).delete(Mockito.any(Animal.class));

		Animal an = animList.get(0);
		animalService.delete(an);

		assertEquals(animalsSize- 1, animList.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deleteAll() {
		
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					@SuppressWarnings("unchecked")
					List<Animal> animals = (List<Animal>) os[0];

					for (Animal a : animals) {
						if (animList.contains(a)) {
							animList.remove(a);
						}
					}
				}
				return null;
			}
		}).when(animRepMock).deleteAll(Mockito.any(Iterable.class));

		Animal an1 = animList.get(0);
		Animal an2 = animList.get(1);

		List<Animal> animalsToDelete = new ArrayList<>();

		animalsToDelete.add(an1);
		animalsToDelete.add(an2);

		animalService.deleteAll(animalsToDelete);

		assertEquals(animalsSize -2, animList.size());

	}
}
