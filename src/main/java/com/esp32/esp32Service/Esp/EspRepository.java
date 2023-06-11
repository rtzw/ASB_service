package com.esp32.esp32Service.Esp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface EspRepository extends JpaRepository<Esp, String> {

    Optional<Esp> getEspById(String id);
    @Query(value = "select e from Esp e")
    ArrayList<Esp> getAll();

}
