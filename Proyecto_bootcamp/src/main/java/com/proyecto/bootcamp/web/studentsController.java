package com.proyecto.bootcamp.web;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.net.URI;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.*;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.slf4j.Logger;

import com.proyecto.bootcamp.bean.Parents;
import com.proyecto.bootcamp.bean.Students;
import com.proyecto.bootcamp.service.parentsServiceImpl;
import com.proyecto.bootcamp.service.studentsServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/index")
@Api(value = "Proyecto Bootcamp")
public class studentsController {

	@Autowired
	private parentsServiceImpl service;

	@Autowired
	private studentsServiceImpl servicepa;

	@GetMapping(value = "/listall", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "LIST PARENTS", response = Parents.class, httpMethod = "GET")
	@ApiResponses({ @ApiResponse(code = 200, message = "Se ejecuto satisfactoriamente.", response = Parents.class),
			@ApiResponse(code = 400, message = "Envio datos incorrectos.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 404, message = "No existe una entidad con ese ID.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 500, message = "Error inesperado.", response = ExceptionInInitializerError.class) })
	public Mono<ResponseEntity<Flux<Parents>>> listStudents() {

		System.out.println("Prueba");

		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(service.findAll()));

	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/create")
	@ApiOperation(value = "CREATE PARENTS", response = Parents.class, httpMethod = "GET")
	@ApiResponses({ @ApiResponse(code = 200, message = "Se ejecuto satisfactoriamente.", response = Parents.class),
			@ApiResponse(code = 400, message = "Envio datos incorrectos.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 404, message = "No existe una entidad con ese ID.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 500, message = "Error inesperado.", response = ExceptionInInitializerError.class) })
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ResponseEntity<? extends Object>> postParents(@RequestBody Mono<Parents> request) {
//		int contador = 0;
//		for (int i = 0; i < resultado.getParents().size(); i++) {
//			if (resultado.getParents().get(i).getRelacion().equals("padre")
//					|| resultado.getParents().get(i).getRelacion().equals("madre")) {
//				contador++;
//
//			}
//		}
//		
//		
//		
//		for (int i = 0; i < request.getParents().size(); i++) {
//		
//			if(request.getParents())
//			
//			
//			Mono<Students> categoria = servicepa.findById(request.getParents().get(i).get_id());
//		}
//		return cate
//		
		Map<String, Object> respuesta = new HashMap<String, Object>();
		return request.flatMap(resultado -> {
			

			if (resultado.get_id() == null) {
				resultado.set_id(resultado.get_id());
			}
			for (int i = 0; i < resultado.getParents().size(); i++) {
				if (resultado.getParents().get(i).getDateBirth() == null) {
					resultado.setParents(resultado.getParents());
					resultado.getParents().get(i).setDateBirth(new Date());

				}

			}

			if (resultado.getDateBirth() == null) {
				resultado.setDateBirth(new Date());
			}

			if (resultado.getParents().size() >= 2) {
				int contador = 0;
				for (int i = 0; i < resultado.getParents().size(); i++) {
					if (resultado.getParents().get(i).getRelacion().equals("padre")
							|| resultado.getParents().get(i).getRelacion().equals("madre")) {
						contador++;

					}
				}
				if (contador <= 2) {


//					List<Students> stpa = (List<Students>) servicepa.findById(resultado.getParents().get(contador).get_id());
					

					return service.save(resultado).map(p -> {

						int contador2 = 0;
						for (int i = 0; i < resultado.getParents().size(); i++) {
							if (resultado.getParents().get(i).getRelacion().equals("padre")
									|| resultado.getParents().get(i).getRelacion().equals("madre")) {
								contador2++;

							}
						}
						
				//		p.setParents(stpa);
						resultado.setStpar(p.getParents().get(contador2));
						respuesta.put("parents", p);
						respuesta.put("mensaje", "Padres creado con éxito");
						respuesta.put("timestamp", new Date());
						return ResponseEntity.created(URI.create("/index/".concat(p.get_id())))
								.contentType(MediaType.APPLICATION_JSON_UTF8).body(respuesta);

					});
				}

				else {
					respuesta.put("mensaje", "Minimo tiene que tener dos padres");
					return Mono.just(ResponseEntity.badRequest().body(respuesta));
				}
			}

			return Mono.just(ResponseEntity.ok().body(resultado));

		});
		
	

//		int contador = 0;
//		for (int i = 0; i < request.getParents().size(); i++) {
//			if (request.getParents().get(i).getRelacion().equals("padre")
//					|| request.getParents().get(i).getRelacion().equals("madre")) {
//				contador++;
//
//			}
//		}
//
//		Mono<Students> stpa = servicepa.findById(request.getParents().get(contador).get_id());
//
//
//		return stpa.map(c -> {
//		
//			return service.save(request);
//		});

	}

//	  @PutMapping(value = "/entities/{entityId}", produces = {
//			    MediaType.APPLICATION_STREAM_JSON_VALUE,
//			    MediaType.APPLICATION_JSON_VALUE
//	  })
//			  public Mono<Parents> putParents(
//			                                        @PathVariable Integer entityId,
//			                                        @RequestBody @Validated Mono<Parents> requestEntity) {
//			    
//			    return requestEntity
//			      .flatMap(e -> service.put(e, entityId));
//			  }

