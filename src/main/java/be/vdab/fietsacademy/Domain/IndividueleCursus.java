package be.vdab.fietsacademy.Domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="individuelecursussen")
public class IndividueleCursus extends Cursus{
    private static final long serialVersionUID = 1L;
    private int duurtijd;

    protected IndividueleCursus(){}

    public IndividueleCursus(String naam, int duurtijd) {
        super(naam);
        this.duurtijd = duurtijd;
    }

    public int getDuurtijd() {
        return duurtijd;
    }
}
