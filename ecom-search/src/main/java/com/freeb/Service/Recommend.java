package com.freeb.Service;

import com.freeb.Clients.SearchClients;
import com.freeb.Entity.ProductInfo;
import com.freeb.Entity.UserActive;
import com.freeb.Entity.UserSimilarity;
import com.freeb.Utils.MapUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Recommend {

    SearchClients clients;

    Recommend(SearchClients c){
        this.clients = c;
    }

    public static boolean updateActiveBehavior(Long userId, Long itemId) {
        boolean flag = false;
        // TODO 数据库/json 添加数据
        return flag;
    }

    public ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> assembleUserBehavior(List<UserActive> userActiveList) {
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> activeMap = new ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>>();
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
     * 计算用户与用户之间的相似性，返回计算出的用户与用户之间的相似度对象
     * @param activeMap 用户对各个二级类目的购买行为的一个map集合
     * @return 计算出的用户与用户之间的相似度的对象存储形式
     */
    public HashMap<Long,Double> calcSimilarityBetweenUsers(Long userId,ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> activeMap) {
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
        if(activeMap.containsKey(userId)){
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
        }

        return similarityMap;
    }

    /**
     * 找出与userId购买行为最相似的topN个用户
     * @param userSimilarityMap 用户相似度列表
     * @param topN 与userId相似用户的数量
     * @return 与usereId最相似的topN个用户
     */
    public List<Long> getTopNSimilarityUsers(Map<Long,Double> userSimilarityMap, Integer topN) {
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

    public List<Map.Entry<Long,Double>> getTopNSimilarityUserSimilarity(Map<Long,Double> userSimilarityMap, Integer topN) {
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
    public List<Integer> getRecommendCategory(Long userId, HashMap<Long,Double> similarUserMap,Integer calTagNum,Integer categoryNum) {
        List<Integer> recommedCategoryList = new ArrayList<>();

        // userId的浏览行为列表
//        HashMap<Integer,Double> userTagsMap = clients.GetUserTags(userId);

        LinkedHashMap<Integer,Double> currUserTags = new LinkedHashMap<>();

        Double sumWeight = 0.0;
        Double weight;
        // 1.
        for (Map.Entry<Long, Double> simEntry: similarUserMap.entrySet()) {
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



    /**
     * 找到当前商品列表中点击量最高的商品
     * @param productList 商品列表
     * @return 点击量最高的商品
     */
//    public static ProductInfo findMaxHitsProduct(List<? extends ProductInfo> productList) {
//        if (productList == null || productList.size() == 0) {
//            return null;
//        }
//        // 记录当前最大的点击量
//        Long maxHits = 0L;
//
//        // 记录当前点击量最大的商品
//        ProductInfo product = null;
//        for (ProductInfo temp : productList) {
//            if (temp.getHits() >= maxHits) {
//                maxHits = temp.getHits();
//                product = temp;
//            }
//        }
//        return product;
//    }

}
