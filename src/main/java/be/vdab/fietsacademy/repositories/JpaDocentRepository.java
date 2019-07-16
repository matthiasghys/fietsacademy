package be.vdab.fietsacademy.repositories;

import be.vdab.fietsacademy.Domain.Docent;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;
@Repository
public class JpaDocentRepository implements DocentRepository {
    private final EntityManager manager;

    public JpaDocentRepository(EntityManager manager) {
        this.manager = manager;

    }


    @Override
    public Optional<Docent> findById(long id) {
        return Optional.ofNullable(manager.find(Docent.class, id));
    }
}
