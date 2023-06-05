package ma.enset.inscriptionpedagogique.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.enset.inscriptionpedagogique.dto.EtudiantResponse;
import ma.enset.inscriptionpedagogique.dto.RequiredSearchParams;
import ma.enset.inscriptionpedagogique.service.EtudiantService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/etudiants")
@RequiredArgsConstructor
@Validated
public class EtudiantController {

    private final EtudiantService service;

    @GetMapping("/search")
    public ResponseEntity<List<EtudiantResponse>> getAllBySearchParams(@Valid RequiredSearchParams searchParams) {

        return ResponseEntity.ok(service.findAllBySearchParams(searchParams));
    }

}
