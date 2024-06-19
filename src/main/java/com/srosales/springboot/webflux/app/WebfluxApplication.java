package com.srosales.springboot.webflux.app;

import com.srosales.springboot.webflux.app.modelos.documentos.Categoria;
import com.srosales.springboot.webflux.app.modelos.documentos.Producto;
import com.srosales.springboot.webflux.app.modelos.servicios.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class WebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoServicio servicio;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(WebfluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Elimina los datos del collection al finalizar
		mongoTemplate.dropCollection("productos").subscribe();
		mongoTemplate.dropCollection("categorias").subscribe();

		Categoria electronica = new Categoria("Electronica");
		Categoria deporte = new Categoria("Deporte");
		Categoria computacion = new Categoria("Computacion");
		Categoria muebles = new Categoria("Muebles");

		Flux.just(electronica, deporte, computacion, muebles)
			.flatMap(servicio::guardarCategoria)
			.doOnNext(categoria ->  {
				log.info("Categoria creada: " + categoria.getNombre() + categoria.getId());
			})
			.thenMany(
				Flux.just(
					new Producto("TV Panasonic Pantalla LCD", 456.89, electronica),
					new Producto("Sony Camara HD Digital", 177.89, electronica),
					new Producto("Apple iPod", 46.89, electronica),
					new Producto("Sony Notebook", 846.89, computacion),
					new Producto("Hewlett Packard Multifuncional", 200.89, computacion),
					new Producto("Bianchi Bicicleta", 70.89, deporte),
					new Producto("HP Notebook Omen 17", 2500.89, computacion),
					new Producto("Mica Comoda 5 cajones", 150.89, muebles),
					new Producto("TV Sony Bravia 4K Ultra HD", 2255.89, electronica)
				)
				.flatMap(producto -> {
					producto.setCreateAt(new Date());
					return servicio.guardar(producto);
				})
			)
			.subscribe(productoMono -> log.info("Insert: " + productoMono.getId() + " " + productoMono.getNombre()));
	}
}
