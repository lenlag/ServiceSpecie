package formation.afpa.fr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
public class TestServicePerson {
	
	@Mock // Ici on indique qu’on a un mock qui simule le repository de Person
	private PersonRepository persRepMock;
	
	@Mock
	private AnimalRepository animRepMock;
	
	@Mock
	private SpecieRepository spRepMock;
	

	@InjectMocks // Ici, on indique que le mock est injecté/utilisé par le service à tester
	private ServicePerson personService;

	// on va utiliser le mock comme si c’était un repository.
	
	
	List<Specie> species = new ArrayList<>();
	List<Animal> animals = new ArrayList<>();
	List<Person> persons = new ArrayList<>();
	int speciesSize;
	int animalsSize;
	int personsSize;
	
	String firstName = "Natalia";
	String lastName = "Bujaud";
	
	

	@Before
	public void setUp() {

		Specie specie1 = new Specie(0l, "Chat", "Catus");
		Specie specie2 = new Specie(1l, "Chien", "Canis");
		species.add(specie1);
		species.add(specie2);
		speciesSize = species.size();

		Animal animal1 = new Animal(0l, specie1, "Lucky", "gris", "f");
		Animal animal2 = new Animal(1l, specie2, "Messi", "black", "m");
		Animal animal3 = new Animal(2l, specie1, "Simba", "red", "m");
		Animal animal4 = new Animal(3l, specie2, "Jordy", "black", "m");
		Animal animal5 = new Animal(4l, specie2, "Maugli", "white", "m");
		
		animals.add(animal1);
		animals.add(animal2);
		animals.add(animal3);
		animals.add(animal4);
		animals.add(animal5);
		animalsSize = animals.size();
		
		List<Animal> animalsP1 = new ArrayList<>();
		animalsP1.add(animal1);
		Person person1 = new Person(0l, "Natalia", "Mathieu", 29, animalsP1);
		
		List<Animal> animalsP2 = new ArrayList<>();
		animalsP2.add(animal2);
		Person person2 = new Person(1l, "Olivier", "Bujaud", 27, animalsP2);
		
		List<Animal> animalsP3 = new ArrayList<>();
		animalsP3.add(animal3);
		animalsP3.add(animal5);
		Person person3 = new Person(2l,"Christine", "Bujaud", 45, animalsP3);
		
		List<Animal> animalsP4 = new ArrayList<>();
		animalsP4.add(animal4);
		animalsP4.add(animal5);
		Person person4 = new Person(3l, "Seb", "Grana", 27, animalsP4);

		persons.add(person1);
		persons.add(person2);
		persons.add(person3);
		persons.add(person4);
		personsSize= persons.size();
		
	}

	

	@Test
	public void findAll() {
		when(persRepMock.findAll()).thenReturn(persons);
		assertEquals(personsSize, personService.findAll().size());
	}

	@Test
	public void findById() {
		when(persRepMock.findById(0l)).thenReturn(Optional.of(getPersonById(0l)));
		assertEquals(firstName, personService.findById(0l).getFirstName());
	}
	private Person getPersonById(Long id) {
		Person personToReturn = null;

		for (Person person : persons) {
			if (person.getId().equals(id)) {
				personToReturn  = person;
			}
		}

		return personToReturn;
	}

	
	@Test
	public void findByLastName() {
		when(persRepMock.findByLastName(lastName)).thenReturn(getPersonByLastName(lastName));
		assertEquals(2, personService.findByLastName(lastName).size());
		assertTrue(personService.findByLastName(lastName).get(1).getLastName().equals(lastName));
	}

	private List<Person> getPersonByLastName(String s) {
		List<Person> listToReturn = new ArrayList<>();

		for (Person person : persons) {
			if(person.getLastName().equals(s)) {
				listToReturn.add(person);
		}
		}
		return listToReturn;
	}
	
	
	@Test
	public void findDictinctByLastName() {
		when(persRepMock.findByLastName(lastName)).thenReturn(findDictinctPersonByLastName(lastName));
		assertEquals(2, personService.findByLastName(lastName).size());
		assertNotEquals(personService.findByLastName(lastName).get(0).getFirstName(), personService.findByLastName(lastName).get(1).getFirstName());
	}

	private List<Person> findDictinctPersonByLastName(String s) {
		List<Person> listToReturn = new ArrayList<>();

		for (Person person : persons) {
			if(person.getLastName().equals(s)) {
				listToReturn.add(person);
		}
		}
		return listToReturn;
	}
	
