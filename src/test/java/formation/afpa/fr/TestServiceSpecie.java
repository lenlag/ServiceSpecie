package formation.afpa.fr;

import static org.junit.Assert.assertEquals;
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

import formation.afpa.fr.Specie;
import formation.afpa.fr.SpecieRepository;;

@RunWith(MockitoJUnitRunner.class) // On indique qu’on utilise Mockito
public class TestServiceSpecie {

	@Mock // Ici on indique qu’on a un mock qui simule le repository des Specie
	private SpecieRepository repoMock;

	@InjectMocks // Ici, on indique que le mock est injecté/utilisé par le service à tester (ici
					// ServiceSpecie)
	private ServiceSpecie service;

	// on va utiliser le mock comme si c’était un repository.

	List<Specie> list = new ArrayList<>();
	String beginLatinName = "Gal";
	String commonName = "Chicken";
	Long id = 0l;
	String latinName = "Cygnus";
	int listSize;

	@Before
	public void setUp() {
		list.add(new Specie(0l, "Chicken", "Gallus domesticus"));
		list.add(new Specie(1l, "Swan", "Cygnus"));
		listSize = list.size();
	}

	@Test
	public void findAll() {
		when(repoMock.findAll()).thenReturn(list);
		assertEquals(listSize, service.findAll().size());
	}

	@Test
	public void findByBeginLatinName() {
		when(repoMock.findByBeginLatinName(beginLatinName)).thenReturn(getSpecieBYBeginLatinName(beginLatinName));
		assertEquals(1, service.findByBeginLatinName(beginLatinName).size());
		assertTrue(service.findByBeginLatinName(beginLatinName).get(0).getLatinName().startsWith(beginLatinName));
	}

	private List<Specie> getSpecieBYBeginLatinName(String s) {
		List<Specie> listToReturn = new ArrayList<>();

		for (Specie specie : list) {
			if (specie.getLatinName().startsWith(s)) {
				listToReturn.add(specie);
			}
		}
		return listToReturn;

	}

	@Test
	public void findByCommonName() {
		when(repoMock.findByCommonName(commonName)).thenReturn(listCommon(commonName));
		assertEquals(1, service.findByCommonName(commonName).size());
		assertEquals(service.findByCommonName(commonName).get(0).getCommonName(), commonName);

	}

	private List<Specie> listCommon(String s) {
		List<Specie> listToReturn = new ArrayList<>();

		for (Specie specie : list) {
			if (specie.getCommonName().equals(commonName)) {
				listToReturn.add(specie);
			}
		}
		return listToReturn;
	}

	@Test
	public void findById() {
		when(repoMock.findById(id)).thenReturn(Optional.of(getSpecieById(id)));
		assertEquals(service.findById(id).getCommonName(), commonName);

	}

	private Specie getSpecieById(Long id) {
		Specie sp = null;
		for (Specie specie : list) {
			if (specie.getId() == id) {
				sp = specie;
			}
		}

		return sp;

	}

	@Test
	public void findByLatinName() {
		when(repoMock.findByLatinName(latinName)).thenReturn(getSpecieByLatinName(latinName));
		assertEquals(1, service.findByLatinName(latinName).size());
		assertEquals(service.findByLatinName(latinName).get(0).getLatinName(), latinName);
	}

	private List<Specie> getSpecieByLatinName(String latinName) {
		List<Specie> listToReturn = new ArrayList<>();

		for (Specie specie : list) {
			if (specie.getLatinName().equals(latinName)) {
				listToReturn.add(specie);
			}
		}
		return listToReturn;
	}

	@Test
	public void create() {
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					Specie sp = (Specie) os[0];
					sp.setId(12L);
					list.add(sp);
				}
				return null;
			}
		}).when(repoMock).save(Mockito.any(Specie.class));

		Specie sp = new Specie();
		sp.setCommonName("AA");
		sp.setLatinName("BB");

		service.save(sp);

		assertEquals(list.size(), listSize + 1);
		assertEquals(12L, list.get(2).getId().longValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createAll() {
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					List<Specie> species = (List<Specie>) os[0];

					for (Specie specie : species) {
						long newId = (long) list.size();
						specie.setId(newId);
						list.add(specie);
					}
				}
				return null;
			}
		}).when(repoMock).saveAll(Mockito.any(Iterable.class));
		List<Specie> speciesToAdd = new ArrayList<>();
		Specie sp1 = new Specie();
		sp1.setCommonName("AA");
		sp1.setLatinName("BB");

		Specie sp2 = new Specie();
		sp2.setCommonName("CC");
		sp2.setLatinName("DD");

		speciesToAdd.add(sp1);
		speciesToAdd.add(sp2);

		service.saveAll(speciesToAdd);

		int newSize = listSize + speciesToAdd.size();
		assertEquals(newSize, list.size());
		Specie newSpecie = list.get(newSize - 1);
		assertEquals(3l, newSpecie.getId().longValue());
	}

	@Test
	public void update() {

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					Specie sp = (Specie) os[0];
					Integer indexOf = list.indexOf(sp);

					if (indexOf != null) {
						Specie oldSpecie = list.get(indexOf);
						oldSpecie.setId(sp.getId());
						oldSpecie.setLatinName(sp.getLatinName());
						oldSpecie.setCommonName(sp.getCommonName());
						oldSpecie.setLatinName(sp.getLatinName());
					}
				}
				return null;
			}
		}).when(repoMock).save(Mockito.any(Specie.class));

		Specie sp = list.get(0);
		sp.setCommonName("AA");
		service.save(sp);

		assertEquals(listSize, list.size());
		assertEquals(0L, list.get(0).getId().longValue());
		assertEquals("AA", list.get(0).getCommonName());
	}

	@Test
	public void delete() {

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					Specie sp = (Specie) os[0];

					if (list.contains(sp)) {
						list.remove(sp);
					}
				}
				return null;
			}
		}).when(repoMock).delete(Mockito.any(Specie.class));

		Specie sp = list.get(0);
		service.delete(sp);

		assertEquals(listSize - 1, list.size());
	}

	@Test
	public void deleteById() {

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] os = invocation.getArguments();
				if (os != null && os.length != 0 && os[0] != null) {
					Long id = (Long) os[0];

					for (Specie specie : list) {
						if (specie.getId() == id) {
							list.remove(specie);
							break;
						}
					}
				}
				return null;
			}
		}).when(repoMock).deleteById(Mockito.any(Long.class));

		service.deleteById(0l);

		assertEquals(listSize - 1, list.size());
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
					List<Specie> species = (List<Specie>) os[0];

					for (Specie specie : species) {
						if (list.contains(specie)) {
							list.remove(specie);
						}
					}
				}
				return null;
			}
		}).when(repoMock).deleteAll(Mockito.any(Iterable.class));

		Specie sp1 = list.get(0);
		Specie sp2 = list.get(1);

		List<Specie> species = new ArrayList<>();

		species.add(sp1);
		species.add(sp2);

		service.deleteAll(species);

		assertEquals(listSize - 2, list.size());
	}

}
