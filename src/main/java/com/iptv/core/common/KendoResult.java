/*    */
package com.iptv.core.common;
/*    */ 
/*    */

import java.util.List;

/*    */
/*    */ 
/*    */ public class KendoResult
/*    */ {
    /*    */   private List data;
    /*    */   private Integer total;
    /*    */   private int page;
    /*    */   private int pageSize;
    /*    */   private int pageNum;

    /*    */
/*    */
    public KendoResult() {
    }

    /*    */
/*    */
    public KendoResult(List data)
/*    */ {
/* 18 */
        this.data = data;
/*    */
    }

    /*    */
/*    */
    public KendoResult(List data, Integer total) {
/* 22 */
        this.data = data;
/* 23 */
        this.total = total;
/*    */
    }

    /*    */
/*    */
    public List getData() {
/* 27 */
        return this.data;
/*    */
    }

    /*    */
/*    */
    public void setData(List data) {
/* 31 */
        this.data = data;
/*    */
    }

    /*    */
/*    */
    public Integer getTotal() {
/* 35 */
        return this.total;
/*    */
    }

    /*    */
/*    */
    public void setTotal(Integer total) {
/* 39 */
        this.total = total;
/*    */
    }

    /*    */
/*    */
    public int getPage() {
/* 43 */
        return this.page;
/*    */
    }

    /*    */
/*    */
    public void setPage(int page) {
/* 47 */
        this.page = page;
/*    */
    }

    /*    */
/*    */
    public int getPageSize() {
/* 51 */
        return this.pageSize;
/*    */
    }

    /*    */
/*    */
    public void setPageSize(int pageSize) {
/* 55 */
        this.pageSize = pageSize;
/*    */
    }

    /*    */
/*    */
    public int getPageNum() {
/* 59 */
        return this.pageNum;
/*    */
    }

    /*    */
/*    */
    public void setPageNum(int pageNum) {
/* 63 */
        this.pageNum = pageNum;
/*    */
    }
/*    */
}


/* Location:              C:\Users\宋羽翔\Desktop\com.jdarc.core.jar!\com\iptv\core\common\KendoResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */