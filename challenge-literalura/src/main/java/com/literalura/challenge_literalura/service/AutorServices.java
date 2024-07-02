package com.literalura.challenge_literalura.service;


import com.literalura.challenge_literalura.models.Autor;
import com.literalura.challenge_literalura.repositorio.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorServices {

    @Autowired
    private AutorRepository autorRepository;



    public List<Autor> getAutoresVivosPorAno(int year) {
        return autorRepository.findAutorByFecha(year);
    }


}