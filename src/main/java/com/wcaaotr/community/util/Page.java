package com.wcaaotr.community.util;

/**
 * 封装分页相关信息
 * @author Connor
 * @create 2021-06-23-20:49
 */
public class Page {

    // 当前页码
    private int currentPage = 1;
    // 页面显示数据条数
    private int limit = 10;
    // 数据总数
    private int rows;
    // 查询路径
    private String path;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if(currentPage >= 1){
            this.currentPage = currentPage;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit >= 1 && limit <= 100){
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows >= 0){
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     * @return
     */
    public int getOffset() {
        // currentPage * limit - limit
        return (currentPage - 1) * limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotalPages() {
        // rows / limit [+1]
        if (rows % limit == 0){
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    public int getFrom(){
        int from = currentPage - 2;
        return from < 1 ? 1 : from;
    }

    public int getTo() {
        int to = currentPage + 2;
        int total = getTotalPages();
        return to > total ? total : to;
    }

}
