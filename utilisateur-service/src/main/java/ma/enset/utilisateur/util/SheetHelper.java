package ma.enset.utilisateur.util;

import ma.enset.utilisateur.constant.CoreConstants;
import ma.enset.utilisateur.dto.role.RoleResponse;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurCreateRequest;
import ma.enset.utilisateur.dto.utilisateur.UtilisateurResponse;
import ma.enset.utilisateur.exception.InternalErrorException;
import ma.enset.utilisateur.exception.InvalidSheetException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Service
public class SheetHelper {
    private final String[] headers = {"CODE", "CIN", "CNE", "NOM", "PRENOM", "TELEPHONE", "DATE NAISSANCE", "ADDRESS", "VILLE", "PAYS", "SEXE", "ROLES"};

    public List<UtilisateurCreateRequest> readExcel(MultipartFile file) throws InvalidSheetException {

        checkExcelFormat(file);

        Workbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            throw new InternalErrorException("fail to parse Excel file: " + e.getMessage());
        }

        List<UtilisateurCreateRequest> utilisateurs = new ArrayList<>();

        for (Sheet sheet : workbook) {
            readSheet(sheet, utilisateurs);
        }

        return utilisateurs;
    }

    public Workbook writeExcel(List<UtilisateurResponse> utilisateurResponses) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Utilisateurs");
        writeHeader(sheet, sheet.createRow(0));
        writeBody(sheet, utilisateurResponses);
        autoFitColums(sheet);
        return workbook;
    }

    private void writeBody(Sheet sheet, List<UtilisateurResponse> utilisateurResponses) {
        int rowIndex = 1;

        CellStyle style = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setFont(font);

        for (UtilisateurResponse utilisateurResponse : utilisateurResponses) {
            Row row = sheet.createRow(rowIndex);

            writeCell(row, 0, utilisateurResponse.getCode(), style);
            writeCell(row, 1, utilisateurResponse.getCin(), style);
            writeCell(row, 2, utilisateurResponse.getCne(), style);
            writeCell(row, 3, utilisateurResponse.getNom(), style);
            writeCell(row, 4, utilisateurResponse.getPrenom(), style);
            writeCell(row, 5, utilisateurResponse.getTelephone(), style);
            writeCell(row, 6, utilisateurResponse.getDateNaissance(), style);
            writeCell(row, 7, utilisateurResponse.getAdresse(), style);
            writeCell(row, 8, utilisateurResponse.getVille(), style);
            writeCell(row, 9, utilisateurResponse.getPays(), style);
            writeCell(row, 10, utilisateurResponse.getSexe(), style);
            writeCell(row, 11, utilisateurResponse.getRoles(), style);

            rowIndex++;
        }
    }

    private void alternativeStyling(int rowIndex, CellStyle style) {

    }

    private void writeHeader(Sheet sheet, Row row) {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        row.setHeight((short) (1.3 * row.getHeight()));
        for (int i = 0; i < headers.length; i++) {
            writeCell(row, i, headers[i], style);
        }
    }

    private void autoFitColums(Sheet sheet) {
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void writeCell(Row row, int cellIndex, Object value, CellStyle style) {
        Cell cell = row.createCell(cellIndex);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue(((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter));
        } else if (value instanceof ArrayList) {
            StringBuilder sb = new StringBuilder();
            List<RoleResponse> roles = (ArrayList<RoleResponse>) value;
            for (int i = 0; i < roles.size(); i++) {
                sb.append(roles.get(i).getRoleName());
                if (i != roles.size() - 1) {
                    sb.append(", ");
                }
            }
            cell.setCellValue(sb.toString());
        } else if (value instanceof LocalDate) {
            cell.setCellValue(((LocalDate) value).format(formatter));
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }


    private int getColumnsLength(Row row) {
        int length = 0;
        for (Cell cell : row) {
            if (cell.getStringCellValue().equals("")) {
                break;
            }
            length++;
        }
        return length;
    }

    private void readSheet(Sheet sheet, List<UtilisateurCreateRequest> utilisateurs) {
        Iterator<Row> rows = sheet.iterator();

        Function<Row, UtilisateurCreateRequest> rowReader;
        if (getColumnsLength(rows.next()) == 18) {
            rowReader = this::readRowApogee;
        } else if (getColumnsLength(rows.next()) == 10) {
            rowReader = this::readRow;
        } else {
            throw new InvalidSheetException(
                    CoreConstants.BusinessExceptionMessage.INVALID_SHEET,
                    new Object[]{"Sheet must have either 10 or 18 columns"}
            );
        }

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            utilisateurs.add(
                    rowReader.apply(currentRow)
            );
        }
    }

    private UtilisateurCreateRequest readRowApogee(Row row) {
        StringBuilder adresse = new StringBuilder();
        adresse.append(getCellValue(row.getCell(13)));
        if (getCellValue(row.getCell(14)) != null && !getCellValue(row.getCell(14)).equals("")) {
            adresse.append(", ").append(getCellValue(row.getCell(14)));
        }
        if (getCellValue(row.getCell(15)) != null && !getCellValue(row.getCell(15)).equals("")) {
            adresse.append(", ").append(getCellValue(row.getCell(15)));
        }
        return UtilisateurCreateRequest.builder()
                .code(getCellValue(row.getCell(0)))
                .cne(getCellValue(row.getCell(1)))
                .cin(getCellValue(row.getCell(2)))
                .prenom(getCellValue(row.getCell(3)))
                .nom(getCellValue(row.getCell(4)))
                .sexe(getCellValue(row.getCell(11)))
                .dateNaissance(LocalDate.parse(getCellValue(row.getCell(12)), DateTimeFormatter.ofPattern("dd/MM/uuuu")))
                .adresse(adresse.toString())
                .ville(getCellValue(row.getCell(18)))
                .build();
    }

    private UtilisateurCreateRequest readRow(Row row) {
        return UtilisateurCreateRequest.builder()
                .code(getCellValue(row.getCell(0)))
                .cne(getCellValue(row.getCell(1)))
                .cin(getCellValue(row.getCell(2)))
                .prenom(getCellValue(row.getCell(3)))
                .nom(getCellValue(row.getCell(4)))
                .sexe(getCellValue(row.getCell(11)))
                .dateNaissance(LocalDate.parse(getCellValue(row.getCell(12)), DateTimeFormatter.ofPattern("dd/MM/uuuu")))
                .ville(getCellValue(row.getCell(18)))
                .build();
    }


    private String getCellValue(Cell cell) {

        if (cell == null) {
            return null;
        }
        if (cell.getColumnIndex() == 0) {
            cell.setCellType(CellType.STRING);
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return null;
    }

    private void checkExcelFormat(MultipartFile file) throws InvalidSheetException {
        String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        if (!TYPE.equals(file.getContentType())) {
            throw new InvalidSheetException(
                    CoreConstants.BusinessExceptionMessage.INVALID_SHEET,
                    new Object[]{"must be an Office Open XML file"}
            );
        }
    }

}
