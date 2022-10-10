package com.example.sbur1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class Sbur1Application {

	public static void main(String[] args) {
		SpringApplication.run(Sbur1Application.class, args);
	}

}

@Entity
class Coffee {
	@Id
	private String id;
	private String name;

	Coffee() {
	}

	public Coffee(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Coffee(String name) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

@RestController
@RequestMapping("/coffees") // for all the method mappings to start with this
class RestApiDemoController {

	@Autowired
	private final CoffeeRepository coffeeRepository;

//	private List<Coffee> coffees = new ArrayList<>();

	public RestApiDemoController(CoffeeRepository coffeeRepository) {
//		coffees.addAll(List.of(
//				new Coffee("Cafe 1"),
//				new Coffee("Cafe 2"),
//				new Coffee("Cafe 3"),
//				new Coffee("Cafe 4")
//				));
		this.coffeeRepository = coffeeRepository;

//		moved to DataLoader
//		this.coffeeRepository.saveAll(List.of(
//				new Coffee("Cafe 1"),
//				new Coffee("Cafe 2"),
//				new Coffee("Cafe 3"),
//				new Coffee("Cafe 4")
//				));

	}
// @RequestMapping(value = "/coffees", method = RequestMethod.GET)
	@GetMapping
	Iterable<Coffee> getCoffees() {
//		return coffees;
		return coffeeRepository.findAll();
	}

	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id) {
//		return coffees.stream().filter(coffee -> coffee.getId().equals(id)).findFirst();
		return coffeeRepository.findById(id);
	}

	@PostMapping
	Coffee postCoffee(@RequestBody Coffee coffee) {
//		coffees.add(coffee);
//		return coffee;
		return coffeeRepository.save(coffee);
	}

	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
//		int coffeeIndex = -1;
//
//		for (Coffee c : coffees) {
//			if (c.getId().equals(id)) {
//				coffeeIndex = coffees.indexOf(c);
//				coffees.set(coffeeIndex, coffee);
//			}
//		}
//		return (coffeeIndex == -1) ?
//				new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED) :
//				new ResponseEntity<>(coffee, HttpStatus.OK);

		return (!coffeeRepository.existsById(id)) ?
				new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.OK) :
				new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable String id) {
//		Optional<Coffee> present = coffees.stream().filter(c -> c.getId().equals(id)).findFirst();
//		present.ifPresent(coffee -> coffees.remove(coffee));
		coffeeRepository.deleteById(id);
	}
}

interface CoffeeRepository extends CrudRepository<Coffee, String> {}

@Component
class DataLoader {
	@Autowired
	private final CoffeeRepository coffeeRepository;

	DataLoader(CoffeeRepository coffeeRepository){
		this.coffeeRepository = coffeeRepository;
	}

	@PostConstruct
	private void loadData() {
		this.coffeeRepository.saveAll(List.of(
				new Coffee("Cafe 1"),
				new Coffee("Cafe 2"),
				new Coffee("Cafe 3"),
				new Coffee("Cafe 4")
		));
	}
}