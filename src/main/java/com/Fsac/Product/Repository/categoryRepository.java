package com.Fsac.Product.Repository;

import com.Fsac.Product.Entite.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface categoryRepository extends JpaRepository<Categorie, Long> {
     Optional<Categorie> findByCategorie(String categoryName);
}
