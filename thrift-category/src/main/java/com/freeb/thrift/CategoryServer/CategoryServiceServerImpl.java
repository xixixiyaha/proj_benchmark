package com.freeb.thrift.CategoryServer;

import com.freeb.Service.CategoryServiceImpl;
import com.freeb.thrift.*;
import com.freeb.thrift.CategoryClients.CategoryForeignClients;
import org.apache.thrift.TException;

import java.util.List;

public class CategoryServiceServerImpl implements CategoryService.Iface {

    private final com.freeb.Service.CategoryService categoryService = new CategoryServiceImpl(new CategoryForeignClients(),10);

    public CategoryServiceServerImpl() throws ClassNotFoundException {
    }

    @Override
    public ProductPage GetProductPage(long prodId) throws TException {
        return CategoryTypeConvert.ConvertProductPageOri2Thr(categoryService.GetProductPage(prodId));
    }

    @Override
    public List<CategoryPage> GetCategoryPage(long userId, String searchKey) throws TException {
        return CategoryTypeConvert.ConvertCategoryPageLstOri2Thr(categoryService.GetCategoryPage(userId,searchKey));
    }

    @Override
    public List<ProductInfo> BM2CompareParallelRpcEfficiency(int totalComputationLoad, int threadNum) throws TException {
        return CategoryTypeConvert.ConvertProducInfoLstOri2Thr(categoryService.BM2CompareParallelRpcEfficiency(totalComputationLoad,threadNum));
    }

    @Override
    public List<ProductPage> BM4ComparePatternFanout(List<Long> pidLst) throws TException {
        return CategoryTypeConvert.ConvertProductPageLstOri2Thr(categoryService.BM4ComparePatternFanout(pidLst));
    }
}
