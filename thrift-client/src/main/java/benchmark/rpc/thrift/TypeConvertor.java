package benchmark.rpc.thrift;
import Bean.*;

import java.io.Closeable;

public class TypeConvertor  {

    public static benchmark.rpc.thrift.User convertUserBean2Thrift(Bean.User user){
        return new benchmark.rpc.thrift.User(user.getUuid(),user.getApartId(),user.getAge(),user.getName(),user.getProfile());
    }
    public static Bean.User convertUserThrift2Bean(benchmark.rpc.thrift.User tuser){
        return new Bean.User(tuser.uuid,tuser.apartId,tuser.age,tuser.name,tuser.profile);
    }
    public static benchmark.rpc.thrift.UserFull convertUserFullBean2Thrift(Bean.UserFull userFull){
        return new benchmark.rpc.thrift.UserFull(userFull.getUuid(),userFull.getName(),userFull.getSex(),userFull.getEmail(),userFull.getMobile(),userFull.getProfile(),userFull.getStatus(),userFull.getPermissions());
    }
    public static Bean.UserFull convertUserFullThrift2Bean(benchmark.rpc.thrift.UserFull tuserFull){
        Bean.UserFull userFull = new Bean.UserFull();
        userFull.setUuid(tuserFull.getUuid());
        userFull.setName(tuserFull.getName());
        userFull.setSex(tuserFull.getSex());
        userFull.setEmail(tuserFull.getEmail());
        userFull.setMobile(tuserFull.getMobile());
        userFull.setProfile(tuserFull.getProfile());
        userFull.setStatus(tuserFull.getStatus());
        userFull.setPermissions(tuserFull.getPermissions());
        return userFull;
    }
}
