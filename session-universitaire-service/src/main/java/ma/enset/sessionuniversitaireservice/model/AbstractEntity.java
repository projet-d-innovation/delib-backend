package ma.enset.sessionuniversitaireservice.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@MappedSuperclass
public abstract class AbstractEntity<ID> implements Persistable<ID> {

    @Transient
    @Setter(AccessLevel.NONE)
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    protected void prePersist() {
        markNotNew();
    }

    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

}
