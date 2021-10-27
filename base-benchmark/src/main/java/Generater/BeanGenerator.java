package Generater;

import Bean.User;
import Bean.UserFull;

import java.util.ArrayList;
import java.util.List;

public class BeanGenerator {

    public static List<UserFull> genUserFull(int num){
        if(num<1){
            num = 1;
        }
        List<UserFull> list = new ArrayList<>();
        for(int i=0;i<num;i++){
            list.add(new UserFull((long)i,"BM"+ String.valueOf(i),1,"abc@gmail","12345678910","http://url",0));
        }
        return list;
    }

    public static List<User> genUser(int num){
        if(num<1){
            num = 1;
        }
        List<User> list = new ArrayList<>();
        for(int i=0;i<num;i++){
            list.add(new User((long)i,1,10,"BM"+ String.valueOf(i),"http://url"));
        }
        return list;
    }
}
