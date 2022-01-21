package freeb.thrift.SearchServer;


import freeb.ProductTypeConvert;
import freeb.thrift.SearchClients.ProductForeignClients;
import freeb.thrift.*;
import org.apache.thrift.TException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductServiceServerImpl implements ProductService.Iface {

    private final com.freeb.Service.ProductService searchService = new com.freeb.Service.ProductServiceImpl(new ProductForeignClients());

    @Override
    public Map<Integer, Integer> GetUserActiveByCategory(long id) throws TException {
        return searchService.GetUserActiveByCategory(id);
    }

    @Override
    public boolean CreateActiveBehavior(long uid, long pid, int cid) throws TException {
        return searchService.CreateActiveBehavior(uid,pid,cid);
    }

    @Override
    public List<Long> GetLastestActiveUsers(int userNum) throws TException {
        return searchService.GetLastestActiveUsers(userNum);
    }

    @Override
    public Set<Long> GetUserActiveByProduct(long uid) throws TException {
        return searchService.GetUserActiveByProduct(uid);
    }

    @Override
    public List<Long> GetProductByCategory(int cid, SearchOrder order, int prodNum) throws TException {
        return searchService.GetProductByCategory(cid,ProductTypeConvert.ConvertSearchOrderThr2Ori(order),prodNum);
    }

    @Override
    public List<Long> GetProductBySimilarity(int cid, SearchOrder order, String words, int prodNum) throws TException {
        return searchService.GetProductBySimilarity(cid,ProductTypeConvert.ConvertSearchOrderThr2Ori(order),words,prodNum);
    }

    @Override
    public ProductInfo IncProductSales(long pid, int perchaseNum) throws TException {
        return ProductTypeConvert.ConvertProductInfoOri2Thr(searchService.IncProductSales(pid,perchaseNum));
    }

    @Override
    public ProductInfo GetProdInfo(long pid) throws TException {
        return  ProductTypeConvert.ConvertProductInfoOri2Thr(searchService.GetProdInfo(pid));
    }

    @Override
    public MerchantInfo GetMerchantInfoById(long mid) throws TException {
        return ProductTypeConvert.ConvertMerchantInfoOri2Thr(searchService.GetMerchantInfoById(mid));
    }

    @Override
    public boolean BM4ComparePatternTrigger(List<Long> uidLst, List<Long> pidLst, List<Integer> cidLst, int compLoad) throws TException {
        return searchService.BM4ComparePatternTrigger(uidLst,pidLst,cidLst,compLoad);
    }

    @Override
    public CommentInfo BM5CompareTransferDataSize(int dataSize) throws TException {
        return ProductTypeConvert.ConvertCommentInfoOri2Thr(searchService.BM5CompareTransferDataSize(dataSize));
    }

    @Override
    public boolean BM6CompareMemBindWidth(int dataSize) throws TException {
        return searchService.BM6CompareMemBindWidth(dataSize);
    }

    @Override
    public List<CommentInfo> GetComments(long prodId, int comtNum) throws TException {
        return ProductTypeConvert.ConvertCommentInfoLstOri2Thr(searchService.GetComments(prodId,comtNum));
    }

    @Override
    public MerchantInfo GetMerchantInfoByProd(long pid) throws TException {
        return ProductTypeConvert.ConvertMerchantInfoOri2Thr(searchService.GetMerchantInfoByProd(pid));
    }
}
