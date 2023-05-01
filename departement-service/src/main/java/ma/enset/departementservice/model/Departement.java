package ma.enset.departementservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Departement {
    @Id
    private String codeDepartement;

    @NotBlank
    private String codeChefDepartement;
    @NotBlank
    private String intituleDepartement;

    @Min(1)
    private int nombreEmployes;

    @Min(0)
    private int nombreFilieres;


    @ElementCollection
    private List<String> filieresIds;
    @ElementCollection
    private List<String> usersIds;


}
