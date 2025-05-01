package cgb.transfert.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCGB {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String username;
    private String password;

    // Client géré par un seul utilisateur
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference("customer_users")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "rolecgb_id", nullable = false)
    @JsonBackReference("rolecgb_users")
    private RoleCGB roleCGB;
}
