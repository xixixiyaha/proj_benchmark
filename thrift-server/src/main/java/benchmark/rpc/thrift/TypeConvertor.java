package thrift;

public class TypeConvertor {

    benchmark.rpc.thrift.User convertUserBean2Thrift(User user){
        return new benchmark.rpc.thrift.User(user.getUuid(),user.getApartId(),user.getAge(),user.getName(),user.getProfile());
    }
    User convertUserThrift2Bean(benchmark.rpc.thrift.User tuser){
        return new User(tuser.uuid,tuser.apartId,tuser.age,tuser.name,tuser.profile);
    }
    benchmark.rpc.thrift.UserFull convertUserFullBean2Thrift(UserFull userFull){
        return new benchmark.rpc.thrift.UserFull(userFull.getUuid(),userFull.getName(),userFull.getSex(),userFull.getEmail(),userFull.getMobile(),userFull.getProfile(),userFull.getStatus(),userFull.getPermissions());
    }
    UserFull convertUserFullThrift2Bean(benchmark.rpc.thrift.UserFull tuserFull){
        UserFull userFull = new UserFull();
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
