package com.freeb.Service;

import com.freeb.Clients.SearchClients;
import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private SearchClients clients = new SearchClients();
    private Recommend rcmd = new Recommend(clients);
    private Boolean forceSearch = true;

    @Override
    public List<ProductInfo> GetRecommendByProdName(Long userId, String words, SearchType type, SearchOrder order) {

        List<ProductInfo> lst = new ArrayList<>();
        if(!clients.AccountExists(userId))return lst;
        List<Integer> tags = clients.GetAccountTag(userId);

        if(!forceSearch){
            //TODO buffer
        }else {
            switch (type){
                case OBJ_NAME:
                    return rcmd.GetRecommendProductsByProdName(userId,order,words);
                case MERCHANT_NAME:
                    //todo
                    break;
                default:
                    break;
            }
        }

        return null;
    }


    @Override
    public Boolean CreateUserClick(Long userId, Long prodId, Integer categoryId) {
        if(!clients.AccountExists(userId)){
            return false;
        }
        return rcmd.CreateUserClickBehavior(userId,prodId,categoryId);
    }
}
