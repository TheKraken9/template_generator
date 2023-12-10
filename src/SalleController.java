import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/salles")
public class SalleController {

    private final SalleRepository salleRepository;

    @Autowired
	public SalleController(SalleRepository salleRepository) {
		this.salleRepository = salleRepository;
	}


    @GetMapping
	public List<Salle> getAllSalles() {
		return salleRepository.findAll();
	}


    @GetMapping("/{id}")
	public ResponseEntity<Salle> getSalleById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
		Salle salle = salleRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Salle not found for this id :: " + id));
		return ResponseEntity.ok().body(salle);
	}


    @PostMapping
	public Salle createSalle(@Valid @RequestBody Salle salle) {
		return salleRepository.save(salle);
	}


    @PutMapping("/{id}")
	public ResponseEntity<Salle> updateSalle(@PathVariable(value = "id") Long id,
		@Valid @RequestBody Salle salle) throws ResourceNotFoundException {
		salle.setId(id);
		return ResponseEntity.ok(salleRepository.save(salle));
	}


    @DeleteMapping("/{id}")
	public Map<String, Boolean> deleteSalle(@PathVariable(value = "id") Long id)
		throws ResourceNotFoundException {
		Salle salle = salleRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Salle not found for this id :: " + id));
		salleRepository.deleteById(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

}
