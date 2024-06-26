package com.srosales.springboot.webflux.app.modelos.servicios;

import com.srosales.springboot.webflux.app.modelos.documentos.Categoria;
import com.srosales.springboot.webflux.app.modelos.documentos.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoServicio {
    public Flux<Producto> listar();
    public Flux<Producto> listarConNombreMayusculas();
    public Flux<Producto> listarConNombreMayusculasRepetir();
    public Mono<Producto> encontrarPorId(String id);
    public Mono<Producto> guardar(Producto producto);
    public Mono<Void> eliminar(Producto producto);
    public Flux<Categoria> listarCategorias();
    public Mono<Categoria> encontrarCategoriaPorId(String id);
    public Mono<Categoria> guardarCategoria(Categoria categoria);
}
