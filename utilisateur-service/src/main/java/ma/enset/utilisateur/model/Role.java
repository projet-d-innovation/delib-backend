package ma.enset.utilisateur.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Role implements Persistable<String> {
    @Id
    private String roleId;
    @Column(nullable = false)
    private String roleName;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<Utilisateur> utilisateurs;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "permission_role",
            joinColumns = @JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "permissionId"))
    private List<Permission> permissions;

    @Transient
    @Setter(AccessLevel.NONE)
    private boolean isNew = true;

    @Override
    public String getId() {
        return roleId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

}
