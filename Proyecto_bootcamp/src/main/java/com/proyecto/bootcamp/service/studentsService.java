package com.proyecto.bootcamp.service;

import com.proyecto.bootcamp.bean.Parents;
import com.proyecto.bootcamp.bean.Students;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface studentsService {
	
	
	
public	Mono<Students> save(Students parent);
	
public Flux<Students> findAll();

public Mono<Students> findById(String id);

	
}
