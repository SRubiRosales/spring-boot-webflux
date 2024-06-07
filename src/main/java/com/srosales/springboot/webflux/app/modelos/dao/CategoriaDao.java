package com.srosales.springboot.webflux.app.modelos.dao;

import com.srosales.springboot.webflux.app.modelos.documentos.Categoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String> {
}
