package com.wcaaotr.community.util;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;


/**
 * @author Connor
 * @create 2021-06-23-21:12
 */
@Component
public class RedisPageUtil {

    public static PageInfo getPageInfo(int total, int pageSize, int pageNum) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotal(total);
        int pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        pageInfo.setPages(pages);
        pageInfo.setPageNum(pageNum);
        return pageInfo;
    }

}
