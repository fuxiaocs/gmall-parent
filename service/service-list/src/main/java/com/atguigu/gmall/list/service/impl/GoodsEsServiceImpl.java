package com.atguigu.gmall.list.service.impl;
import com.atguigu.gmall.model.list.*;
import com.google.common.collect.Lists;

import com.atguigu.gmall.list.dao.GoodsEsDao;
import com.atguigu.gmall.list.service.GoodsEsService;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GoodsEsServiceImpl implements GoodsEsService {

    @Autowired
    GoodsEsDao goodsEsDao;

    @Autowired
    ElasticsearchRestTemplate esRestTemplate;

    /**
     * ???es????????? goods
     * @param goods
     */
    @Override
    public void saveGoods(Goods goods) {
        goodsEsDao.save(goods);
    }

    /**
     * ?????? skuId ???es???????????????
     * @param skuId
     */
    @Override
    public void deleteGoods(Long skuId) {
        goodsEsDao.deleteById(skuId);
    }

    /**
     * ???????????? ????????????
     * @param param
     * @return
     */
    @Override
    public GoodsSearchResultVo searchGoods(SearchParam param) {

        Query query = this.buildQueryBySearchParam(param);

        SearchHits<Goods> goods = esRestTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));

        GoodsSearchResultVo goodsSearchResultVo = this.buildResponse(goods,param);

        return goodsSearchResultVo;
    }

    /**
     * ????????????
     * @param skuId
     * @param score
     */
    @Override
    public void updateHotScore(Long skuId, Long score) {
        Optional<Goods> byId = goodsEsDao.findById(skuId);
        Goods goods = byId.get();
        goods.setHotScore(score);

        goodsEsDao.save(goods);
    }

    /**
     * ???????????????????????????
     * @param param
     * @return
     */
    private Query buildQueryBySearchParam(SearchParam param) {

        NativeSearchQuery query = null;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (!ObjectUtils.isEmpty(param.getCategory1Id())) {
             boolQuery.must(QueryBuilders.termQuery("category1Id",param.getCategory1Id()));
        }
        if (!ObjectUtils.isEmpty(param.getCategory2Id())) {
            boolQuery.must(QueryBuilders.termQuery("category2Id",param.getCategory2Id()));
        }
        if (!ObjectUtils.isEmpty(param.getCategory3Id())) {
            boolQuery.must(QueryBuilders.termQuery("category3Id",param.getCategory3Id()));
        }

        if (!ObjectUtils.isEmpty(param.getTrademark())) {
            String[] split = param.getTrademark().split(":");
            boolQuery.must(QueryBuilders.termQuery("tmId",split[0]));
        }

        if (!ObjectUtils.isEmpty(param.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("title",param.getKeyword()));
        }

        if (!ObjectUtils.isEmpty(param.getProps()) && param.getProps().length > 0) {
            for (String prop : param.getProps()) {
                String[] split = prop.split(":");
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId",split[0]));
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrValue",split[1]));
                boolQuery.must(QueryBuilders.nestedQuery("attrs",nestedBoolQuery, ScoreMode.None));
            }
        }
        query = new NativeSearchQuery(boolQuery);

        // ??????
        if (!ObjectUtils.isEmpty(param.getOrder())) {
            String[] split = param.getOrder().split(":");
            String sortField;
            switch (split[0]) {
                case "1": sortField = "hotScore"; break;
                case "2": sortField = "price"; break;
                default: sortField = "hotScore";
            }
            Sort sort = Sort.by(Sort.Direction.fromString(split[1]), sortField);
            query.addSort(sort);
        }

        //??????
        Pageable pageable = PageRequest.of(param.getPageNo() - 1,param.getPageSize());
        query.setPageable(pageable);

        //??????????????????
        if (!ObjectUtils.isEmpty(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title").preTags("<span style='color:red'>").postTags("</span>");

            HighlightQuery highlightQuery = new HighlightQuery(highlightBuilder);
            query.setHighlightQuery(highlightQuery);
        }

        //?????? ??????
        //????????????
        TermsAggregationBuilder tmAgg = AggregationBuilders.terms("tmIdAgg").field("tmId").size(100);
        tmAgg.subAggregation(AggregationBuilders.terms("tmNameAgg").field("tmName").size(1));
        tmAgg.subAggregation(AggregationBuilders.terms("tmLogAgg").field("tmLogoUrl").size(1));
        query.addAggregation(tmAgg);

        //??????????????????
        NestedAggregationBuilder attrsAgg = AggregationBuilders.nested("attrsAgg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(100);
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1));
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(100));

        attrsAgg.subAggregation(attrIdAgg);

        query.addAggregation(attrsAgg);

        return query;
    }

    /**
     * ????????????????????????
     * @param goods
     * @param param
     * @return
     */
    private GoodsSearchResultVo buildResponse(SearchHits<Goods> goods,SearchParam param) {
        GoodsSearchResultVo goodsSearchResultVo = new GoodsSearchResultVo();
        //???????????????????????????
        goodsSearchResultVo.setSearchParam(param);

        //?????????????????????
        if (param.getProps() != null && param.getProps().length > 0){
            ArrayList<SearchAttr> searchAttrs = new ArrayList<>();
            for (String prop : param.getProps()) {
                String[] split = prop.split(":");
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrId(Long.parseLong(split[0]));
                searchAttr.setAttrValue(split[1]);
                searchAttr.setAttrName(split[2]);
                searchAttrs.add(searchAttr);
            }
            goodsSearchResultVo.setPropsParamList(searchAttrs);
        }

        //???????????????
        if (!StringUtils.isEmpty(param.getTrademark())){
            String[] split = param.getTrademark().split(":");
            goodsSearchResultVo.setTrademarkParam("?????? : " + split[1]);
        }

        //url??????
        String urlParam = buildUrlParam(param);
        goodsSearchResultVo.setUrlParam(urlParam);

        //????????? :??????????????????
        List<SearchResponseTmVo> searchResponseTmVoList = this.buildSearchTm(goods);
        goodsSearchResultVo.setTrademarkList(searchResponseTmVoList);

        //????????? : ????????????????????????
        List<SearchResponseAttrVo> searchResponseAttrVoList = this.buildSearchAttr(goods);
        goodsSearchResultVo.setAttrsList(searchResponseAttrVoList);

        //????????????
        SearchResponseOmVo searchResponseOmVo = new SearchResponseOmVo();
        String[] split = param.getOrder().split(":");
        searchResponseOmVo.setType(split[0]);
        searchResponseOmVo.setSort(split[1]);
        goodsSearchResultVo.setOrderMap(searchResponseOmVo);

        //??????????????????????????????
        ArrayList<Goods> goodsList = new ArrayList<>();
        for (SearchHit<Goods> searchHit : goods.getSearchHits()) {
            Goods goodsItem = searchHit.getContent();
            if (!StringUtils.isEmpty(param.getKeyword())) {
                String title = searchHit.getHighlightField("title").get(0);
                goodsItem.setTitle(title);
            }
            goodsList.add(goodsItem);
        }
        goodsSearchResultVo.setGoodsList(goodsList);


        //?????????
        goodsSearchResultVo.setPageNo(param.getPageNo());
        // ?????????
        Long totalPage = goods.getTotalHits() % param.getPageSize() == 0 ?
                goods.getTotalHits() / param.getPageSize() :
                (goods.getTotalHits() / param.getPageSize() + 1);
        goodsSearchResultVo.setTotalPages(totalPage.intValue());

        return goodsSearchResultVo;
    }

    /**
     * ?????????????????? ??????????????????????????????
     * @param goods
     * @return
     */
    private List<SearchResponseAttrVo> buildSearchAttr(SearchHits<Goods> goods) {
        ArrayList<SearchResponseAttrVo> searchResponseAttrVos = new ArrayList<>();
        ParsedNested attrsAgg = goods.getAggregations().get("attrsAgg");
        ParsedLongTerms attrIdAgg = attrsAgg.getAggregations().get("attrIdAgg");
        for (Terms.Bucket bucketItem : attrIdAgg.getBuckets()) {
            SearchResponseAttrVo searchResponseAttrVo = new SearchResponseAttrVo();

            //attrId
            searchResponseAttrVo.setAttrId(bucketItem.getKeyAsNumber().longValue());

            //attrValue
            ParsedStringTerms attrValueAgg = bucketItem.getAggregations().get("attrValueAgg");
            ArrayList<String> attrValueList = new ArrayList<>();
            for (Terms.Bucket bucket : attrValueAgg.getBuckets()) {
                attrValueList.add(bucket.getKeyAsString());
            }
            searchResponseAttrVo.setAttrValueList(attrValueList);

            //attrName
            ParsedStringTerms attrNameAgg = bucketItem.getAggregations().get("attrNameAgg");
            searchResponseAttrVo.setAttrName(attrNameAgg.getBuckets().get(0).getKeyAsString());

            searchResponseAttrVos.add(searchResponseAttrVo);
        }
        return searchResponseAttrVos;
    }

    /**
     * ?????????????????? ????????????????????????
     * @param goods
     * @return
     */
    private List<SearchResponseTmVo> buildSearchTm(SearchHits<Goods> goods) {
        ArrayList<SearchResponseTmVo> searchResponseTmVos = new ArrayList<>();
        ParsedLongTerms tmIdAgg = goods.getAggregations().get("tmIdAgg");
        for (Terms.Bucket bucketItem : tmIdAgg.getBuckets()) {
            SearchResponseTmVo searchResponseTmVo = new SearchResponseTmVo();
            searchResponseTmVo.setTmId(bucketItem.getKeyAsNumber().longValue());

            Aggregations aggregations = bucketItem.getAggregations();

            ParsedStringTerms tmNameAgg = aggregations.get("tmNameAgg");
            searchResponseTmVo.setTmName(tmNameAgg.getBuckets().get(0).getKeyAsString());

            ParsedStringTerms tmLogAgg = aggregations.get("tmLogAgg");
            searchResponseTmVo.setTmLogoUrl(tmLogAgg.getBuckets().get(0).getKeyAsString());

            searchResponseTmVos.add(searchResponseTmVo);
        }
        return searchResponseTmVos;
    }

    /**
     * ???????????? ??????url
     * @param param
     * @return
     */
    private String buildUrlParam(SearchParam param) {
        StringBuilder urlParamSb = new StringBuilder("list.html?");
        if (param.getCategory1Id() != null) {
            urlParamSb.append("category1Id=" + param.getCategory1Id() + "&");
        }
        if (param.getCategory2Id() != null) {
            urlParamSb.append("category2Id=" + param.getCategory2Id() + "&");
        }
        if (param.getCategory3Id() != null) {
            urlParamSb.append("category3Id=" + param.getCategory3Id() + "&");
        }

        if (!StringUtils.isEmpty(param.getTrademark())) {
            urlParamSb.append("trademark=" + param.getTrademark() + "&");
        }
        if (!StringUtils.isEmpty(param.getKeyword())) {
            urlParamSb.append("keyWord=" + param.getKeyword() + "&");
        }
        if (param.getProps() != null && param.getProps().length > 0){
            for (String prop : param.getProps()) {
                urlParamSb.append("props=" + prop + "&");
            }
        }
        return urlParamSb.toString();
    }
}
