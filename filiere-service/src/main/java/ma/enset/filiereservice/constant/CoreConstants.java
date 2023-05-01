package ma.enset.filiereservice.constant;

public class CoreConstants {
    public static class BusinessExceptionMessage {
        public static final String INTERNAL_ERROR = "businessException.InternalError.message";
        public static final String FILIERE_NOT_FOUND = "businessException.filiere.NotFound.message";
        public static final String FILIERE_ALREADY_EXISTS = "businessException.filiere.AlreadyExists.message";
        public static final String REGLE_DE_CALCUL_NOT_FOUND = "businessException.regle.NotFound.message";

        public static final String SEMESTRE_NOT_FOUND = "businessException.semestre.NotFound.message";
        public static final String SEMESTRE_ALREADY_EXISTS = "businessException.semestre.AlreadyExists.message";

        public static final String ANNEE_UNIVERSITAIRE_NOT_FOUND = "businessException.anneeUniversitaire.NotFound.message";
        public static final String ANNEE_UNIVERSITAIRE_ALREADY_EXISTS = "businessException.anneeUniversitaire.AlreadyExists.message";
        public static final String REGLE_DE_CALCUL_ALREADY_EXISTS = "businessException.regle.AlreadyExists.message";

        public static final String DEPARTEMENT_NOT_FOUND = "businessException.departement.NotFound.message";

        public static final String USER_NOT_FOUND = "businessException.user.NotFound.message";
    }
}
