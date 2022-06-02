package com.atguigu.gmall.model.list;

import lombok.Data;

@Data
public class SearchResponseOmVo {

    private String type; //排序方法 1综合 , 2价格
    private String sort;  //排序规则
}
