package com.freeb.Service;

import com.freeb.Clients.SearchClients;
import com.freeb.Dao.ProductInfoStorage;
import com.freeb.Entity.ProductInfo;
import com.freeb.Entity.UserActive;
import com.freeb.Entity.UserSimilarity;
import com.freeb.Enum.SearchOrder;
import com.freeb.Utils.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Recommend {

    private static final Logger logger = LoggerFactory.getLogger(ProductInfoStorage.class);

    SearchClients clients;
    ProductInfoStorage storage = new ProductInfoStorage();

    Recommend(SearchClients c){
        this.clients = c;
    }

    public boolean CreateUserClickBehavior(Long userId, Long prodId) {
        boolean flag = false;
        //TODO 数据库/json 添加数据
        // 现在 user + prod => 查物品表 categoryId => 更新
        // 改进 直接给出 三者

        //TODO & NOTICE 目前一个用户重复点击一个商品会重复计数

        return flag;
    }

    public boolean CreateUserClickBehavior(Long userId, Long prodId, Integer categoryId) {
        boolean flag = false;
        //TODO 数据库/json 添加数据
        return storage .CreateActiveBehavior(userId,prodId,categoryId);

        //TODO & NOTICE 目前一个用户重复点击一个商品会重复计数
    }
    private ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> AssembleUserBehavior(List<UserActive> userActiveList) {
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> activeMap = new ConcurrentHashMap<>();
        // 遍历查询到的用户点击行为数据
        for (UserActive userActive : userActiveList) {
            // 1.获取用户id
            Long userId = userActive.getUserId();
            // 2.获取该二级类目的点击量
            Long categoryId = userActive.getCategoryId();

            // 判断activeMap中是否已经存在了该userId的信息，如果存在则进行更新
            if (activeMap.containsKey(userId)) {
                ConcurrentHashMap<Long, Long> tempMap = activeMap.get(userId);
                if(tempMap.containsKey(categoryId)){
                    Long hits = tempMap.get(categoryId) + 1L;
                    tempMap.put(categoryId, hits);
                }else {
                    tempMap.put(categoryId, 1L);
                }
                activeMap.put(userId, tempMap);
            } else {
                // 不存在则直接put进
                ConcurrentHashMap<Long, Long> category2Map = new ConcurrentHashMap<Long, Long>();
                category2Map.put(categoryId, 1L);
                activeMap.put(userId, category2Map);
            }
        }

        return activeMap;
    }

    /**
     * 计算一个 userId 的浏览行为
     * @return 用户的 各个 categoryId 的点击频率
     */
    private ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> AssembleUserBehavior() {
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> activeMap = new ConcurrentHashMap<>();
        // 遍历查询到的用户点击行为数据
        // 获取最近活跃的 1000 个用户
        List<Long> activeUsers = storage.GetLastestAvtiveUsers(1000);
        for(Long uid:activeUsers){
            activeMap.put(uid,storage.GetUserActiveByCategory(uid));
        }
        return activeMap;
    }

    /**
     * 计算用户与用户之间的相似性，返回计算出的用户与用户之间的相似度对象
     * @param activeMap 用户对各个二级类目的购买行为的一个map集合
     * @return 计算出的用户与用户之间的相似度的对象存储形式
     */
    private HashMap<Long,Double> calcSimilarityBetweenUsers(Long userId,ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> activeMap) {
        // 用户之间的相似度对集合
        HashMap<Long,Double> similarityMap = new HashMap<>();

        // 获取所有的键的集合
        Set<Long> userSet = activeMap.keySet();

        // 把这些集合放入ArrayList中
        List<Long> userIdList = new ArrayList<>(userSet);

        // 小于两个说明当前map集合中只有小于等于一个用户，直接返回
        if (userIdList.size() < 2) {
            return similarityMap;
        }

        // 计算所有的用户之间的相似度对
        if(!activeMap.containsKey(userId)) {
            activeMap.put(userId,storage.GetUserActiveByCategory(userId));
        }
        ConcurrentHashMap<Long, Long> userCategory2Map = activeMap.get(userId);
        Set<Long> key1Set = userCategory2Map.keySet();
        Iterator<Long> it1;

        for (Long refUserId : userIdList) {
            if (userId.equals(refUserId)) {
                continue;
            }
             it1 = key1Set.iterator();
            // 分别获取两个用户对每个二级类目的点击量
            ConcurrentHashMap<Long, Long> userRefCategory2Map = activeMap.get(refUserId);

            // 获取map中二级类目id的集合
            Set<Long> key2Set = userRefCategory2Map.keySet();
            Iterator<Long> it2 = key2Set.iterator();

            // 两用户之间的相似度
            double similarity = 0.0;
            // 余弦相似度公式中的分子
            double molecule = 0.0;
            // 余弦相似度公式中的分母
            double denominator = 1.0;
            // 余弦相似度公式中分母根号下的两个向量的模的值
            double vector1 = 0.0;
            double vector2 = 0.0;

            while (it1.hasNext() && it2.hasNext()) {
                Long it1Id = it1.next();
                Long it2Id = it2.next();
                // 获取二级类目对应的点击次数
                Long hits1 = userCategory2Map.get(it1Id);
                Long hits2 = userRefCategory2Map.get(it2Id);
                // 累加分子
                molecule += hits1 * hits2;
                // 累加分母中的两个向量的模
                vector1 += Math.pow(hits1, 2);
                vector2 += Math.pow(hits2, 2);
            }
            // 计算分母
            denominator = Math.sqrt(vector1) * Math.sqrt(vector2);
            // 计算整体相似度
            similarity = molecule / denominator;

            similarityMap.put(refUserId,similarity);
        }

        return similarityMap;
    }

    /**
     * 找出与userId购买行为最相似的topN个用户
     * @param userSimilarityMap 用户相似度列表
     * @param topN 与userId相似用户的数量
     * @return 与usereId最相似的topN个用户
     */
    private List<Long> GetTopNSimilarityUsers(Map<Long,Double> userSimilarityMap, Integer topN) {
        // 用来记录与userId相似度最高的前N个用户的id
        List<Long> similarityList = new ArrayList<>(topN);

        // 堆排序找出最高的前N个用户，建立小根堆，遍历的时候当前的这个相似度比堆顶元素大就剔掉堆顶的值，把这个数入堆(把小的都删除干净,所以要建立小根堆)
        PriorityQueue<Map.Entry<Long,Double>> minHeap = new PriorityQueue<Map.Entry<Long,Double>>(new Comparator<Map.Entry<Long,Double>>() {
            @Override
            public int compare(Map.Entry<Long,Double> o1, Map.Entry<Long,Double> o2) {
                if (o1.getValue() - o2.getValue() > 0) {
                    return 1;
                } else if (o1.getValue() - o2.getValue() == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        for (Map.Entry<Long,Double> entry : userSimilarityMap.entrySet()) {
            if (minHeap.size() < topN) {
                minHeap.offer(entry);
                assert minHeap.peek() != null;
                System.out.println(minHeap.peek().getValue());
            } else {
                assert minHeap.peek() != null;
                if (minHeap.peek().getValue() < entry.getValue()) {
                    minHeap.poll();
                    minHeap.offer(entry);
                }
            }
        }
        // 把得到的最大的相似度的用户的id取出来(不要取它自己)
        for (Map.Entry<Long,Double> entry : minHeap) {
            similarityList.add(entry.getKey());
        }
        return similarityList;
    }

    private List<Map.Entry<Long,Double>> GetTopNSimilarityUserSimilarity(Map<Long,Double> userSimilarityMap, Integer topN) {
        // 用来记录与userId相似度最高的前N个用户的id
        List<Map.Entry<Long,Double>> similarityList = new ArrayList<>(topN);

        // 堆排序找出最高的前N个用户，建立小根堆，遍历的时候当前的这个相似度比堆顶元素大就剔掉堆顶的值，把这个数入堆(把小的都删除干净,所以要建立小根堆)
        PriorityQueue<Map.Entry<Long,Double>> minHeap = new PriorityQueue<Map.Entry<Long,Double>>(new Comparator<Map.Entry<Long,Double>>() {
            @Override
            public int compare(Map.Entry<Long,Double> o1, Map.Entry<Long,Double> o2) {
                if (o1.getValue() - o2.getValue() > 0) {
                    return 1;
                } else if (o1.getValue() - o2.getValue() == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        for (Map.Entry<Long,Double> entry : userSimilarityMap.entrySet()) {
            if (minHeap.size() < topN) {
                minHeap.offer(entry);
                assert minHeap.peek() != null;
                System.out.println(minHeap.peek().getValue());
            } else {
                assert minHeap.peek() != null;
                if (minHeap.peek().getValue() < entry.getValue()) {
                    minHeap.poll();
                    minHeap.offer(entry);
                }
            }
        }
        // 把得到的最大的相似度的用户的id取出来(不要取它自己)
        for (Map.Entry<Long,Double> entry : minHeap) {
            similarityList.add(entry);
        }
        return similarityList;
    }

    /**
     * 到similarUserList中的用户访问的二级类目中查找userId不经常点击的二级类目中获得被推荐的类目id
     * @param userId 被推荐商品的用户id
     * @param similarUserMap 用userId相似的用户集合 <Id,Similarity>
     * @param calTagNum 相似用户计算前 calTagNum 个分类的权重
     * @return 可以推荐给userId的类目id
     */
    private List<Integer> GetRecommendCategory(Long userId, List<Map.Entry<Long,Double>> similarUserMap,Integer calTagNum,Integer categoryNum) {
        List<Integer> recommedCategoryList = new ArrayList<>();

        // userId的浏览行为列表
//        HashMap<Integer,Double> userTagsMap = clients.GetUserTags(userId);

        LinkedHashMap<Integer,Double> currUserTags = new LinkedHashMap<>();

        Double sumWeight = 0.0;
        Double weight;
        // 1.
        for (Map.Entry<Long, Double> simEntry: similarUserMap) {
            Double similarity = simEntry.getValue();
            if(similarity<0.5){
                // Todo: Notice the threshold - Is 0.5 reasonable ?
                continue;
            }
            // 找到当前这个用户的浏览行为
            Map<Integer,Double> refTagsMap = clients.GetUserTags(simEntry.getKey());
            // 排序
            refTagsMap = (LinkedHashMap<Integer, Double>) MapUtil.sortByBigValue(refTagsMap);

            int counter = 0;
            // @tagEntry<tagId,tagPreference> 0<tp<1
            for (Map.Entry<Integer, Double> tagEntry : refTagsMap.entrySet()) {
                weight = similarity*tagEntry.getValue();
                sumWeight+=weight;
                if(currUserTags.containsKey(tagEntry.getKey())){
                    currUserTags.put(tagEntry.getKey(),currUserTags.get(tagEntry.getKey())+weight);
                }else {
                    currUserTags.put(tagEntry.getKey(),weight);
                }
                if(++counter>calTagNum){
                    break;
                }
            }
        }
        currUserTags = (LinkedHashMap<Integer, Double>) MapUtil.sortByBigValue(currUserTags);
        for (Map.Entry<Integer, Double> tagEntry: currUserTags.entrySet()) {
            currUserTags.put(tagEntry.getKey(),tagEntry.getValue()/sumWeight);
        }
        clients.SetUserTags(userId,currUserTags);
        int counter = 0;
        for (Map.Entry<Integer, Double> tagEntry: currUserTags.entrySet()) {
            recommedCategoryList.add(tagEntry.getKey());
            if(++counter>categoryNum){
                break;
            }
        }
        return recommedCategoryList;
    }


    private static Integer REDUNDANCY_CATEGORY_NUM = 10;
    private static Integer DEFAUALT_SIMILARITY_NUM = 50;
    private static List<Integer> DEFAULT_CATEGORY_WEIGHT = List.of(60,30,10);
    /**
     * 根据userId给出推荐商品 Recommend.Class 向外提供的函数
     * @param words 搜索关键词
     * @param categoryWeight 根据分类推荐商品，List的长度len决定了要推荐top(Len)分类下的商品，权重是一共推荐x%*总推荐数
     *                       目前写死 len = 3 (0.6,0.3,0.1)
     * @param prodNum 推荐总数 目前写死 num = 50
     */
    public List<ProductInfo> GetRecommendProductsByProdName(Long userId, SearchOrder order, String words, List<Integer> categoryWeight, Integer prodNum){
        List<ProductInfo> prodList = new ArrayList<>();
        int categoryLen = categoryWeight.size();

        // 1. & 2. 获取最近活跃的1000名用户 & 计算当前用户与他们的相似度
        HashMap<Long,Double> similarities =  calcSimilarityBetweenUsers(userId,AssembleUserBehavior());
        // 3. 获取最相似的 DEFAUALT_SIMILARITY_NUM 个用户
        List<Map.Entry<Long,Double>> topSimilarities = GetTopNSimilarityUserSimilarity(similarities,DEFAUALT_SIMILARITY_NUM);
        // 4. 获取用户可能最喜欢的类别
        List<Integer> rcmdCategory = GetRecommendCategory(userId,topSimilarities,categoryLen+REDUNDANCY_CATEGORY_NUM,categoryLen);
        // TODO: null 兜底逻辑
        for(int i=0;i<categoryLen;i++){
            List<ProductInfo> prodSubList = GetProductByCategory(rcmdCategory.get(i),order,words,prodNum*categoryWeight.get(i));
            prodList.addAll(prodSubList);
        }
        return prodList;
    }

    public List<ProductInfo> GetRecommendProductsByProdName(Long userId, SearchOrder order, String words){
        List<ProductInfo> prodList = new ArrayList<>();
        int categoryLen = DEFAULT_CATEGORY_WEIGHT.size();

        // 1. & 2. 获取最近活跃的1000名用户 & 计算当前用户与他们的相似度
        HashMap<Long,Double> similarities =  calcSimilarityBetweenUsers(userId,AssembleUserBehavior());
        // 3. 获取最相似的 DEFAUALT_SIMILARITY_NUM 个用户
        List<Map.Entry<Long,Double>> topSimilarities = GetTopNSimilarityUserSimilarity(similarities,DEFAUALT_SIMILARITY_NUM);
        // 4. 获取用户可能最喜欢的类别
        List<Integer> rcmdCategory = GetRecommendCategory(userId,topSimilarities,categoryLen+REDUNDANCY_CATEGORY_NUM,categoryLen);
        // TODO: null 兜底逻辑
        for(int i=0;i<categoryLen;i++){
            List<ProductInfo> prodSubList = GetProductByCategory(rcmdCategory.get(i),order,words,50*DEFAULT_CATEGORY_WEIGHT.get(i));
            prodList.addAll(prodSubList);
        }
        return prodList;
    }

    // Todo 现阶段提供两种 order: by SALES 和 模糊搜索
    public List<ProductInfo> GetProductByCategory(Integer categoryId, SearchOrder order,String words,Integer prodNum){
        switch (order){
            case SALES:
                return storage.GetProductByCategory(categoryId,order,prodNum);
            case SIMILARITY:
                return storage.GetProductBySimilarity(categoryId,order,words,prodNum);
            default:
                logger.warn("Unsupported recommend type !");
        }

        return null;
    }


    /**
     * 找到当前商品列表中点击量最高的商品
     * @param productList 商品列表
     * @return 点击量最高的商品
     */

}
