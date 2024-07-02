package com.literalura.challenge_literalura.repositorio;

import com.literalura.challenge_literalura.models.Autor;
import com.literalura.challenge_literalura.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<String> findDistinctIdiomaBy();

    Optional<Libro> findById(long id);
    List<Libro> findByAutorIdAutor(Long idAutor);

    Optional<Libro> findLibroByTitulo(String titulo);

    @Query("SELECT l FROM Libro l WHERE l.autor = :autor")
    List<Libro> findLibrosByAutor(Autor autor);

    @Query("SELECT DISTINCT l.idioma FROM Libro l")
    List<String> findDistinctIdiomas();

    @Query("SELECT l FROM Libro l WHERE l.idioma = :language")
    List<Libro> findByIdioma(String language);

    @Query("SELECT l FROM Libro l WHERE l.titulo = :title")
    Optional<Libro> findByTitulo(String title);
}
