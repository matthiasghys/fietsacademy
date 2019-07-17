package be.vdab.fietsacademy.repositories;

import be.vdab.fietsacademy.Domain.Cursus;

import java.util.Optional;

public interface CursusRepository {
    Optional<Cursus> findById(String id);
    void create(Cursus cursus);
}
