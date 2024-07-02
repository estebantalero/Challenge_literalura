package com.literalura.challenge_literalura.repositorio;


import com.literalura.challenge_literalura.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long>{
    List<Autor> findAutorByNombre(String nombre);

    @Query(value = "SELECT a FROM Autor a WHERE a.fechaDefuncion >:year AND a.fechaNacimiento <:year")
    List<Autor> findAutorByFecha(int year);
}
