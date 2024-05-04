package com.srosales.springboot.webflux.app.modelos.servicios;

import com.srosales.springboot.webflux.app.modelos.documentos.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoServicio {
    public Flux<Producto> findAll();
    public Flux<Producto> findAllConNombreMayusculas();
    public Flux<Producto> findAllConNombreMayusculasRepetir();
    public Mono<Producto> findById(String id);
    public Mono<Producto> save(Producto producto);
    public Mono<Void> delete(Producto producto);
}
