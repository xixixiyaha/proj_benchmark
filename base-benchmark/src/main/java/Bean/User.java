package Bean;


public class User {
    /**
     * uuid : 1234567
     * apartId : 20
     * age : 24
     * name : “Lin”
     * profile : https://qs.com/2345
     */

    private long uuid;
    private int apartId;
    private int age;
    private String name;
    private String profile;

    public User(){

    }

    public User(long uuid,int apartId,int age,String name,String profile){
        super();
        this.uuid=uuid;
        this.apartId = apartId;
        this.age = age;
        this.name=name;
        this.profile = profile;
    }

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public int getApartId() {
        return apartId;
    }

    public void setApartId(int apartId) {
        this.apartId = apartId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
