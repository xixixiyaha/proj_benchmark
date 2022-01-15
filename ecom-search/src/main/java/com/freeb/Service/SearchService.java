package com.freeb.Service;

import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;

import java.util.List;

public interface SearchService {


    /*
    * CPU contention 预计使用计算密集 cache可选
    * */
    List<Long> GetRecommendByProdName(Long userId, String words, SearchType type, SearchOrder order);

    /*
    * 插入数据
    * */
    Boolean CreateUserClick(Long userId,Long prodId,Integer categoryId);

    List<Long> IdealResEfficiencyTest(Integer totalComputationLoad, Integer threadNum);

}
