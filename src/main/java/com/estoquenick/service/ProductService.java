//Fair warning, this Service class is REALLY loaded, and it's where I commented most of my thought process and learning
//I apologize if it's a hard read lol

package com.estoquenick.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired; //haha beans (I'll get to autowired in a second)
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.estoquenick.dto.PriceListResponse;
import com.estoquenick.dto.LowStockResponse;
import com.estoquenick.dto.BalanceReportResponse;
import com.estoquenick.dto.BalanceReportItemResponse;
import com.estoquenick.dto.CategoryProductCountResponse;

import com.estoquenick.dto.ProductRequest;
import com.estoquenick.dto.ProductResponse;
import com.estoquenick.repository.ProductRepo;
import com.estoquenick.repository.CategoryRepo;
import com.estoquenick.model.Product;
import com.estoquenick.model.Category;

import java.util.List;

//@Service is for creating a "bean", which is an object of this class, and lets it be injected somewhere else i think
//Also it's important for connecting Repository and Controller
@Service
public class ProductService {

    //Autowired is to automatically inject an instance of an object, I think, in this case productRepository, cuz ProductRepo is actually also a Spring Bean (that's a funny name)
    //Basically I only type in the structure, let Spring do all the work and then inject the bean in here with Autowired :3
    @Autowired 
    private ProductRepo productRepository;

    @Autowired
    private CategoryRepo categoryRepository;

    //list everything
    public List<ProductResponse> findAll() {
        return productRepository.findAll() //note this returns a list i think
            .stream() //convert into usable stuff 
            .map(this::toResponse) //map it properly, but not before converting it to dto
            .toList(); //now make a list out of it
    }

    //search using id
    public ProductResponse findById(Long id) { //findById is likely something spring added automatically, since it wasn't me lol
        Product product = productRepository.findById(id) //attempt to find the desired product using its id
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return toResponse(product); //if found, return product in dto form
    }

    // public Product save(Product product) {
    //     //here are all the failsafes being put in place
    //     //obsolete now that we're using dto, value limits are already in place
    //    
    //     //needs a name:
    //     if (product.getName() == null || product.getName().isBlank()) {
    //         throw new RuntimeException("Product needs a name");
    //     }
    //     //price has to be at least more than nothing lol
    //     if (product.getPrice() <= 0 || product.getPrice() == null) {
    //         throw new RuntimeException("Price can't be zero or negative");
    //     }
    //     //stock can't be negative
    //     if (product.getCurrentStock() == null || product.getCurrentStock() < 0) {
    //         throw new RuntimeException("Stock can't be negative");
    //     }
    //     //category must be inserted
    //     if (product.getCategory() == null || product.getCategory().getId() == null) {
    //         throw new RuntimeException("Valid category required");
    //     }
    //     //category inserted must actually exist lmao, we're checking the databank for this one
    //     categoryRepository.findById(product.getCategory().getId()) //check find the id of the current category
    //         .orElseThrow(() -> new RuntimeException("Category does not exist"));
    //
    //     return productRepository.save(product); //returns the saved object? this deals with both Inserting AND Updating so that's pretty darn cool
    // }

    //CREATE the product, wow i put it in all caps this time!
    public ProductResponse save(ProductRequest dto) {

        Category category = categoryRepository.findById(dto.categoryId())  //check the category id to see if it exists
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        Product p = new Product(); //set the current product's properties to the dto we have on hand (plus the category)
        p.setName(dto.name());
        p.setPrice(dto.price());
        p.setCurrentStock(dto.currentStock());
        p.setCategory(category);
        p.setUnit(dto.unit());
        p.setMinStock(dto.minStock());
        p.setMaxStock(dto.maxStock());

        productRepository.save(p); //and then save it
        return toResponse(p); //and return the product we just created
    }

    public void delete(Long id) {
        productRepository.deleteById(id); //also self explanatory
    }

    //guess what this one does lmao, ding ding ding that's right! updates existing values!
    public ProductResponse update(Long id, ProductRequest dto) {

        Product p = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        p.setName(dto.name()); //just like before, set the new product to the dto properties we have on hand
        p.setPrice(dto.price());
        p.setCurrentStock(dto.currentStock());
        p.setCategory(category);
        p.setUnit(dto.unit());
        p.setMinStock(dto.minStock());
        p.setMaxStock(dto.maxStock());

        productRepository.save(p); //save and return new product
        return toResponse(p);
    }

