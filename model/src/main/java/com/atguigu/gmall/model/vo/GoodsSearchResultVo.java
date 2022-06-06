package com.atguigu.gmall.model.vo;

import com.atguigu.gmall.model.list.*;
import lombok.Data;

import java.util.List;

@Data
public class GoodsSearchResultVo {

    private SearchParam searchParam; //当时检索的所有参数
    private List<SearchAttr> propsParamList; //平台属性面包屑
    private String trademarkParam; //品牌面包屑
    private String urlParam;  //url参数
    private List<SearchResponseTmVo> trademarkList; //检索区 :品牌列表信息
    private List<SearchResponseAttrVo> attrsList; //检索区 : 平台属性列表信息
    private SearchResponseOmVo orderMap; //排序条件
    private List<Goods> goodsList; //检索到的所有商品集合
    private Integer pageNo;  //当前页
    private Integer totalPages;  // 总页码
}
