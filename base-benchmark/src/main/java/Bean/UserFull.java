package Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class UserFull implements Serializable {
    // NOTICE uuid 用来进行联合查询

    /**
     * uuid : 12
     * name : Dary
     * sex : 2
     * email : address
     * mobile : 12345
     * profile : url of profile
     * permissions : [1,2,3,4,5]
     * status : 1
     * createTime : 12345
     * updateTime : 12345
     */

    // TODO@ low priority   1: 增加变量类型 + 类的长度

    private long uuid;
    private String name;
    private int sex;
    private String email;
    private String mobile;
    private String profile;
    private int status;
//    private Date createTime;
//    private Date updateTime;
    private List<Long> permissions;

    public UserFull(){
    }
    public UserFull(long uuid,String name,int sex,String email,String mobile,String profile,int status){
        super();
        this.uuid=uuid;
        this.name=name;
        this.sex = sex;
        this.email=email;
        this.mobile = mobile;
        this.profile = profile;
        this.status=status;
        this.permissions = new ArrayList<>();
    }

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
//
//    public Date getUpdateTime() {
//        return updateTime;
//    }
//
//    public void setUpdateTime(Date updateTime) {
//        this.updateTime = updateTime;
//    }

    public List<Long> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Long> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "UserFull [uuid" + uuid +" , name = "+ name +"]";
    }


}