	@Test
	public void findByFirstNameOrLastName() {
		when(persRepMock.findByFirstNameOrLastName(firstName, lastName)).thenReturn(findPersonByFirstNameOrLastName(firstName, lastName));
		assertEquals(personService.findByFirstNameOrLastName(firstName, lastName).size(), 3);
	
	}
	
	private List<Person> findPersonByFirstNameOrLastName (String s, String y) {
		
		List<Person> myList = new ArrayList<>();
		for (Person person : persons) {
			if((person.getFirstName().equals(s)) || (person.getLastName().equals(y))) {
				myList.add(person);
			}
		}
		return myList;
	}



	@Test
	public void create() {
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					
					Person per = (Person) os[0];
					persons.add(per);

				}
				return null;
			}
		}).when(persRepMock).save(Mockito.any(Person.class));

		List<Animal> animalsNewPerson = new ArrayList<>();
		animalsNewPerson.add(animals.get(0));
		Person newPerson = new Person(4l, "Alain", "Delphonse", 55, animalsNewPerson);
		personService.save(newPerson);
		
		assertEquals(personsSize + 1, persons.size());
		assertEquals(4L, persons.get(personsSize).getId().longValue());
	}


	@SuppressWarnings("unchecked")
	@Test
	public void createAll() {
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					List<Person> personToAdd = (List<Person>) os[0];

					for (Person person : personToAdd) {
						long newId = (long) persons.size();
						person.setId(newId);
						persons.add(person);
					}
				}
				return null;
			}
		}).when(persRepMock).saveAll(Mockito.any(Iterable.class));

		List<Animal> animalsNewPerson1 = new ArrayList<>();
		animalsNewPerson1.add(animals.get(0));
		Person newPerson1 = new Person(4l, "Martin", "Fourcade", 55, animalsNewPerson1);
		
		List<Animal> animalsNewPerson2 = new ArrayList<>();
		animalsNewPerson2.add(animals.get(0));
		Person newPerson2 = new Person(5l, "Megan", "Markle", 37, animalsNewPerson2);
		
		List<Person> personToAdd = new ArrayList<>();
		personToAdd.add(newPerson1);
		personToAdd.add(newPerson2);
		
		personService.saveAll(personToAdd);

		int newSize = personsSize + personToAdd.size();
		assertEquals(newSize, persons.size());
		Person person = persons.get(newSize - 1);
		assertEquals(5l, person.getId().longValue());                        
	}

	@Test
	public void update() {

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					Person per = (Person) os[0];
					Integer indexOf = persons.indexOf(per);

					if (indexOf != null) {
						Person oldPerson = persons.get(indexOf);
						oldPerson.setAge(per.getAge());
						oldPerson.setFirstName(per.getFirstName());
						oldPerson.setFirstName(per.getFirstName());
						oldPerson.setLastName(per.getLastName());
						oldPerson.setAnimal(per.getAnimal());
					}
				}
				return null;
			}
		}).when(persRepMock).save(Mockito.any(Person.class));

		Person per = persons.get(0);
		String newFirstName = "AA";
		per.setFirstName(newFirstName);
		personService.save(per);

		assertEquals(personsSize, persons.size());
		assertEquals(0L, persons.get(0).getId().longValue());
		assertEquals(newFirstName, persons.get(0).getFirstName());
	}

	@Test
	public void delete() {

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					Person an = (Person) os[0];

					if (persons.contains(an)) {
						persons.remove(an);
					}
				}
				return null;
			}
		}).when(persRepMock).delete(Mockito.any(Person.class));

		Person per = persons.get(0);
		personService.delete(per);

		assertEquals(personsSize- 1, persons.size());
	}


	@Test
	public void deleteById() {

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					Long id = (Long) os[0];

					for (Person person : persons) {
						if(person.getId().equals(id)) {
							persons.remove(person);
							break;
						}
					}
				}
				return null;
			}
		}).when(persRepMock).deleteById(Mockito.any(Long.class));

		personService.deleteById(0l);

		assertEquals(personsSize- 1, persons.size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void deleteAll() {

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					
					List<Person> personsToAdd = (List<Person>) os[0];

					for (Person person : personsToAdd) {
						if (persons.contains(person)) {
							persons.remove(person);
						}
					}
				}
				return null;
			}
		}).when(persRepMock).deleteAll(Mockito.any(Iterable.class));

		Person per1 = persons.get(0);
		Person per2 = persons.get(1);

		List<Person> personsToDelete = new ArrayList<>();

		personsToDelete.add(per1);
		personsToDelete.add(per2);

		personService.deleteAll(personsToDelete);

		assertEquals(personsSize -2, persons.size());
	}


	
	
}
