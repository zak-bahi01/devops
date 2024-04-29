package com.Fsac.Product.Service;

import com.Fsac.Product.Entite.Categorie;
import com.Fsac.Product.Entite.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<Product> AllProducts() ;
    void AddProduct(MultipartFile file , String name , String categorie , double price) throws IOException;
    Optional<Product> FindProduct(Long id);
    void DeleteProduct(Long id);
    void EditProduct(Long id, MultipartFile file, String name, String categorie, double price) throws IOException;
    List<Categorie> AllCategories() ;
    List<Product> AllProductsOfCategory(String category);
    void AddCategory(String categorie);
}
