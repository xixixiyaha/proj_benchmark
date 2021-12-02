package com.freeb.Service;

import com.freeb.Clients.SearchClients;
import com.freeb.Entity.ObjInfo;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;

import java.util.ArrayList;
import java.util.List;

public class SearchServiceImpl implements SearchService {

    private SearchClients clients = new SearchClients();
    private Boolean forceSearch = true;

    @Override
    public List<ObjInfo> GetRecommendObjByName(Long accoundId, String words, SearchType type, SearchOrder order) {

        List<ObjInfo> lst = new ArrayList<>();
        if(!clients.AccountExists(accoundId))return lst;


        if(!forceSearch){
            //TODO buffer
        }else {

        }

        return null;
    }
}
