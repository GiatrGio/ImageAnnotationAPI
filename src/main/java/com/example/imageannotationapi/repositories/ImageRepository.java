package com.example.imageannotationapi.repositories;

import com.example.imageannotationapi.models.database.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i LEFT JOIN FETCH i.annotations LEFT JOIN FETCH i.user WHERE i.id = :id")
    Optional<Image> findByImageId(Long id);

    @Modifying
    @Query(value = "INSERT INTO images (user_id, url, status) VALUES (:userId, :url, 'PROCESSING')", nativeQuery = true)
    void addNewImage(Long userId, String url);

    @Query(value = "SELECT LAST_INSERT_ID()")
    Long getLastInsertedId();

    List<Image> findAllByUserId(Long userId);

    @Query("SELECT i FROM Image i LEFT JOIN FETCH i.annotations WHERE i.id = :imageId AND i.user.id = :userId")
    Optional<Image> findImageDetailsByUserIdAndImageId(Long userId, Long imageId);

}
