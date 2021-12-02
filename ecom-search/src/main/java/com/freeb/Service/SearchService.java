package com.freeb.Service;

import com.freeb.Entity.ObjInfo;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;

import java.util.List;

public interface SearchService {


    /*
    * CPU contention 预计使用计算密集 cache可选
    * */
    List<ObjInfo> GetRecommendObjByName(Long accoundId, String words, SearchType type, SearchOrder order);

}
