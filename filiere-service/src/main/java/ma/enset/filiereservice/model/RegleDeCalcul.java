package ma.enset.filiereservice.model;

import jakarta.persistence.*;
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
public class RegleDeCalcul {
    @Id
    private String codeRegle;



    private float noteValidationModule ;
    private float noteEliminatoireModule ;
    private float noteCompensationModule ;
    private float noteValidationAnnee;
    private int nbrModulesDerogation ;

    @OneToMany(mappedBy = "regleDeCalcul" ,fetch = FetchType.LAZY ,targetEntity = Filiere.class)
    private List<Filiere> filieres;



}
