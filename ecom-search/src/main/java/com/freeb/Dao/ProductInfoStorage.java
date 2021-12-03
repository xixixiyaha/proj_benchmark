package com.freeb.Dao;

import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;
import com.freeb.Utils.MarshalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class ProductInfoStorage {

    private static final Logger logger = LoggerFactory.getLogger(ProductInfoStorage.class);
    static String PROD_DB_URL;
    static String PROD_USER;
    static String PROD_PWD;

    StringBuilder prodBuilder = new StringBuilder();

    private static String GET_PRODUCT_BY_CATEGORY = "SELECT * FROM PRODUCT_INFOS WHERE category_id = ?";

    private static String GET_PRODUCT_BY_CATEGORY_ORDER_BY_UPDATE_TIME_DESC = "SELECT * FROM PRODUCT_INFOS WHERE category_id = ? ORDER BY update_time DESC";

    private static String GET_PRODUCT_BY_CATEGORY_ORDER_BY_PRICE_ASC = "SELECT * FROM PRODUCT_INFOS WHERE category_id = ? ORDER BY prod_price ASC";
    private static String GET_PRODUCT_BY_CATEGORY_ORDER_BY_PRICE_DESC = "SELECT * FROM PRODUCT_INFOS WHERE category_id = ? ORDER BY prod_price DESC";

    private static String GET_PRODUCT_BY_CATEGORY_ORDER_BY_SALES_DESC = "SELECT * FROM PRODUCT_INFOS WHERE category_id = ? ORDER BY prod_sales DESC";


    public List<ProductInfo> GetProductByCategory(Integer categoryId, SearchOrder order, Integer topN){
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(PROD_DB_URL, PROD_USER, PROD_PWD)){
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
}