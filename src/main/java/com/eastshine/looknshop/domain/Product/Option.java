package com.eastshine.looknshop.domain.Product;

import javax.persistence.*;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "option_category_id")
    private OptionCategory category;
}
