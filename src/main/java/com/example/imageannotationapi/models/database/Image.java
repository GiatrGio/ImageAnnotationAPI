package com.example.imageannotationapi.models.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "images")
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "image_annotations",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "annotation_id")
    )
    private Set<Annotation> annotations;

}
