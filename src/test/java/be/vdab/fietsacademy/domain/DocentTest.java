package be.vdab.fietsacademy.domain;

import be.vdab.fietsacademy.Domain.Adres;
import be.vdab.fietsacademy.Domain.Campus;
import be.vdab.fietsacademy.Domain.Docent;
import be.vdab.fietsacademy.Domain.Geslacht;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class DocentTest {
    private final static BigDecimal WEDDE = BigDecimal.valueOf(200);
    private Docent docent1;
    private Docent docent2;
    private Campus campus1;
    private Docent nogEensDocent1;
    private Campus campus2;


    @Before
    public void before() {
        campus1 = new Campus("test", new Adres("test", "test", "test", "test"));
        campus2 = new Campus("test2", new Adres("test2", "test2", "test2", "test2"));
        docent1 = new Docent("test", "test", WEDDE, "test@fietsacademy.be", Geslacht.MAN, campus1);
        nogEensDocent1 = new Docent("test", "test", WEDDE, "test@fietsacademy.be", Geslacht.MAN, campus1);
        docent2 = new Docent("test2", "test", WEDDE, "test2@fietsacademy.be", Geslacht.MAN, campus1);

    }

    @Test
    public void opslag() {
        docent1.opslag(BigDecimal.TEN);
        assertThat(docent1.getWedde()).isEqualByComparingTo("2200");
    }

    @Test
    public void opslagMetNullIsMislukt() {
        assertThatNullPointerException().isThrownBy(() -> docent1.opslag(null));
    }

    @Test
    public void opslagMet0IsMislukt() {
        assertThatIllegalArgumentException().isThrownBy(() -> docent1.opslag(BigDecimal.ZERO));
    }

    @Test
    public void negatieveOpslagMislukt() {
        assertThatIllegalArgumentException().isThrownBy(() -> docent1.opslag(BigDecimal.valueOf(-1)));
    }

    @Test
    public void eenNieuweDocentHeeftGeenBijnamen() {
        assertThat(docent1.getBijnamen()).isEmpty();
    }

    @Test
    public void bijnaamToevoegen() {
        assertThat(docent1.addBijnaam("test")).isTrue();
        assertThat(docent1.getBijnamen()).containsOnly("test");
    }

    @Test
    public void tweeKeerDezelfdeBijnaamMislukt() {
        docent1.addBijnaam("test");
        assertThat(docent1.addBijnaam("test")).isFalse();
        assertThat(docent1.getBijnamen()).containsOnly("test");
    }

    @Test
    public void nullAlsBijnaamMislukt() {
        assertThatNullPointerException().isThrownBy(() -> docent1.addBijnaam(null));
    }

    @Test
    public void eenLegeBijnaamMislukt() {
        assertThatIllegalArgumentException().isThrownBy(() -> docent1.addBijnaam(""));
    }

    @Test
    public void eenBijnaamMetEnkelSpatiesMislukt() {
        assertThatIllegalArgumentException().isThrownBy(() -> docent1.addBijnaam(" "));
    }

    @Test
    public void bijnaamVerwijderen() {
        docent1.addBijnaam("test");
        assertThat(docent1.removeBijnaam("test")).isTrue();
        assertThat(docent1.getBijnamen()).isEmpty();
    }

    @Test
    public void eenBijnaamVerwijderenDieJeNietToevoegdeMislukt() {
        docent1.addBijnaam("test");
        assertThat(docent1.removeBijnaam("test2")).isFalse();
        assertThat(docent1.getBijnamen()).containsOnly("test");
    }

    @Test
    public void meerdereDocentenKunnenTotDezelfdeCampusBehoren() {
        assertThat(campus1.getDocenten()).containsOnly(docent1, docent2);

    }

    @Test
    public void docentenZijnGelijkAlsHunEmailAdressenGelijkZijn() {
        assertThat(docent1).isEqualTo(nogEensDocent1);
    }

    @Test
    public void docentenZijnVerschillendAlsHunEmailAdressenVerschillen() {
        assertThat(docent1).isNotEqualTo(docent2);
    }

    @Test
    public void eenDocentVerschiltVanNull() {
        assertThat(docent1).isNotEqualTo(null);
    }

    @Test
    public void eenDocentVerschiltVanEenAnderTypeObject() {
        assertThat(docent1).isNotEqualTo("");
    }

    @Test
    public void gelijkeDocentenGevenDezelfdeHashCode() {
        assertThat(docent1).hasSameHashCodeAs(nogEensDocent1);
    }

   @Test
    public void eenNullCampusInDeConstructorMislukt() {
        assertThatNullPointerException().isThrownBy(() -> new Docent
                ("test", "test", WEDDE, "test@fietsacademy.be", Geslacht.MAN, null));

    }

    @Test
    public void docent1KomtVoorInCampus1(){
        assertThat(docent1.getCampus()).isEqualTo(campus1);
        assertThat(campus1.getDocenten()).contains(docent1);
    }

    @Test
    public void docent1VerhuistNaarCampus2(){
        docent1.setCampus(campus2);
        assertThat(docent1.getCampus()).isEqualTo(campus2);
        assertThat(campus1.getDocenten()).doesNotContain(docent1);
        assertThat(campus2.getDocenten()).contains(docent1);
    }

    @Test
    public void eenNullCampusInDeSetterMislukt(){
        assertThatNullPointerException().isThrownBy(()->docent1.setCampus(null));
    }


}
