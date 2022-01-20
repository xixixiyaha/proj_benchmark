package com.freeb.Service;

import com.freeb.Entity.CategoryPage;
import com.freeb.Entity.ProductInfo;
import com.freeb.Entity.ProductPage;

import java.util.List;

public interface CategoryService {
    ProductPage GetProductPage(Long prodId);

    List<CategoryPage> GetCategoryPage(Long userId, String searchKey);

    List<ProductInfo> BM2CompareParallelRpcEfficiency(Integer totalComputationLoad, Integer threadNum);

    List<ProductPage> BM4ComparePatternFanout(List<Long> pidLst);


}
