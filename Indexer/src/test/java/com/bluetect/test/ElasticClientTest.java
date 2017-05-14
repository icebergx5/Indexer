package com.bluetect.test;

import com.bluetech.core.ElasticClient;
import com.bluetech.core.ElasticClientFactory;
import com.bluetech.core.IndexObject;
import com.bluetech.core.Query;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ElasticClientTest {

    private static String test_index = "test_search";
    private static String CLUSTER_NAME = "mehaz";
    private static final String[] SERVER_ADDRESSES = {"127.0.0.1:9300"};
    
    private ElasticClient elasticClient;

    @Before
    public void before() throws IOException, InterruptedException {
        elasticClient = ElasticClientFactory.create(CLUSTER_NAME, SERVER_ADDRESSES);
    }

    @After
    public void after() {
        //elasticClient.getClient().admin().indices().prepareDelete(test_index).execute().actionGet();
    }


//    @Test
    public void test_index_1() throws InterruptedException {
        IndexObject indexObject = new IndexObject("1");

        indexObject.field("name", "aaaa");
        indexObject.field("age", 32);
        indexObject.field("height", 178);
        indexObject.field("comment", "aaaaaaaaaa");

        boolean test_search = elasticClient.index(test_index, indexObject);
        Assert.assertEquals(true, test_search);

        Thread.sleep(1000);

//        Assert.assertEquals(1L, elasticClient.count(new Query(test_index)));
    }
    
//    @Test
    public void test_update_1() throws InterruptedException {
        test_index_1();
        IndexObject indexObject = new IndexObject("1");

        indexObject.field("comment", "bbbbbb");

        boolean test_search = elasticClient.update(test_index, indexObject);
        Assert.assertEquals(true, test_search);

        Thread.sleep(1000);

       // Assert.assertEquals(1L, elasticClient.count(new Query(test_index)));
    }

//    @Test
    public void test_delete_1() throws InterruptedException {
        test_index_1();

        elasticClient.delete(test_index, "1");

        Thread.sleep(1000);

    }

//    @Test
    public void test_queryFirst() throws InterruptedException {
        index(1000);

        Map<String, Object> item = elasticClient.queryFirst(new Query(test_index).eq("group", "coder"));
        Assert.assertNotNull(item);
        Assert.assertNotNull(item.get("_id"));
        Assert.assertNotNull(item.get("name"));
        Assert.assertNotNull(item.get("group"));
        Assert.assertNotNull(item.get("age"));
        Assert.assertNotNull(item.get("height"));
        Assert.assertNotNull(item.get("comment"));

    }
    
    @Test
    public void test_queryList() throws InterruptedException {
     //   index(1000);

        List<Map<String, Object>> list = elasticClient.queryList(new Query(test_index).eq("group", "coder").limit(0, 5).orderByAsc("name"));

        Assert.assertEquals(5, list.size());
        int id = 0;
        for (Map<String, Object> item : list) {
            Assert.assertEquals(String.format("yasin_%05d", id++), item.get("name"));
        }

    }

/*
    @Test
    public void test_queryPage() throws InterruptedException {
        index(10);

        Page page = elasticClient.queryPage(new Query(test_index).eq("group", "coder").limit(0, 5).orderByAsc("name"));

        Assert.assertEquals(5, page.getItems().size());
        Assert.assertEquals(true, page.isMore());
        int id = 0;
        for (Map<String, Object> item : page.getItems()) {
            Assert.assertEquals(String.format("wens%05d", id++), item.get("name"));
        }


        page = elasticClient.queryPage(new Query(test_index).eq("group", "coder").limit(5, 5).orderByAsc("name"));

        Assert.assertEquals(5, page.getItems().size());
        Assert.assertEquals(false, page.isMore());
        id = 5;
        for (Map<String, Object> item : page.getItems()) {
            Assert.assertEquals(String.format("wens%05d", id++), item.get("name"));
        }

    }
*/

    public void index(int num) throws InterruptedException {
        List<IndexObject> list = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < num; i++) {
            IndexObject indexObject = new IndexObject(String.valueOf(i));
            indexObject.field("name", String.format("yasin_%05d", i));
            indexObject.field("group", "coder");
            indexObject.field("age", r.nextInt(30));
            indexObject.field("height", 165 + r.nextFloat());
            indexObject.field("comment", "kkkkkk" + i);
            list.add(indexObject);
        }

        boolean test_search = elasticClient.index(test_index, list);
//        Assert.assertEquals(true, test_search);

        Thread.sleep(5000);
}
    
    
    
}
