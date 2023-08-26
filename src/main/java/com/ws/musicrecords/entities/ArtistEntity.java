package com.ws.musicrecords.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "artists")
public class ArtistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;
    @NotBlank
    private String artistName;
    @Size(max = 1000)
    private String description;
    private String country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArtistEntity that = (ArtistEntity) o;
        return getArtistId() != null && Objects.equals(getArtistId(), that.getArtistId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
