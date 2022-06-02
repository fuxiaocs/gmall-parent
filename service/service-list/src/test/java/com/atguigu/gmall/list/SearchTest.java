package com.atguigu.gmall.list;

import com.atguigu.gmall.list.service.GoodsEsService;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;

@SpringBootTest
public class SearchTest {

    @Autowired
    GoodsEsService goodsEsService;

    @Autowired
    ElasticsearchRestTemplate restTemplate;

    @Test
    void searchTest01(){
        SearchParam searchParam = new SearchParam();
        searchParam.setCategory3Id(61L);
        GoodsSearchResultVo goodsSearchResultVo = goodsEsService.searchGoods(searchParam);
    }


    @Test
    void aaa(){
        SearchHits<Goods> goods = restTemplate.search(Query.findAll(), Goods.class, IndexCoordinates.of("goods"));
        for (SearchHit<Goods> searchHit : goods.getSearchHits()) {
            Goods content = searchHit.getContent();
            System.out.println("content = " + content);
        }
    }
}
