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
    public List<ProductInfo> GetRecommendByName(Long accountId, String words, SearchType type, SearchOrder order) {

        List<ProductInfo> lst = new ArrayList<>();
        if(!clients.AccountExists(accountId))return lst;
        List<Integer> tags = clients.GetAccountTag(accountId);

        if(!forceSearch){
            //TODO buffer
        }else {
            switch (type){
                case OBJ_NAME:
                    return RecommendObjByName(accountId,words,order,tags);
                case MERCHANT_NAME:
                    //todo
                    break;
                default:
                    break;
            }
        }

        return null;
    }

    /**/
    private List<ProductInfo> RecommendObjByName(Long accountId, String words, SearchOrder order, List<Integer> tags){
        List<ProductInfo> lst = new ArrayList<>();
        if(tags == null){
            logger.warn("Fall into default recommend");
        }else {
            //todo recommend system
        }

        return null;
    }

}
