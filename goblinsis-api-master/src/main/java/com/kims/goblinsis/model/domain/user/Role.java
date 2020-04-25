package com.kims.goblinsis.model.domain.user;

import com.google.common.base.Objects;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "ROLE")
@Data
public class Role implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @NotNull
    @NotEmpty
    @Size(max = 20)
    @Column(name = "rolename", length = 20)
    private String rolename;

    @Override
    public String toString() {
        return String.format("%s(id=%d, rolename='%s')",
                this.getClass().getSimpleName(),
                this.getId(), this.getRolename());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj instanceof Role) {
            final Role other = (Role) obj;
            return Objects.equal(getId(), other.getId())
                    && Objects.equal(getRolename(), other.getRolename());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getRolename());
    }
}
