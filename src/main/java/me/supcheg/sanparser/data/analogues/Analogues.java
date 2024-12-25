package me.supcheg.sanparser.data.analogues;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.supcheg.sanparser.data.type.SantechIdentifierArrayUserType;
import me.supcheg.sanparser.data.type.SantechIdentifierType;
import me.supcheg.sanparser.id.SantechIdentifier;
import org.hibernate.annotations.Type;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Analogues {
    @Id
    @Type(SantechIdentifierType.class)
    private SantechIdentifier root;

    @Type(SantechIdentifierArrayUserType.class)
    private SantechIdentifier[] analogues;
}
