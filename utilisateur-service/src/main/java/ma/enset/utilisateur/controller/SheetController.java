package ma.enset.utilisateur.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurResponse;
import ma.enset.utilisateur.service.SheetService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/utilisateurs/sheet")
@AllArgsConstructor
@Validated
public class SheetController {

    private final SheetService service;

    @PostMapping
    public ResponseEntity<List<UtilisateurResponse>> saveFromSheet(@RequestParam("file") MultipartFile file) {
        return ResponseEntity
                .ok()
                .body(service.saveFromSheet(file));
    }


    @GetMapping
    public ResponseEntity<Resource> exportToSheet(@RequestParam("codes") @NotEmpty Set<@NotBlank String> codes) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"utilisateurs-" + java.time.LocalDate.now() + ".xlsx\"")
                .body(
                        service.exportToSheet(codes)
                );
    }

}
