package ma.enset.filiereservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Filiere {
    @Id
    private String codeFiliere;
    private String codeRegle;
    private String codeDepartement;
    private String intituleFiliere;
}
