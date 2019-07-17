package be.vdab.fietsacademy.repositories;


import be.vdab.fietsacademy.Domain.Adres;
import be.vdab.fietsacademy.Domain.Campus;
import be.vdab.fietsacademy.Domain.Docent;
import be.vdab.fietsacademy.Domain.Geslacht;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/insertCampus.sql")
@Sql("/insertDocent.sql")
@Import(JpaDocentRepository.class)
public class JpaDocentRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    private Docent docent;
    private Campus campus;

    @Before
    public void before() {
        campus = new Campus("test", new Adres("test", "test", "test", "test"));
        docent = new Docent("test", "test", BigDecimal.TEN, "test@fietsacademy.be", Geslacht.MAN, campus);
    }

    @Autowired
    private DocentRepository repository;
    @Autowired
    private EntityManager entityManager;

    private long idVanTestMan() {
        return super.jdbcTemplate.queryForObject("select id from docenten where voornaam = 'testM'", Long.class);
    }

    private long idVanTestVrouw() {
        return super.jdbcTemplate.queryForObject("select id from docenten where voornaam = 'testV'", Long.class);

    }

    @Test
    public void findById() {
        assertThat(repository.findById(idVanTestMan()).get().getVoornaam()).isEqualTo("testM");
    }

    @Test
    public void findByOnbestaandeId() {
        assertThat(repository.findById(-2)).isNotPresent();
    }

    @Test
    public void man() {
        assertThat(repository.findById(idVanTestMan()).get().getGeslacht()).isEqualTo(Geslacht.MAN);
    }

    @Test
    public void vrouw() {
        assertThat(repository.findById(idVanTestVrouw()).get().getGeslacht()).isEqualTo(Geslacht.VROUW);
    }

    @Test
    public void create() {
        entityManager.persist(campus);
        repository.create(docent);
        entityManager.flush();
        assertThat(docent.getId()).isPositive();
        assertThat(super.countRowsInTableWhere("docenten", "id=" + docent.getId())).isOne();
        assertThat(super.jdbcTemplate.queryForObject("select campusid from docenten where id=?", Long.class, docent.getId()))
                .isEqualTo(campus.getId());
        assertThat(campus.getDocenten()).contains(docent);
    }

    @Test
    public void delete() {
        long id = idVanTestMan();
        repository.delete(id);
        entityManager.flush();
        assertThat(super.countRowsInTableWhere("docenten", "id=" + id)).isZero();
    }

    @Test
    public void findAll() {
        assertThat(repository.findAll()).hasSize(super.countRowsInTable("docenten")).extracting(Docent::getWedde).isSorted();
    }

    @Test
    public void findByWeddeBetween() {
        BigDecimal duizend = BigDecimal.valueOf(1000);
        BigDecimal tweeduizend = BigDecimal.valueOf(2000);
        List<Docent> docenten = repository.findByWeddeBetween(duizend, tweeduizend);
        assertThat(docenten).hasSize(super.countRowsInTableWhere("docenten", "wedde between 1000 AND 2000"))
                .allSatisfy(docent -> assertThat(docent.getWedde()).isBetween(duizend, tweeduizend));
    }

    @Test
    public void findEmailAdressen() {
        assertThat(repository.findEmailAdressen())
                .hasSize(super.jdbcTemplate.queryForObject("select count(distinct emailAdres) from docenten", Integer.class))
                .allSatisfy(adres -> assertThat(adres).contains("@"));
    }

    @Test
    public void findIdsEnEmailAdressen() {
        assertThat(repository.findIdsEnEmailAdressen()).hasSize(super.countRowsInTable("docenten"));
    }

    @Test
    public void findGrootsteWedde() {
        assertThat(repository.findGrootsteWedde())
                .isEqualByComparingTo(super.jdbcTemplate.queryForObject("select max(wedde) from docenten", BigDecimal.class));
    }

    @Test
    public void findAantalDocentenPerWedde() {
        BigDecimal duizend = BigDecimal.valueOf(1000);
        assertThat(repository.findAantalDocentenPerWedde()).hasSize(super.jdbcTemplate.
                queryForObject("select count(distinct wedde) from docenten", Integer.class))
                .filteredOn(aantalPerWedde -> aantalPerWedde.getWedde().compareTo(duizend) == 0)
                .allSatisfy(aantalPerWeddde -> assertThat(aantalPerWeddde.getAantal())
                        .isEqualTo(super.countRowsInTableWhere("docenten", "wedde= 1000")));
    }

    @Test
    public void algemeneOpslag() {
        assertThat(repository.algemeneOpslag(BigDecimal.TEN)).isEqualTo(super.countRowsInTable("docenten"));
        assertThat(super.jdbcTemplate.queryForObject("select wedde from docenten where id = ?", BigDecimal.class, idVanTestMan()))
                .isEqualByComparingTo("1100");
    }

    @Test
    public void bijnamenLezen() {
        assertThat(repository.findById(idVanTestMan()).get().getBijnamen()).containsOnly("test");
    }

    @Test
    public void bijnaamToevoegen() {
        repository.create(docent);
        docent.addBijnaam("test");
        entityManager.flush();
        assertThat(super.jdbcTemplate.queryForObject("select bijnaam from docentenbijnamen where docentid=?", String.class, docent.getId())).isEqualTo("test");
    }

    @Test
    public void campusLazyLoaded() {
        Docent docent = repository.findById(idVanTestMan()).get();
        assertThat(docent.getCampus().getNaam()).isEqualTo("test");
    }


}
