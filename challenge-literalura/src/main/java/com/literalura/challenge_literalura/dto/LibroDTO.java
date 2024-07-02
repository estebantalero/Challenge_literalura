package com.literalura.challenge_literalura.dto;

import com.literalura.challenge_literalura.models.Autor;

public record LibroDTO(
        int idLibro,
        String titulo,
        Autor autor,
        String idioma,
        int numeroDeDescargas) {
}
