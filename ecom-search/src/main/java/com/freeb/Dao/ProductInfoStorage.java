package com.freeb.Dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;
import com.freeb.Utils.MarshalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ProductInfoStorage {

    private static final Logger logger = LoggerFactory.getLogger(ProductInfoStorage.class);
    static String PROD_DB_URL;
    static String PROD_USER;
    static String PROD_PWD;
    private DruidUtil druidUtil;
    public ProductInfoStorage(){
        logger.error("TODO@ unsupported initialization method");
    }

    public ProductInfoStorage(String url,String user,String psw) throws ClassNotFoundException {
        PROD_DB_URL = url;
        PROD_USER=user;
        PROD_PWD = psw;
        Class.forName("com.mysql.cj.jdbc.Driver");
        druidUtil=new DruidUtil(url,user,psw);
    }

    public Boolean TestConn() {
        try (Connection conn = druidUtil.GetConnection()) {
            return true;
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }
    }

    private static final String GET_PRODUCT_BY_CATEGORY = "SELECT * FROM PRODUCT_INFOS WHERE category_id = ?";

    private static final String GET_PRODUCT_BY_CATEGORY_ORDER_BY_UPDATE_TIME_DESC = "SELECT * FROM PRODUCT_INFOS WHERE category_id = ? ORDER BY update_time DESC";

    private static final String GET_PRODUCT_BY_CATEGORY_ORDER_BY_PRICE_ASC = "SELECT * FROM PRODUCT_INFOS WHERE category_id = ? ORDER BY prod_price ASC";
    private static final String GET_PRODUCT_BY_CATEGORY_ORDER_BY_PRICE_DESC = "SELECT * FROM PRODUCT_INFOS WHERE category_id = ? ORDER BY prod_price DESC";

    private static final String GET_PRODUCT_BY_CATEGORY_ORDER_BY_SALES_DESC = "SELECT * FROM PRODUCT_INFOS WHERE category_id = ? ORDER BY prod_sales DESC";

    private static final String CREATE_PRODUCT = "INSERT INTO PRODUCT_INFOS (prod_name,category_id,prod_price,prod_sales,discounts_id,merchant_id) VALUES (?,?,?,?,?,?)";

    // TODO & NOTICE: 小数据量使用 like 避免大炮打蚊子
    private static final String GET_PRODUCT_BY_SIMILARITY = "SELECT * FROM PRODUCT_INFOS WHERE category_id = ? AND prod_name LIKE %?% ORDER BY prod_sales DESC";


    //todo 挪入productDao
    private static final String GET_CATEGORY_BY_PRODUCT = "SELECT category_id FROM PRODUCT_INFOS WHERE prod_id = ?";
    public Integer GetCategoryByProduct(Long productId) {
        ResultSet rs = null;
        try (Connection conn = druidUtil.GetConnection()) {
            PreparedStatement stmt = conn.prepareStatement(GET_CATEGORY_BY_PRODUCT);
            stmt.setLong(1, productId);
            rs = stmt.executeQuery();
            while (rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Boolean CreateProductInfo(String prodName,Integer categoryId,Double prodPrice,Integer prodSales,Long discountsId,Long merchantId){
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(CREATE_PRODUCT);
            stmt.setString(1,prodName);
            stmt.setInt(2,categoryId);
            stmt.setDouble(3,prodPrice);
            stmt.setInt(4,prodSales);
            stmt.setLong(5,discountsId);
            stmt.setLong(6,merchantId);
            int rs = stmt.executeUpdate();
            if(rs>0)return true;

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
        return false;
    }


    //todo 挪入disocounts
    private static final String CREATE_DISCOUNT = "INSERT INTO DISCOUNT_INFOS(discount_type,prod_id,discount_price) VALUES(?,?,?)";
    public Boolean CreateDiscountInfo(Integer type,Long prodId,Double price){
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(CREATE_DISCOUNT);
            stmt.setInt(1,type);
            stmt.setLong(2,prodId);
            stmt.setDouble(3,price);
            int rs = stmt.executeUpdate();
            if(rs>0)return true;

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
        return false;
    }





    public List<ProductInfo> GetProductByCategory(Integer categoryId, SearchOrder order, Integer topN){
        ResultSet rs;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt;
            StringBuilder builder;
            // 1. Select products WHERE categoryId and order matches
            switch (order){
                case SALES:
                    builder = new StringBuilder(GET_PRODUCT_BY_CATEGORY_ORDER_BY_SALES_DESC);
                    break;
                case PRICE_ASC:
                    builder = new StringBuilder(GET_PRODUCT_BY_CATEGORY_ORDER_BY_PRICE_ASC);
                    break;
                case PRICE_DESC:
                    builder = new StringBuilder(GET_PRODUCT_BY_CATEGORY_ORDER_BY_PRICE_DESC);
                    break;
                case UPDATE_TIME:
                    builder = new StringBuilder(GET_PRODUCT_BY_CATEGORY_ORDER_BY_UPDATE_TIME_DESC);
                    break;
                default:
                    builder = new StringBuilder(GET_PRODUCT_BY_CATEGORY);
            }
            // 2. check topN "-1" means no limit
            if(!topN.equals(-1)){
                builder.append(" LIMIT = ?");
                stmt = conn.prepareStatement(builder.toString());
                stmt.setInt(1,categoryId);
                stmt.setInt(2,topN);
            }else {
                stmt = conn.prepareStatement(builder.toString());
                stmt.setInt(1,categoryId);
            }
            // 3. visit SQL
            rs = stmt.executeQuery();
            // 4. convert into ProdInfo
            return MarshalUtil.convertRs2ProdList(rs);

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
    }

    public List<ProductInfo> GetProductBySimilarity(Integer categoryId, SearchOrder order,String words, Integer topN){
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt;
            StringBuilder builder;
            // 1. Select products WHERE categoryId and order matches
            if (order == SearchOrder.SIMILARITY) {
                builder = new StringBuilder(GET_PRODUCT_BY_SIMILARITY);
            } else {
                logger.warn("<unsupported type> use default method");
                builder = new StringBuilder(GET_PRODUCT_BY_SIMILARITY);
            }
            // 2. check topN "-1" means no limit
            if(!topN.equals(-1)){
                builder.append(" LIMIT = ?");
                stmt = conn.prepareStatement(builder.toString());
                stmt.setInt(1,categoryId);
                stmt.setString(2,words);
                stmt.setInt(3,topN);
            }else {
                stmt = conn.prepareStatement(builder.toString());
                stmt.setInt(1,categoryId);
            }
            // 3. visit SQL
            rs = stmt.executeQuery();
            // 4. convert into ProdInfo
            return MarshalUtil.convertRs2ProdList(rs);

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
    }

    private static String CREATE_USER_ACTIVE = "INSERT INTO USER_ACTIVE_INFOS (user_id,prod_id,category_id) VALUES (?,?,?)";
    private static String GET_USER_ACTIVE_BY_USER_GROUPBY_CATEGORY = "SELECT category_id,COUNT(*) FROM USER_ACTIVE_INFOS WHERE user_id = ? GROUP BY category_id ORDER BY COUNT(*) DESC LIMIT 10";
    private static String GET_LASTEST_ACTIVE_USERS = "SELECT user_id FROM USER_ACTIVE_INFOS GROUP BY user_id ORDER BY MAX(update_time) DESC";

    private static String GET_USER_ACTIVE_BY_USER_GROUPBY_PRODUCT = "SELECT prod_id FROM USER_ACTIVE_INFOS WHERE user_id = ? ORDER BY UPDATE_TIME DESC LIMIT 50";

    public Boolean CreateActiveBehavior(Long userId,Long prodId,Integer categoryId){
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(CREATE_USER_ACTIVE);
            stmt.setLong(1,userId);
            stmt.setLong(2,prodId);
            stmt.setInt(3,categoryId);
            int rs = stmt.executeUpdate();
            if(rs>0)return true;

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return userId最活跃的前10个category
     * TODO : 从最近 x 条该用户的点击记录里找 ↖
     */
    public ConcurrentHashMap<Integer, Integer> GetUserActiveByCategory(Long userId){
        ConcurrentHashMap<Integer, Integer> userActives = new ConcurrentHashMap<>();
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(GET_USER_ACTIVE_BY_USER_GROUPBY_CATEGORY);
            stmt.setLong(1,userId);
            rs = stmt.executeQuery();
            //todo
            while(rs.next()){
                userActives.put(rs.getInt(1), rs.getInt(2));
            }
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
        return userActives;
    }

    /**
     * @return userId 最近点击的 50 个商品 prodId
     */
    public HashSet<Long> GetUserActiveByProduct(Long userId){
        HashSet<Long> userActives = new HashSet<>();
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(GET_USER_ACTIVE_BY_USER_GROUPBY_PRODUCT);
            stmt.setLong(1,userId);
            rs = stmt.executeQuery();
            while(rs.next()){
                userActives.add(rs.getLong(1));
            }
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
        return userActives;
    }

    /**
     * @param limit 获取最近活跃的 limit 名用户
     */
    public List<Long> GetLastestAvtiveUsers(int limit){
        List<Long> users = new ArrayList<>();
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt;
            StringBuilder builder = new StringBuilder(GET_LASTEST_ACTIVE_USERS);
            // 2. check topN "-1" means no limit
            if(limit!=-1){
                builder.append(" LIMIT ?");
                stmt = conn.prepareStatement(builder.toString());
                stmt.setInt(1,limit);
            }else {
                stmt = conn.prepareStatement(builder.toString());
            }
            rs = stmt.executeQuery();
            // 4. convert into ProdInfo
            while (rs.next()){
                users.add(rs.getLong(1));
            }

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return users;
    }
}
