package be.vdab.fietsacademy.Domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("I")
public class IndividueleCursus extends Cursus{
    private static final long serialVersionUID = 1L;
    private int duurtijd;

    protected IndividueleCursus(){}

    public IndividueleCursus(int duurtijd) {
        this.duurtijd = duurtijd;
    }

    public int getDuurtijd() {
        return duurtijd;
    }
}
