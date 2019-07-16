package be.vdab.fietsacademy.Domain;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name= "cursussen")
@DiscriminatorColumn(name = "soort")
public abstract class Cursus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;

    protected Cursus() {

    }

    private Cursus(long id, String naam) {
        this.id = id;
        this.naam = naam;
    }


    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }
}
