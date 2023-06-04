package ma.enset.utilisateur.service;

import ma.enset.utilisateur.dto.utilisateur.UtilisateurResponse;
import ma.enset.utilisateur.exception.InvalidSheetException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface SheetService {
    List<UtilisateurResponse> saveFromSheet(MultipartFile file) throws InvalidSheetException;

    InputStreamResource exportToSheet(Set<String> codes);
}
