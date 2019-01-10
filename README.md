# ServiceSpecie
Spring Boot - Unit Testing and Mocking with Mockito and JUnit.


This is a part of a Spring project with Unit testing of Business Services (ServiceSpecie, ServiceAnimal, ServicePerson).

Business Services are tested without using Repositories.

In my Unit Testing I use @Mock to simulate Repositories (SpecieRepository, AnimalRepository, PersonRepository) 
and @InjectMocks on Business Services to test.

pom.xml - Contains all the dependencies needed to build this project
