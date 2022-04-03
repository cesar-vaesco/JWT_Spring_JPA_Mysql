package com.vaescode.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.vaescode.springboot.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long> {

	@Query("select p from Producto p where p.nombre like %?1%") // SQL -> like es operador lógico de coincidencia
	public List<Producto> findByNombre(String term);

	public List<Producto> findByNombreLikeIgnoreCase(String term);

}
