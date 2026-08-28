package com.tech.agendaai.company.model.company;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class CompanyPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;
    @Column(name = "photo_url")
    private String url;
    @Column(name = "photo_name")
    private String fileName;
    @Column(name = "photo_size")
    private int size;
    @Column(name = "photo_mime_type")
    private String mimeType;

    @OneToOne(mappedBy = "photo")
    private Company company;
}