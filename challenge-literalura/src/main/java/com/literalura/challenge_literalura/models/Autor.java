package com.literalura.challenge_literalura.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autor")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAutor;
    @Column(unique = true)
    private String nombre;
    private int fechaNacimiento;
    private int fechaDefuncion;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libro;

    public Autor(){
    }
    public Autor(DatosAutor datosAutor){
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaDefuncion = datosAutor.fechaDefuncion();
    }

    // Getter and Setter
    public Long getId() {
        return idAutor;
    }
    public void setId(Long id) {
        this.idAutor = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(int fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public int getFechaDefuncion() {
        return fechaDefuncion;
    }
    public void setFechaDefuncion(int fechaDefuncion) {
        this.fechaDefuncion = fechaDefuncion;
    }
    public List<Libro> getLibro() {
        return libro;
    }
    public void setLibro(List<Libro> libro) {
        this.libro = libro;
    }

    public String librosAutor (List<Libro> libro) {
        return libro.stream()
                .map(Libro::getTitulo).collect(Collectors.joining("\n"));

    }
    @Override
    public String toString() {
        return
                "---------- Autor ---------\n" +
                        "Nombre: " + nombre + "\n" +
                        "Año de Nacimiento: " + fechaNacimiento + "\n" +
                        "Año de Fallecimiento: " + fechaDefuncion + "\n" +
                        "Libros escritos: " + librosAutor(libro) + "\n";
    }
}



