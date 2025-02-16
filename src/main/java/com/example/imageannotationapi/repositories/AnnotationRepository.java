package com.example.imageannotationapi.repositories;

import com.example.imageannotationapi.models.database.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AnnotationRepository  extends JpaRepository<Annotation, Long> {

    Optional<Annotation> findByName(String annotation);

    @Query("SELECT a.name FROM Annotation a")
    List<String> findAllNames();

}
