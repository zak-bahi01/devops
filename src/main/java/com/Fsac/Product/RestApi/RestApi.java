 package com.Fsac.Product.RestApi;

import com.Fsac.Product.Entite.Categorie;
import com.Fsac.Product.Entite.Product;
import com.Fsac.Product.Service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class RestApi {

    @Autowired
    IProductService iProductService ;

    @GetMapping("/products")
    public String getAllProducts(@RequestParam(required = false) String categorie, Model model) {
        List<Product> products;
        if (categorie != null) {
            products = iProductService.AllProductsOfCategory(categorie);
        } else {
            products = iProductService.AllProducts();
        }
        List<Categorie> categories = iProductService.AllCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "product-list";
    }


    @PostMapping("/addproduct/add")
    public String addProduct(@RequestParam MultipartFile file, @RequestParam String name ,
                             @RequestParam  String categorie ,@RequestParam double price , RedirectAttributes redirectAttributes) throws IOException {
        iProductService.AddProduct(file,name,categorie,price);
        redirectAttributes.addFlashAttribute("successMessage", "Product added successfully.");
        return "redirect:/products";
    }
    @PostMapping("/products/category")
    public String ProductOfCategory(@RequestParam String category , RedirectAttributes redirectAttributes){
        List<Product> products = iProductService.AllProductsOfCategory(category) ;
        redirectAttributes.addAttribute("categorie", category);
        return "redirect:/products";
    }

    @GetMapping("/addproduct")
    public String GetAddProductform(){
        return "add-product";
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = iProductService.FindProduct(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return ResponseEntity.ok(product);
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        iProductService.DeleteProduct(id);
        return "redirect:/products";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id,
                            @RequestParam MultipartFile file,
                            @RequestParam String name,
                            @RequestParam String categorie,
                            @RequestParam double price , RedirectAttributes redirectAttributes) throws IOException {
        iProductService.EditProduct(id, file, name, categorie, price);
        redirectAttributes.addFlashAttribute("successMessage", "Product have been successfully.");

        return "redirect:/products";

    }

    @GetMapping("/products/edit/{id}")
    public String getEditForm(@PathVariable Long id, Model model) {
        Product product = iProductService.FindProduct(id).orElse(null); // Utilisez orElse(null) pour éviter le retour de Optional vide
        if (product == null) {
            return "error";
        }
        model.addAttribute("product", product);
        return "edit-product";
    }

   @GetMapping("/addCategory")
   String getAddCategoryForm(){
        return "add-category";
   }

    @PostMapping("/categories/add")
    String AddCategory(@RequestParam String categoryName, RedirectAttributes redirectAttributes){
        try {
            iProductService.AddCategory(categoryName);
            redirectAttributes.addFlashAttribute("successMessage", "Category added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add category: " + e.getMessage());
        }
        return "redirect:/products";
    }

}