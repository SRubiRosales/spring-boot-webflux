package com.srosales.springboot.webflux.app.controllers;

import com.srosales.springboot.webflux.app.modelos.documentos.Producto;
import com.srosales.springboot.webflux.app.modelos.servicios.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Date;

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
        model.addAttribute("boton", "Crear");
        return Mono.just("formulario");
    }

    @GetMapping("/form-v2/{id}")
    public Mono<String> editarV2(@PathVariable String id, Model model) {
        return servicio.encontrarPorId(id).doOnNext(p -> {
                    log.info("Producto encontrado: " + p.getNombre());
                    model.addAttribute("producto", p);
                    model.addAttribute("boton", "Editar");
                    model.addAttribute("titulo", "Editar Producto");
                }).defaultIfEmpty(new Producto())
                .flatMap(p -> {
                    if (p.getId() == null) {
                        return Mono.error(new InterruptedException("El producto no existe"));
                    }
                    return Mono.just(p);
                })
                .then(Mono.just("formulario"))
                .onErrorResume(e -> Mono.just("redirect:/listar?error=el+producto+no+existe"));
    }

    @GetMapping("/form/{id}")
    public Mono<String> editar(@PathVariable String id, Model model) {
        Mono<Producto> producto = servicio.encontrarPorId(id).doOnNext(p -> {
            log.info("Producto encontrado: " + p.getNombre());
        }).defaultIfEmpty(new Producto());
        model.addAttribute("producto", producto);
        model.addAttribute("boton", "Editar");
        model.addAttribute("titulo", "Editar Producto");
        return Mono.just("formulario");
    }

    @PostMapping("/form")
    public Mono<String> guardar(@Valid Producto producto, BindingResult result, Model model, SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("boton", "Guardar");
            model.addAttribute("titulo", "Errores en formulario Producto");
            return  Mono.just("formulario");
        } else {
            status.setComplete();
            if (producto.getCreateAt()== null) {
                producto.setCreateAt(new Date());
            }
            return servicio.guardar(producto).doOnNext(p -> {
                log.info("Producto guardado" + p.getNombre() + " Id: " + p.getId());
            }).thenReturn("redirect:/listar?exito=producto+guardado+con+exito");
        }
    }

    @GetMapping("/eliminar/{id}")
    public Mono<String> eliminar(@PathVariable String id) {
        return servicio.encontrarPorId(id).defaultIfEmpty(new Producto())
                .flatMap(p -> {
                    if (p.getId() == null) {
                        return Mono.error(new InterruptedException("El producto no existe"));
                    }
                    return Mono.just(p);
                }).flatMap(producto -> {
                    log.info("Eliminando producto " + producto.getNombre());
                    log.info("Producto Id" + producto.getId());
            return servicio.eliminar(producto);
        }).then(Mono.just("redirect:/listar?exito=producto+eliminado+con+exito"))
        .onErrorResume(e -> Mono.just("redirect:/listar?error=el+producto+a+eliminar+no+existe"));
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
