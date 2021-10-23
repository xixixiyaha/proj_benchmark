package Bean;

import java.util.List;

public class Page<T> {
    private static final long serialVersionUID = -10101010101010L;

    private int typeNo;
    private int pageNo;
    private String pageToken;
    private int total;
    private List<T> result;

    public int getTypeNo() {
        return typeNo;
    }

    public void setTypeNo(int typeNo){this.typeNo=typeNo;}

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
}
