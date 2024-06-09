package org.example.carservice.web;

import org.example.carservice.Application;
import org.example.carservice.PostgresContainer;
import org.example.carservice.command.CreateCarCommand;
import org.example.carservice.dto.CarResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@ContextConfiguration(initializers = PostgresContainer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;


    private static final PostgresContainer container = new PostgresContainer();

    @BeforeAll
    void beforeAll() throws IOException {
        container.initDatabase();
    }

    @AfterEach
    void afterEach() throws IOException {
        container.clearRecords();
    }

    @AfterAll
    void afterAll() throws IOException {
        container.clearRecords();
        container.clearDatabase();
    }
    @Test
    void shouldCreateCar(){
        // given
        var createCarCommand = CreateCarCommand.builder()
                .name("test-name")
                .mileage(1000000)
                .build();

        // when
        createCar(createCarCommand);

        // then
        shouldCreateCarInDatabase("test-name", 1000000.0);
    }

    @Test
    void shouldNotCreateCarIfCarWithTheSameNameExist(){
        // given
        var createCarCommand = CreateCarCommand.builder()
                .name("test-name")
                .mileage(1000)
                .build();
        // when
        createCar(createCarCommand);

        // then
        shouldCreateCarInDatabase("test-name", 1000);

        // and
        var response = testRestTemplate.postForEntity("/api/v1/car", createCarCommand, CarResponse.class);
        Assertions.assertFalse(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void shouldGetCar(){
        // given
        var createCarCommand = CreateCarCommand.builder()
                .name("test-name")
                .mileage(1000)
                .build();
        // when
        createCar(createCarCommand);

        // then
        shouldFindSingleCar("test-name");
    }

    private void shouldFindSingleCar(String carName) {
        var response = testRestTemplate.getForEntity("/api/v1/car/" + carName, CarResponse.class);
        CarResponse carResponse = response.getBody();
        Assertions.assertEquals(carName, carResponse.getName());
    }

    private CarResponse createCar(CreateCarCommand createCarCommand){
        var response = testRestTemplate.postForEntity("/api/v1/car", createCarCommand, CarResponse.class);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        return response.getBody();
    }

    private void shouldCreateCarInDatabase(String name, double mileage){
        var cars = container.executeQueryForObjects("SELECT * from car where name='" + name + "';");
        Assertions.assertFalse(cars.isEmpty());
        Assertions.assertEquals(name, cars.get(0).get("name"));
        Assertions.assertEquals(mileage, Double.parseDouble(String.valueOf(cars.get(0).get("mileage"))));
    }
}
