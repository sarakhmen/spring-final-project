package com.sarakhman.onlineStore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;

    @Column(name = "product_name")
    @NotEmpty(message = "*Please provide a product name")
    private String productName;

    @Column(name = "price")
    @NotEmpty(message = "*Please provide a price")
    private double price;

    @Temporal(TemporalType.DATE)
    @Column(name = "creation_date")
    private Date creationDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Set<Property> properties;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Set<Order> orders;
}
