package ma.enset.utilisateur.service;

import lombok.AllArgsConstructor;
import ma.enset.utilisateur.dto.IncludeParams;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurCreateRequest;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurResponse;
import ma.enset.utilisateur.exception.InternalErrorException;
import ma.enset.utilisateur.exception.InvalidSheetException;
import ma.enset.utilisateur.util.SheetHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SheetServiceImpl implements SheetService {
    private final UtilisateurService utilisateurService;
    private final SheetHelper sheetHelper;

    @Override
    public List<UtilisateurResponse> saveFromSheet(MultipartFile file) throws InvalidSheetException {
        List<UtilisateurCreateRequest> utilisateurs = sheetHelper.readExcel(file);
        return utilisateurService.saveAll(utilisateurs);
    }

    @Override
    public InputStreamResource exportToSheet(Set<String> codes) {

        List<UtilisateurResponse> utilisateurs = utilisateurService.findAllById(
                codes,
                IncludeParams.builder().includeRoles(true).build()
        );

        try {
            Workbook workbook = sheetHelper.writeExcel(utilisateurs);
            File file = File.createTempFile("utilisateurs", ".xlsx");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            return new InputStreamResource(new FileInputStream(file));
        } catch (IOException e) {
            throw new InternalErrorException("fail to create Excel file: " + e.getMessage());
        }
    }

}
