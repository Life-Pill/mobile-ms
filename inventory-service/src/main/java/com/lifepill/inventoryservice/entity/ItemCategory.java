package com.lifepill.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * ItemCategory entity for categorizing items
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item_category")
public class ItemCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;
    
    @Column(name = "category_name", length = 100, nullable = false)
    private String categoryName;
    
    @Column(name = "category_description", length = 255)
    private String categoryDescription;
    
    @Column(name = "category_image", length = 500)
    private String categoryImage;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemCategory", fetch = FetchType.LAZY)
    private Set<Item> items = new HashSet<>();
}
