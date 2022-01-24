package com.freeb.Service;

import com.freeb.Clients.SearchClients;
import com.freeb.Dao.ProductInfoStorage;
import com.freeb.Entity.ProductInfo;
import com.freeb.Entity.UserActive;
import com.freeb.Enum.SearchOrder;
import com.freeb.Utils.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Recommend {

    private static final Logger logger = LoggerFactory.getLogger(Recommend.class);

    SearchClients clients;

    private ConcurrentHashMap<Long,HashSet<Long>> activeMap;
    private long updateTime;

    private ProductInfoStorage storage;
    private Boolean IS_OFFLINE = false;

    public Recommend(SearchClients c){
        this.clients = c;
        this.updateTime= 0;
        this.activeMap=null;
    }
    public void SetStorage(){
        storage = new ProductInfoStorage();
    }
    public void SetOffline(Boolean status){
        IS_OFFLINE = status;
    }


    public Map<Integer,Double> GetUserTags(Long id){
        HashMap<Integer, Integer> categoryTags;
        if(IS_OFFLINE){
            categoryTags = storage.GetUserActiveByCategory(id);
        }else{
            categoryTags = clients.GetUserActiveByCategory(id);
        }
        Integer sumClicks = 0;
        Map<Integer,Double> tags = new HashMap<>();
        for (Map.Entry<Integer, Integer> integerIntegerEntry : categoryTags.entrySet()) {
            sumClicks += integerIntegerEntry.getValue();
        }
        for(Map.Entry<Integer,Integer> entry:categoryTags.entrySet()){
            tags.put(entry.getKey(),(entry.getValue()*1.0)/sumClicks);
        }
        return tags;
    }

    public boolean CreateUserClickBehavior(Long userId, Long prodId) {
        boolean flag = false;
        //TODO@ low priority 数据库/json 添加数据
        // 现在 user + prod => 查物品表 categoryId => 更新
        // 改进 直接给出 三者

        // NOTICE 目前一个用户重复点击一个商品会重复计数

        return flag;
    }

    /*
    * Deprecated. Only in Test.
    * */

    public ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> AssembleUserBehavior(List<UserActive> userActiveList) {
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> activemap = new ConcurrentHashMap<>();
        // 遍历查询到的用户点击行为数据
        for (UserActive userActive : userActiveList) {
            // 1.获取用户id
            Long userId = userActive.getUserId();
            // 2.获取该二级类目的点击量
            Long categoryId = userActive.getCategoryId();

            // 判断activeMap中是否已经存在了该userId的信息，如果存在则进行更新
            if (activemap.containsKey(userId)) {
                ConcurrentHashMap<Long, Long> tempMap = activemap.get(userId);
                if(tempMap.containsKey(categoryId)){
                    Long hits = tempMap.get(categoryId) + 1L;
                    tempMap.put(categoryId, hits);
                }else {
                    tempMap.put(categoryId, 1L);
                }
                activemap.put(userId, tempMap);
            } else {
                // 不存在则直接put进
                ConcurrentHashMap<Long, Long> category2Map = new ConcurrentHashMap<Long, Long>();
                category2Map.put(categoryId, 1L);
                activemap.put(userId, category2Map);
            }
        }

        return activemap;
    }

    /**
     * Deprecated. Only in Test.
     * 计算一个 userId 的浏览行为
     * @return 用户的 各个 categoryId 的点击频率
     */
    public HashMap<Long, HashMap<Integer, Integer>> AssembleUserBehavior() {
        HashMap<Long, HashMap<Integer, Integer>> activemap = new HashMap<>();
        // 遍历查询到的用户点击行为数据
        // 获取最近活跃的 1000 个用户
        List<Long> activeUsers = clients.GetLastestActiveUsers(1000);
        for(Long uid:activeUsers){
            activemap.put(uid,clients.GetUserActiveByCategory(uid));
        }
        logger.info("assemable user num="+activemap.size());
        return activemap;
    }


    /**
     *
     * @return 获取最近活跃的1000名用户和他们最近点击的50个商品,每120s更新一次
     */
    public ConcurrentHashMap<Long, HashSet<Long>> AssembleUserClicks(){
//        ConcurrentHashMap<Long, HashSet<Long>> activeMap = new ConcurrentHashMap<>();
        long now = new Date().getTime();
        if(activeMap!=null&&(now-updateTime)/1000<120){
            return activeMap;
        }
        activeMap = new ConcurrentHashMap<>();
        List<Long> activeUsers = clients.GetLastestActiveUsers(1000);
        for(Long uid:activeUsers){
            activeMap.put(uid,clients.GetUserActiveByProduct(uid));
        }
        now =  new Date().getTime();
        updateTime = now;
        logger.info("update at "+updateTime+"assemable user num="+activeMap.size());
        return activeMap;
    }

    /**
     * 计算用户与用户之间的相似性，返回计算出的用户与用户之间的相似度对象
     * @param activeMap 用户对各个二级类目的购买行为的一个map集合
     * @return 计算出的用户与用户之间的相似度的对象存储形式
     */
    public HashMap<Long,Double> calcSimilarityBetweenUsers(Long userId,ConcurrentHashMap<Long, HashSet<Long>> activeMap) {
        // 用户之间的相似度对集合
        HashMap<Long,Double> similarityMap = new HashMap<>();

        // 计算所有的用户之间的相似度对
        if(!activeMap.containsKey(userId)) {
            activeMap.put(userId,clients.GetUserActiveByProduct(userId));
        }

        // 获取所有的键的集合
        Set<Long> userSet = activeMap.keySet();

        // 把这些集合放入ArrayList中
        List<Long> userIdList = new ArrayList<>(userSet);

        // 小于两个说明当前map集合中只有小于等于一个用户，直接返回
        if (userIdList.size() < 2) {
            logger.warn(" userIdList.size() < 2 userId="+userId);
            return similarityMap;
        }

        HashSet<Long> key1Set = activeMap.get(userId);
        double key1size = Math.sqrt(key1Set.size());
//        Iterator<Long> it1;

        for (Long refUserId : userIdList) { //O(num(refUser)*num(prodId))
            if (userId.equals(refUserId)) {
                continue;
            }
//            it1 = key1Set.iterator();
            HashSet<Long> key2Set = activeMap.get(refUserId);
//            // 两用户之间的相似度
            double similarity = 0.0;
//            // 余弦相似度公式中的分子
            double molecule = 0.0;
//            // 余弦相似度公式中的分母
            double denominator = 1.0;

            // 先计算分母 等会set2会变成交集
            denominator = Math.sqrt(key2Set.size())*key1size;
            //交集 O(n)
            key2Set.retainAll(key1Set);
            //计算分子
            molecule = 1.0*key2Set.size();
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

        for (Map.Entry<Long,Double> entry : minHeap) {
            similarityList.add(entry.getKey());
        }
        return similarityList;
    }

    public List<Map.Entry<Long,Double>> GetTopNSimilarityUserSimilarity(Map<Long,Double> userSimilarityMap, Integer topN) {
        // 用来记录与userId相似度最高的前N个用户的id
        List<Map.Entry<Long,Double>> similarityList = new ArrayList<>(topN);
        // 堆排序找出最高的前N个用户，建立小根堆，遍历的时候当前的这个相似度比堆顶元素大就剔掉堆顶的值，把这个数入堆(把小的都删除干净,所以要建立小根堆)
        PriorityQueue<Map.Entry<Long,Double>> minHeap = new PriorityQueue<Map.Entry<Long,Double>>((o1, o2) -> {
            if (o1.getValue() - o2.getValue() > 0) {
                return 1;
            } else if (o1.getValue() - o2.getValue() == 0) {
                return 0;
            } else {
                return -1;
            }
        });

        StringBuilder builder = new StringBuilder();
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
            //Notice 这里对吗
            similarityList.add(entry);
            builder.append(String.format("refUserId=%d similarity=%f %n",entry.getKey(),entry.getValue()));
        }
        logger.debug(builder.toString());
        return similarityList;
    }

    /**
     * 到similarUserList中的用户访问的类目中查找userId不经常点击的二级类目中获得被推荐的类目id
     * @param userId 被推荐商品的用户id
     * @param similarUserList 用userId相似的用户集合 <Id,Similarity>
     * @param calTagNum 相似用户计算前 calTagNum 个分类的权重
     * @return 可以推荐给userId的类目id
     */
    public List<Integer> GetRecommendCategory(Long userId, List<Map.Entry<Long,Double>> similarUserList,Integer calTagNum,Integer categoryNum) {
        List<Integer> rcmdCategoryList = new ArrayList<>();

        LinkedHashMap<Integer,Double> currUserTags = new LinkedHashMap<>();
        int cnter = 0;
        Double sumWeight = 0.0;
        Double weight;
        // 1.
        for (Map.Entry<Long, Double> simEntry: similarUserList) {
            Double similarity = simEntry.getValue();
            if(similarity<0.3){
                // Todo@ Notice the threshold - Is 0.3 reasonable ?
                continue;
            }
            cnter++;
            // 找到当前这个用户的浏览行为
            LinkedHashMap<Integer,Double> refTagsMap = (LinkedHashMap<Integer, Double>) GetUserTags(simEntry.getKey());
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

        logger.info("valid refUser Num = "+cnter);
        currUserTags = (LinkedHashMap<Integer, Double>) MapUtil.sortByBigValue(currUserTags);
        for (Map.Entry<Integer, Double> tagEntry: currUserTags.entrySet()) {
            currUserTags.put(tagEntry.getKey(),tagEntry.getValue()/sumWeight);
        }
//        clients.SetUserTags(userId,currUserTags);
        cnter = 0;
        for (Map.Entry<Integer, Double> tagEntry: currUserTags.entrySet()) {
            rcmdCategoryList.add(tagEntry.getKey());
            if(++cnter>categoryNum){
                break;
            }
        }
        cnter--;
        logger.info("valid rcmdCategoeyNum="+cnter);
        return rcmdCategoryList;
    }


    private static Integer REDUNDANCY_CATEGORY_NUM = 5;
    private static Integer DEFAUALT_SIMILARITY_NUM = 50;
    private static List<Integer> DEFAULT_CATEGORY_WEIGHT = List.of(60,30,10);
    /**
     * 根据userId给出推荐商品 Recommend.Class 向外提供的函数
     * @param words 搜索关键词
     * @param categoryWeight 根据分类推荐商品，List的长度len决定了要推荐top(Len)分类下的商品，权重是一共推荐x%*总推荐数
     *                       目前写死 len = 3 (0.6,0.3,0.1)
     * @param prodNum 推荐总数 目前写死 num = 50
     */
    public List<Long> GetRecommendProductsByProdName(Long userId, SearchOrder order, String words, List<Integer> categoryWeight, Integer prodNum){
        List<Long> prodList = new ArrayList<>();
        int categoryLen = categoryWeight.size();

        // 1. & 2. 获取最近活跃的1000名用户 & 计算当前用户与他们的相似度
        HashMap<Long,Double> similarities =  calcSimilarityBetweenUsers(userId,AssembleUserClicks());
        if(similarities.size()<1){
            logger.warn("similar users is null ! cannnot recommend in current version !");
            return prodList;
        }
        // 3. 获取最相似的 DEFAUALT_SIMILARITY_NUM 个用户
        List<Map.Entry<Long,Double>> topSimilarities = GetTopNSimilarityUserSimilarity(similarities,DEFAUALT_SIMILARITY_NUM);
        // 4. 获取用户可能最喜欢的类别
        List<Integer> rcmdCategory = GetRecommendCategory(userId,topSimilarities,categoryLen+REDUNDANCY_CATEGORY_NUM,categoryLen);
        for(int i=0;i<categoryLen;i++){
            List<Long> prodSubList = GetProductByCategory(rcmdCategory.get(i),order,words,prodNum*categoryWeight.get(i));
            if(prodSubList == null || prodSubList.size()==0){
                logger.error(String.format("prodSubList is invalid! userId={%d} category={%d} order={%s} words={%s}",userId,rcmdCategory.get(i),order.toString(),words));
                continue;
            }
            prodList.addAll(prodSubList);
        }
        return prodList;
    }

    public List<Long> GetRecommendProductsByProdName(Long userId, SearchOrder order, String words){
        List<Long> prodList = new ArrayList<>();
        int categoryLen = DEFAULT_CATEGORY_WEIGHT.size();

        // 1. & 2. 获取最近活跃的1000名用户 & 计算当前用户与他们的相似度
        HashMap<Long,Double> similarities =  calcSimilarityBetweenUsers(userId,AssembleUserClicks());
        if(similarities.size()<1){
            logger.warn("similar users is null ! cannnot recommend in current version !");
            return prodList;
        }
        // 3. 获取最相似的 DEFAUALT_SIMILARITY_NUM 个用户
        List<Map.Entry<Long,Double>> topSimilarities = GetTopNSimilarityUserSimilarity(similarities,DEFAUALT_SIMILARITY_NUM);
        // 4. 获取用户可能最喜欢的类别
        List<Integer> rcmdCategory = GetRecommendCategory(userId,topSimilarities,categoryLen+REDUNDANCY_CATEGORY_NUM,categoryLen);
        for(int i=0;i<categoryLen;i++){
            List<Long> prodSubList = GetProductByCategory(rcmdCategory.get(i),order,words,50*DEFAULT_CATEGORY_WEIGHT.get(i));
            if(prodSubList == null || prodSubList.size()==0){
                logger.error(String.format("prodSubList is invalid! userId={%d} category={%d} order={%s} words={%s}",userId,rcmdCategory.get(i),order.toString(),words));
                continue;
            }
            prodList.addAll(prodSubList);
        }
        return prodList;
    }

    // NOTICE 现阶段提供两种 order: by SALES 和 模糊搜索
    public List<Long> GetProductByCategory(Integer categoryId, SearchOrder order,String words,Integer prodNum){
        switch (order){
            case SALES:
                return clients.GetProductByCategory(categoryId,order,prodNum);
            case SIMILARITY:
                return clients.GetProductBySimilarity(categoryId,order,words,prodNum);
            default:
                logger.warn("Unsupported recommend type !");
        }
        return null;
    }

}
