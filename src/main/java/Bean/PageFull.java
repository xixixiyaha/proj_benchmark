package Bean;

import java.util.List;

public class PageFull<T> {

    private int pageNo;
    private int total;
    private List<T> result;
    // NOTICE: 联合查询
    private List<Long> readUsers;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public List<Long> getReadUsers(){return readUsers;}

    public void setReadUsers(List<Long> readedUsers){this.readUsers = readedUsers;}

}
