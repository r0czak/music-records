package com.ws.musicrecords.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "albums")
public class AlbumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumId;

    @ManyToOne
    @JoinColumn(name = "artistId")
    private ArtistEntity artist;

    @ManyToOne
    @JoinColumn(name = "genreId")
    private GenreEntity genre;

    private String albumName;
    @DateTimeFormat
    private LocalDate releaseDate;
    private String albumCover;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AlbumEntity that = (AlbumEntity) o;
        return getAlbumId() != null && Objects.equals(getAlbumId(), that.getAlbumId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
