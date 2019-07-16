package be.vdab.fietsacademy.Domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;
@Entity
@DiscriminatorValue("G")
public class GroepsCursus extends Cursus{
    private static final long serialVersionUID= 1L;
    private LocalDate van;
    private LocalDate tot;

    protected GroepsCursus(){}

    public GroepsCursus(LocalDate van, LocalDate tot) {
        this.van = van;
        this.tot = tot;
    }

    public LocalDate getVan() {
        return van;
    }

    public LocalDate getTot() {
        return tot;
    }
}
