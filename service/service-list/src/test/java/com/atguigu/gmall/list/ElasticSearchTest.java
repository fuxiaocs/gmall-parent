package com.atguigu.gmall.list;

import com.atguigu.gmall.common.util.JSONs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class ElasticSearchTest {

    @Autowired
    ElasticsearchRestTemplate esTemplate;

    @Test
    void insertTest(){
        Hello hello = new Hello("拂晓成双999",111);
        Hello aaa = esTemplate.save(hello, IndexCoordinates.of("aaa"));
        System.out.println(aaa);
//        Hello zzz = new Hello("9999", "zzz", 222);
//        IndexQuery indexQuery = new IndexQuery();
////        indexQuery.setId("10");
//        indexQuery.setObject(zzz);
//        String aaa = esTemplate.index(indexQuery, IndexCoordinates.of("aaa"));
//        System.out.println("aaa = " + aaa);
    }

    @Test
    void deleteTest(){
        String aaa = esTemplate.delete("1", IndexCoordinates.of("aaa"));
        System.out.println(aaa);
    }

    @Test
    void updateTest(){
//        Hello hello = new Hello("99","wyj999",99);
        HashMap<Object, Object> hello = new HashMap<>();
        hello.put("id","100");
        String json = JSONs.toStr(hello);
        Document parse = Document.parse(json);
        UpdateQuery updateQuery = UpdateQuery.builder("10")
                .withDocAsUpsert(false)
                .withDocument(parse)
                .build();
        UpdateResponse aaa = esTemplate.update(updateQuery, IndexCoordinates.of("aaa"));
        System.out.println(aaa.getResult());
    }

    @Test
    void selectTest(){
//        Hello aaa = esTemplate.get("10", Hello.class, IndexCoordinates.of("aaa"));
//        System.out.println(aaa);


        Query all = Query.findAll();
        SearchHits<Hello> aaa1 = esTemplate.search(all,Hello.class, IndexCoordinates.of("aaa"));
        List<SearchHit<Hello>> searchHits = aaa1.getSearchHits();
        for (SearchHit<Hello> searchHit : searchHits) {
            Hello content = searchHit.getContent();
            String id = searchHit.getId();
            System.out.println(id);
            System.out.println(content);
        }
    }
}

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
class Hello{
//    private String id;
    private String name;
    private Integer age;
}
