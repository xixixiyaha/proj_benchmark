package com.freeb.Service;

import com.freeb.Entity.CategoryPage;
import com.freeb.Entity.ProductPage;

import java.util.List;

public interface CategoryService {
    public ProductPage GetProductPage(Long prodId);

    public List<CategoryPage> GetCategoryPage(Long userId, String searchKey);


}
