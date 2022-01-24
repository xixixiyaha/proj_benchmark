package com.freeb.Service;

import Bean.User;
import Bean.UserFull;
import CommuteUtils.HttpClientUtils;
import CommuteUtils.JsonUtils;
import com.fasterxml.jackson.databind.JavaType;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserServiceClientJsonImpl implements UserService {

    private static String URL_CREATE_USER = "http://benchmark-server:8080/user-create";
    private static String URL_GET_USER = "http://benchmark-server:8080/user-get=?";

    private final CloseableHttpClient client;
    private final ObjectMapper objectMapper = JsonUtils.objectMapper;

    private final JavaType userType = objectMapper.getTypeFactory().constructParametricType(UserFull.class,List.class);

    public UserServiceClientJsonImpl(int concurrency){
        client = HttpClientUtils.createHttpClient(concurrency);
    }

    @Override
    public boolean createUser(UserFull userFull) {
        try{
            byte[] bytes = objectMapper.writeValueAsBytes(userFull);
            HttpPost request = new HttpPost(URL_CREATE_USER);
            HttpEntity entity = EntityBuilder.create().setBinary(bytes).build();
            request.setEntity(entity);

            CloseableHttpResponse response = client.execute(request);

            String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            return "true".equals(result);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public User getUser(Long uuid) {
        try{
            String url = URL_GET_USER+uuid;

            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = client.execute(request);

            byte[] bytes = EntityUtils.toByteArray(response.getEntity());

            return objectMapper.readValue(bytes,User.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public List<User> getUsers(List<Long> uuids) {
//        return null;
//    }
//
//    @Override
//    public UserFull getUserFull(Long uuid) {
//        return null;
//    }
//
//    @Override
//    public List<UserFull> getUserFulls(List<Long> uuids) {
//        return null;
//    }
//
//    @Override
//    public boolean existUser(Long uuid) {
//        return false;
//    }

    public static void main(String[] args) throws Exception{
        UserService userService = new UserServiceClientJsonImpl(256);
    }
}
