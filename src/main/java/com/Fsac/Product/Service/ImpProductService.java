package com.Fsac.Product.Service;

import com.Fsac.Product.Entite.Categorie;
import com.Fsac.Product.Entite.Product;
import com.Fsac.Product.Repository.ProductRepository;
import com.Fsac.Product.Repository.categoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImpProductService implements IProductService{

    @Autowired
    ProductRepository productRepository ;
    @Autowired
    categoryRepository categoryrepository ;

    @Override
    public List<Product> AllProducts() {
        return productRepository.findAll() ;
    }

    public void AddProduct(MultipartFile file, String name, String categoryName, double price) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")) {
            throw new IllegalArgumentException("Invalid file name");
        }

        // Recherche ou création de la catégorie
        Categorie category = categoryrepository.findByCategorie(categoryName)
                .orElseGet(() -> {
                    Categorie newCategory = new Categorie();
                    newCategory.setCategorie(categoryName);
                    return categoryrepository.save(newCategory);
                });

        // Création du produit
        Product product = new Product();
        product.setName(name);
        product.setCategorie(category);
        product.setPrice(price);
        product.setImage(Base64.getEncoder().encodeToString(file.getBytes()));

        // Si la liste des produits de la catégorie est vide ou nulle, initialisez-la avec une nouvelle liste
        if (category.getProducts() == null || category.getProducts().isEmpty()) {
            List<Product> products_init = new ArrayList<>();
            products_init.add(product);
            category.setProducts(products_init);
        } else {
            category.getProducts().add(product);
        }

        // Enregistrer la catégorie pour mettre à jour la liste des produits
        categoryrepository.save(category);
    }


    @Override
    public Optional<Product> FindProduct(Long id) {
        Optional<Product> product = productRepository.findById(id) ;
        return  product;
    }

    @Override
    public void DeleteProduct(Long id) {
        Optional<Product> optionalProduct = FindProduct(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            Categorie categorie = product.getCategorie();
            categorie.getProducts().remove(product);
            categoryrepository.save(categorie);
            productRepository.delete(product);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    @Override
    public void EditProduct(Long id, MultipartFile file, String name,
                            String categoryName, double price) throws IOException {

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {

            Product existingProduct = optionalProduct.get();
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            if (fileName.contains("..")) {
                throw new IllegalArgumentException("Invalid file name");
            }
            Categorie currentCategory = existingProduct.getCategorie();

            if (!currentCategory.getCategorie().equals(categoryName)) {
                if (currentCategory.getProducts() != null) {
                    currentCategory.getProducts().remove(existingProduct);
                } else {
                    currentCategory.setProducts(new ArrayList<>());
                }
                categoryrepository.save(currentCategory);

                Categorie newCategory = categoryrepository.findByCategorie(categoryName)
                        .orElseGet(() -> {
                            Categorie category = new Categorie();
                            category.setCategorie(categoryName);
                            return categoryrepository.save(category);
                        });
                if (newCategory.getProducts() == null) {
                    newCategory.setProducts(new ArrayList<>());
                }
                newCategory.getProducts().add(existingProduct);
                existingProduct.setCategorie(newCategory);
                categoryrepository.save(newCategory);
            }

            existingProduct.setName(name);
            existingProduct.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
            existingProduct.setPrice(price);


            productRepository.save(existingProduct);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    @Override
    public List<Categorie> AllCategories() {
        return categoryrepository.findAll();
    }

    @Override
    public List<Product> AllProductsOfCategory(String category) {
        Categorie categorie = categoryrepository.findByCategorie(category).orElse(null);
        if (categorie != null) {
            List<Product> products = AllProducts();
            return products.stream()
                    .filter(p -> p.getCategorie().getCategorie().equals(category))
                    .collect(Collectors.toList());
        } else {
            return productRepository.findAll();
        }
    }

    @Override
    public void AddCategory(String categorie) {
        Optional<Categorie> existingCategory = categoryrepository.findByCategorie(categorie);
        if (existingCategory.isPresent()) {
            throw new RuntimeException("The category already exists.");
        } else {

            Categorie newCategory = new Categorie();
            newCategory.setCategorie(categorie);
            categoryrepository.save(newCategory);
        }
    }


}

