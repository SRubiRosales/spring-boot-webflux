package com.srosales.springboot.webflux.app;

import com.srosales.springboot.webflux.app.modelos.dao.ProductoDao;
import com.srosales.springboot.webflux.app.modelos.documentos.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class WebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoDao dao;

	private static final Logger log = LoggerFactory.getLogger(WebfluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux.just(
				new Producto("TV Panasonic Pantalla LCD", 456.89),
				new Producto("Sony Camara HD Digital", 177.89),
				new Producto("Apple iPod", 46.89),
				new Producto("Sony Notebook", 846.89),
				new Producto("Hewlett Packard Multifuncional", 200.89),
				new Producto("Bianchi Bicicleta", 70.89),
				new Producto("HP Notebook Omen 17", 2500.89),
				new Producto("Mica Comoda 5 cajones", 150.89),
				new Producto("TV Sony Bravia 4K Ultra HD", 2255.89)
			)
			.flatMap(producto -> dao.save(producto))
			.subscribe(productoMono -> log.info("Insert: " + productoMono.getId() + " " + productoMono.getNombre()));
	}
}
