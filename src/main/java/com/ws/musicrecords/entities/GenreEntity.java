package com.ws.musicrecords.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "genres")
public class GenreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreId;
    private String genreName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GenreEntity that = (GenreEntity) o;
        return getGenreId() != null && Objects.equals(getGenreId(), that.getGenreId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