    //this is for updating the price of a SINGLE product
    public ProductResponse adjustPrice(Long id, double percentage) {
        Product p = productRepository.findById(id)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        double factor = 1 + (percentage / 100.0); //boring math stuff to make it work, but basically:
        double newPrice = p.getPrice() * factor; //multiply the current price by the factor we have

        p.setPrice(newPrice); //update it

        productRepository.save(p);
        return toResponse(p); //standard save and return in dto form
    }

    //same as the previous one but for an entire category instead
    public List<ProductResponse> adjustPriceMass(double percentage, Long categoryId) { //gotta return a list instead since we're changing a lot
        List<Product> products;
        if (categoryId == null) { //if no category is specified, then get ALL OF THEM >:3
            products = productRepository.findAll(); //lowkey forgot we had this method
        }
        else {
            products = productRepository.findByCategoryId(categoryId);
        }

        //nick using a for loop for once?? no way (i hate lists)
        double factor = 1 + (percentage / 100.0);
        for(Product p : products) { //for each product in products, multiply its current price by our factor
            p.setPrice(p.getPrice() * factor); 
        }

        productRepository.saveAll(products);

        //returning a list is always dookey when having this whole dto structure set up...
        return products.stream()
            .map(this::toResponse)
            .toList();
    }

    public List<ProductResponse> findByCategoryId(Long categoryId) {
        // check if the category exists
        categoryRepository.findById(categoryId)
            .orElseThrow(() -> 
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // check its products
        return productRepository.findByCategoryId(categoryId)
            .stream()
            .map(this::toResponse) 
            .toList(); //yk the drill by now
    }

    //----report functions

    //this is for the price list reports, I'm ngl I barely understood this part myself
    public List<PriceListResponse> getPriceList() {
        return productRepository.findAllByOrderByNameAsc() //orders it by alphabetical order
            .stream() //stream so we can map it
            .map(product -> new PriceListResponse( //map it, get a new response object and insert its properties
                product.getName(),
                product.getPrice(),
                product.getUnit(),
                product.getCategory().getName()
            ))
            .toList(); //turn it into a list, this is what gets returned
    }

    //this is for the low stock reports, shows which products have currentStock below minStock
    public List<LowStockResponse> getLowStockReport() {
        //spring data can't logic this by themselves so we're here to help :D
        return productRepository.findAllByOrderByNameAsc() //get every product we have
            .stream()
            .filter(p -> p.getCurrentStock() < p.getMinStock()) //only new p objects are the ones that are low (trippy ik)
            .map(p -> new LowStockResponse(
                p.getName(),
                p.getCurrentStock(),
                p.getMinStock()
            ))
            .toList(); 
            //yknow I'd also add in the category name here but that wasn't requested and I'm already low on time, so...
    }

    public BalanceReportResponse getBalanceReport() {
        List<BalanceReportItemResponse> items = productRepository.findAllByOrderByNameAsc() //get a list of all
            .stream()
            .map(p -> new BalanceReportItemResponse(
                p.getName(),
                p.getCurrentStock(),
                p.getPrice(),
                p.getCurrentStock() * p.getPrice() //this is for the total value of that amount of items
            ))
            .toList(); //yaknow the drill

        double totalValue = items.stream() //this is for our entire catalogue
            .mapToDouble(BalanceReportItemResponse::totalValue) //it's ALREADY a double, but I'm putting this here in case I change pricing to bigDecimal l8r
            .sum(); //basically, get every item in that list, sum everything together, that's totalValue

        //returns the list of each item and its properties, including individual total values for each product, and the total value of our catalogue
        return new BalanceReportResponse(items, totalValue);
    }

    public List<CategoryProductCountResponse> getProductCountByCategory() {
        return categoryRepository.findAllByOrderByNameAsc()
            .stream()
            .map(category -> new CategoryProductCountResponse(
                category.getName(),
                productRepository.countByCategoryId(category.getId()) //
            )) //you know the drill already
            .toList();
    }

    //----end of report functions

    //this converts entities into dto format!! this is also the ONLY helper function in this class!!! :D (whoops)
    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getCurrentStock(),
                p.getCategory() != null ? p.getCategory().getName() : null, //if the category exists then gets its name, otherwise do nothing
                p.getUnit(),
                p.getMinStock(),
                p.getMaxStock()
        );
    }
}
