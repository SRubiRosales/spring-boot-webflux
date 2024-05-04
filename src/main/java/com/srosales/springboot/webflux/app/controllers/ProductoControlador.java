package com.srosales.springboot.webflux.app.controllers;

import com.srosales.springboot.webflux.app.modelos.dao.ProductoDao;
import com.srosales.springboot.webflux.app.modelos.documentos.Producto;
import com.srosales.springboot.webflux.app.modelos.servicios.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller
public class ProductoControlador {
    @Autowired
    private ProductoServicio servicio;

    private static final Logger log = LoggerFactory.getLogger(ProductoControlador.class);

    @GetMapping({"/listar", "/"})
    public Mono<String> listar(Model model) {
        Flux<Producto> productos = servicio.listarConNombreMayusculas();

        productos.subscribe(p -> log.info(p.getNombre()));
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Lista de productos");
        return Mono.just("listar");
    }

    @GetMapping("/form")
    public Mono<String> crear(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("titulo", "Producto");
        return Mono.just("form");
    }

    @PostMapping("/form")
    public Mono<String> guardar(Producto producto) {
        return servicio.guardar(producto).doOnNext(p -> {
            log.info("Producto guardado" + p.getNombre() + " Id: " + p.getId());
        }).thenReturn("redirect:/listar");
    }
    @GetMapping("/listar-datadriver")
    public String listarDataDriver(Model model) {
        Flux<Producto> productos = servicio.listarConNombreMayusculas().delayElements(Duration.ofSeconds(1));

        productos.subscribe(p -> log.info(p.getNombre()));
        model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 1));
        model.addAttribute("titulo", "Lista de productos");
        return "listar";
    }

    @GetMapping("/listar-full")
    public String listarFull(Model model) {
        Flux<Producto> productos = servicio.listarConNombreMayusculasRepetir();

        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Lista de productos");
        return "listar";
    }
    @GetMapping("/listar-chunked")
    public String listarChunked(Model model) {
        Flux<Producto> productos = servicio.listarConNombreMayusculasRepetir();

        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Lista de productos 2");
        return "listar-chunked";
    }
}
