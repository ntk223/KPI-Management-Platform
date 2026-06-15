package vdt.kpimanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "account_roles")
@Getter
@Setter
public class AccountRole {

    @EmbeddedId
    private AccountRoleId id = new AccountRoleId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Embeddable
    @Getter
    @Setter
    public static class AccountRoleId implements Serializable {

        @Column(name = "account_id")
        private Long accountId;

        @Column(name = "role_id")
        private Long roleId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AccountRoleId that)) return false;
            return Objects.equals(accountId, that.accountId) && Objects.equals(roleId, that.roleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountId, roleId);
        }
    }
}
