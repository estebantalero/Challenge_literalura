package com.literalura.challenge_literalura.models;

import jakarta.persistence.*;
import jdk.jfr.Name;

@Entity
@Table(name = "libro")
public class Libro {
    @Id
    private int idLibro;
    @Column(unique = true)
    @Name(value = "titulo")
    private String titulo;
    private String idioma;
    private Double numeroDeDescargas;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idAutor")
    private Autor autor;

    public Libro(){
        
    }
    public Libro(DatosLibro datosLibro, Autor autor, String idioma){
        this.idLibro = datosLibro.idLibro();
        this.titulo = datosLibro.titulo();
        this.autor = autor;
        this.idioma = idioma;
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
    }
    @Override
    public String toString() {
        return
                "----------- Libro -----------\n" +
                        "Titulo: " + titulo + "\n" +
                        "Autor: " + autor.getNombre() + "\n" +
                        "Idioma: " + Idioma.from(idioma) + "\n" +
                        "Numero de descargas: " + numeroDeDescargas +"\n";
    }

    // Getter and Setter


    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
