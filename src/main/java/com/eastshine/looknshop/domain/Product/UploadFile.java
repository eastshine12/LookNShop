package com.eastshine.looknshop.domain.Product;

import javax.persistence.*;

@Entity
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_file_id")
    private Long id;
}
