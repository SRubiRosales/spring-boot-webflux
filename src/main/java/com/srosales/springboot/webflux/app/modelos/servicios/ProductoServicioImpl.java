package com.srosales.springboot.webflux.app.modelos.servicios;

import com.srosales.springboot.webflux.app.modelos.dao.CategoriaDao;
import com.srosales.springboot.webflux.app.modelos.dao.ProductoDao;
import com.srosales.springboot.webflux.app.modelos.documentos.Categoria;
import com.srosales.springboot.webflux.app.modelos.documentos.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServicioImpl implements ProductoServicio{
    @Autowired
    private ProductoDao dao;

    @Autowired
    private CategoriaDao categoriaDao;
    @Override
    public Flux<Producto> listar() {
        return dao.findAll();
    }

    @Override
    public Flux<Producto> listarConNombreMayusculas() {
        return dao.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        });
    }

    @Override
    public Flux<Producto> listarConNombreMayusculasRepetir() {
        return listarConNombreMayusculas().repeat(5000);
    }

    @Override
    public Mono<Producto> encontrarPorId(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<Producto> guardar(Producto producto) {
        return dao.save(producto);
    }

    @Override
    public Mono<Void> eliminar(Producto producto) {
        return dao.delete(producto);
    }

    @Override
    public Flux<Categoria> listarCategorias() {
        return categoriaDao.findAll();
    }

    @Override
    public Mono<Categoria> encontrarCategoriaPorId(String id) {
        return categoriaDao.findById(id);
    }

    @Override
    public Mono<Categoria> guardarCategoria(Categoria categoria) {
        return categoriaDao.save(categoria);
    }
}