	@DeleteMapping("/{id}")
	@ApiOperation(value = "DELETE PARENTS", response = Parents.class, httpMethod = "GET")
	@ApiResponses({ @ApiResponse(code = 200, message = "Se ejecuto satisfactoriamente.", response = Parents.class),
			@ApiResponse(code = 400, message = "Envio datos incorrectos.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 404, message = "No existe una entidad con ese ID.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 500, message = "Error inesperado.", response = ExceptionInInitializerError.class) })
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id) {
		return service.findById(id).flatMap(p -> {
			return service.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
		}).onErrorReturn(new ResponseEntity<Void>(HttpStatus.BAD_REQUEST));

	}

//	@PostMapping(value = "/createpo", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
//			MediaType.APPLICATION_JSON_VALUE })
//	public Mono<Students> guardar(@Valid Parents stu){
//		
//		
//		
//		
//		
//  
//
//	}
//	

	@GetMapping("/consultarnombre/{nombre}")
	@ApiOperation(value = "LIST PARENTS NAME", response = Parents.class, httpMethod = "GET")
	@ApiResponses({ @ApiResponse(code = 200, message = "Se ejecuto satisfactoriamente.", response = Parents.class),
			@ApiResponse(code = 400, message = "Envio datos incorrectos.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 404, message = "No existe una entidad con ese ID.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 500, message = "Error inesperado.", response = ExceptionInInitializerError.class) })
	public Mono<ResponseEntity<Mono<Parents>>> consultarnombre(@PathVariable String nombre) {

		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(service.findByComplementName(nombre).switchIfEmpty(Mono.empty())));

	}

	@GetMapping("/numero/{numero}")
	@ApiOperation(value = "LIST PARENTS NUMBER", response = Parents.class, httpMethod = "GET")
	@ApiResponses({ @ApiResponse(code = 200, message = "Se ejecuto satisfactoriamente.", response = Parents.class),
			@ApiResponse(code = 400, message = "Envio datos incorrectos.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 404, message = "No existe una entidad con ese ID.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 500, message = "Error inesperado.", response = ExceptionInInitializerError.class) })
	public Mono<ResponseEntity<Mono<Parents>>> consultarnumero(@PathVariable String numero) {

		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(service.findByNumberidentify(numero).switchIfEmpty(Mono.empty())));

	}

	@GetMapping("/consultaid/{id}")
	@ApiOperation(value = "FIND PARENTS ID", response = Parents.class, httpMethod = "GET")
	@ApiResponses({ @ApiResponse(code = 200, message = "Se ejecuto satisfactoriamente.", response = Parents.class),
			@ApiResponse(code = 400, message = "Envio datos incorrectos.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 404, message = "No existe una entidad con ese ID.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 500, message = "Error inesperado.", response = ExceptionInInitializerError.class) })
	public Mono<ResponseEntity<Mono<Parents>>> findById(@PathVariable String id) {

		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(service.findById(id)));

	}

//	  @PutMapping("/{id}")
//		public Mono<Parents> editar(@PathVariable("id") final String id, @RequestBody final Parents parents){
//			return service.update(id, parents);
//		}
//	

	@PutMapping(value = "/{id}")
	@ApiOperation(value = "UPDATE PARENTS", response = Parents.class, httpMethod = "GET")
	@ApiResponses({ @ApiResponse(code = 200, message = "Se ejecuto satisfactoriamente.", response = Parents.class),
			@ApiResponse(code = 400, message = "Envio datos incorrectos.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 404, message = "No existe una entidad con ese ID.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 500, message = "Error inesperado.", response = ExceptionInInitializerError.class) })
	public Mono<ResponseEntity<Parents>> updateParents(@PathVariable("id") String id, @RequestBody Parents parents) {
		return service.findById(id).flatMap(resultado -> {
			resultado.setComplementName(parents.getComplementName());
			resultado.setDateBirth(parents.getDateBirth());
			resultado.setNumberidentify(parents.getNumberidentify());
			resultado.setSex(parents.getSex());
			resultado.setTypeidentify(parents.getTypeidentify());

			for (int i = 0; i < resultado.getParents().size(); i++) {
				resultado.getParents().get(i).setComplementName(parents.getParents().get(i).getComplementName());
				resultado.getParents().get(i).setSex(parents.getParents().get(i).getSex());
				resultado.setParents(parents.getParents());
				resultado.getParents().get(i).setTypeidentify(parents.getParents().get(i).getTypeidentify());
				resultado.getParents().get(i).setNumberidentify(parents.getParents().get(i).getNumberidentify());
			}
			return service.save(resultado);
		}).map(respuesta -> new ResponseEntity<Parents>(respuesta, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = "/listtodate/{start}/{end}", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "UPDATE PARENTS", response = Parents.class, httpMethod = "GET")
	@ApiResponses({ @ApiResponse(code = 200, message = "Se ejecuto satisfactoriamente.", response = Parents.class),
			@ApiResponse(code = 400, message = "Envio datos incorrectos.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 404, message = "No existe una entidad con ese ID.", response = ExceptionInInitializerError.class),
			@ApiResponse(code = 500, message = "Error inesperado.", response = ExceptionInInitializerError.class) })
	public Mono<ResponseEntity<Flux<Parents>>> findByDateBirthBetween(
			@DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable("start") Date start,
			@DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable("end") Date end) {

		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(service.findDateBirthbetween(start, end))).switchIfEmpty(Mono.empty());

	}

//	  
//	  
//	  
//	  @GetMapping(
//			    value = "/index",
//			    produces = {
//			      MediaType.APPLICATION_STREAM_JSON_VALUE,
//			      MediaType.APPLICATION_JSON_VALUE
//			    }
//)
//	  public void listParents() {
//		  
//		  
//		  
//		  
//	  }

}
