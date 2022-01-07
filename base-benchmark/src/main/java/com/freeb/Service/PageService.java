package com.freeb.Service;

import Bean.NForm;
import Bean.Page;

import java.util.List;

public interface PageService {

    public boolean createPage(Page<NForm> page);

    public boolean createPages(List<Page<NForm>> pages);

    public boolean existPage(String pageToken);

    public Page<NForm> getPage(String pageToken);

    public Page<NForm> getPage(int typeNo, int pageNo);

    public List<Page<NForm>> getPages(List<String> pageTokens);

    public List<Integer> getReadUsers(String pageToken);

}
