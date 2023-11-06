package com.srosales.springboot.webflux.app.modelos.dao;

import com.srosales.springboot.webflux.app.modelos.documentos.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {
}
