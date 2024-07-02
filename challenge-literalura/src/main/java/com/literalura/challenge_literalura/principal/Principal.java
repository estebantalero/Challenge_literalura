package com.literalura.challenge_literalura.principal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.literalura.challenge_literalura.models.*;
import com.literalura.challenge_literalura.repositorio.AutorRepository;
import com.literalura.challenge_literalura.repositorio.LibroRepository;
import com.literalura.challenge_literalura.service.AutorServices;
import com.literalura.challenge_literalura.service.ConsumoAPI;
import com.literalura.challenge_literalura.service.ConvierteDatos;
import com.literalura.challenge_literalura.service.LibroServices;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private LibroServices libroServices = new LibroServices();
    private AutorServices autorServices = new AutorServices();
    private Autor autorLibro;
    private DatosAutor datosAutor;
    private DatosLibro datosLibro;
    private List<Autor> autores;
    private List<Libro> libros;
    private int opcion = 0;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository){
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void mostrarMenu() throws Exception {
        var menu =
                "-----------------------------------------\n" +
                "MENU PRINCIPAL\n" +
                "1 - Buscar libro por titulo\n" +
                "2 - Listar libros almacenados\n" +
                "3 - Listar autor almacenado\n" +
                "4 - Listar autor vivo por año\n" +
                "5 - Listar libros por idioma" +
                "\n" +
                "0 - Salir" +
                "\n" +
                "Elige la opción que desea realizar";
        do {
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion){
                case 1:
                    buscarLibroPorNombre();
                    break;
                case 2:
                    listarLibrosGuardados();
                    break;
                case 3:
                    listarAutoresGuardados();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarPorIdiomas();
                    break;
                case 0:
                    System.out.println("Esperamos Volver a servirte");
                    break;
                default:
                    System.out.println("opcion no valida");
            }
        }
        while (opcion != 0);
    }

    private void listarPorIdiomas() {
        System.out.println("\nListado de idiomas almacenados:");
        var listaDeIdiomas = libroRepository.findDistinctIdiomas();
        for (int i = 0; i < listaDeIdiomas.size() ; i++) {
            System.out.println("[" + (i + 1) + "] " + Idioma.from(listaDeIdiomas.get(i)));
        }
        System.out.println("\nSeleccione el idioma que desea buscar: ");
        var eleccion = teclado.nextInt();
        if (eleccion < 1 || eleccion > listaDeIdiomas.size()) {
            System.out.println("Opción Inválida");
            return;
        }
        String idiomaSeleccionado =Idioma.from(listaDeIdiomas.get(eleccion - 1)).toString();
        System.out.println("\nListado de libros en - [ " + idiomaSeleccionado.toUpperCase() + " ]:");
        libros = libroRepository.findByIdioma(listaDeIdiomas.get(eleccion - 1));
        libros.forEach(System.out::println);
        System.out.println();
    }


    private void listarAutoresVivos() {
        System.out.println("\nEscriba el año de busqueda: ");
        var ano = teclado.nextInt();

        autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No se encontro autores vivos en el año seleccionado:");
        }else {
            System.out.println(ano);
            System.out.println("\nAutores vivos en el año " + ano + ": ");
            autores = autorRepository.findAutorByFecha(ano);
            autores.forEach(System.out::println);

        }
    }

    private void listarAutoresGuardados() {
        System.out.println("\nListado de autores almacenados:");
        autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void listarLibrosGuardados() {
        System.out.println("\nListado de libros almacenados:");
        libros = libroRepository.findAll();
        libros.forEach(System.out::println);
    }

    private void buscarLibroPorNombre() throws Exception {
        System.out.println("Escriba el título del libro que desea buscar: ");
        var tituloLibro = teclado.nextLine();
        var resultadoBusqueda = new ConsumoAPI().obtenerDatos(tituloLibro);

        JSONObject jsonObject = new JSONObject(resultadoBusqueda);
        JSONArray resultsArray = jsonObject.getJSONArray("results");

        if (resultsArray.isEmpty()) {
            System.out.println("Libro no encontrado: " + tituloLibro);
            return;
        }

        System.out.println("Se encontraron " + resultsArray.length() + " libros: \n");
        for (int i = 0; i < resultsArray.length(); i++) {
            System.out.println("[" + (i + 1) + "] " + resultsArray.getJSONObject(i).getString("title"));
        }

        System.out.println("\nSeleccone el libro deseado indicando su número: ");
        var numeroLibro = teclado.nextInt();
        if (numeroLibro == 0) {
            return;
        }
        numeroLibro = numeroLibro - 1;
        jsonObject = new JSONObject(resultsArray.getJSONObject(numeroLibro).toString());
        datosLibro = convierteDatos.obtenerDatos(jsonObject.toString(), DatosLibro.class);

        //Para verificar libro ya almacenado
        Optional<Libro> libro = libroRepository.findById(datosLibro.idLibro());
        if (libro.isPresent()) {
            System.out.println("Libro ya esta almacenado");
            System.out.println();
            System.out.println(libro.get());
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = mapper.readValue(jsonObject.toString(), Map.class);
        String authorsJson = mapper.writeValueAsString((List<Map<String, Object>>) jsonMap.get("authors"));
        String resultado = authorsJson.substring(1, authorsJson.length() - 1);
        datosAutor = convierteDatos.obtenerDatos(resultado, DatosAutor.class);

        String idioma = new ConsumoAPI().getIdioma(jsonMap);

        autores = autorRepository.findAll();
        Optional<Autor> autor = autores.stream()
                .filter(a -> a.getNombre().equals(datosAutor.nombre()))
                .findFirst();
        if (autor.isPresent()) {
            autorLibro = autor.get();
        } else {
            autorLibro = new Autor(datosAutor);
            autorRepository.save(autorLibro);
        }
        Libro libroNuevo = new Libro(datosLibro, autorLibro, idioma);
        libroRepository.save(libroNuevo);
        System.out.println("Libro almacenado exitosamente");
        System.out.println();
        System.out.println(libroNuevo);
    }
}

