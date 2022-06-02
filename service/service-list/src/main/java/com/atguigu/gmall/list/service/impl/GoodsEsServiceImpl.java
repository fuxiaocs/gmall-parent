package com.atguigu.gmall.list.service.impl;

import com.atguigu.gmall.list.dao.GoodsEsDao;
import com.atguigu.gmall.list.service.GoodsEsService;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class GoodsEsServiceImpl implements GoodsEsService {

    @Autowired
    GoodsEsDao goodsEsDao;

    @Autowired
    ElasticsearchRestTemplate esRestTemplate;

    /**
     * 往es中添加 goods
     * @param goods
     */
    @Override
    public void saveGoods(Goods goods) {
        goodsEsDao.save(goods);
    }

    /**
     * 根据 skuId 在es中删除数据
     * @param skuId
     */
    @Override
    public void deleteGoods(Long skuId) {
        goodsEsDao.deleteById(skuId);
    }

    /**
     * 条件查询 商品信息
     * @param param
     * @return
     */
    @Override
    public GoodsSearchResultVo searchGoods(SearchParam param) {

        Query query = this.buildQueryBySearchParam(param);

        SearchHits<Goods> goods = esRestTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));

        GoodsSearchResultVo goodsSearchResultVo = this.buildResponse(goods);

        return goodsSearchResultVo;
    }

    /**
     * 封装完整的查询条件
     * @param param
     * @return
     */
    private Query buildQueryBySearchParam(SearchParam param) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (!ObjectUtils.isEmpty(param.getCategory3Id())) {
            boolQuery.must(QueryBuilders.termQuery("category3Id",param.getCategory3Id()));
        }

        NativeSearchQuery query = new NativeSearchQuery(boolQuery);

        return query;

    }

    /**
     * 处理查询到的结果
     * @param goods
     * @return
     */
    private GoodsSearchResultVo buildResponse(SearchHits<Goods> goods) {
        return null;
    }
}
