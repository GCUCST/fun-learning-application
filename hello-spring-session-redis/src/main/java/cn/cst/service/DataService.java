package cn.cst.service;


import cn.cst.entities.MyData;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = {"dataService"})
public class DataService {


    @Cacheable(key = "#code+#name",sync = false,unless="#result == null")
    public List<MyData> testMetaKey(String code, String name){
        MyData data = new MyData("1001","哈哈哈1001");
        MyData data1 = new MyData("1002","哈哈哈1002");
        MyData data2 = new MyData("1003","哈哈哈1003");
        ArrayList<MyData> list = new ArrayList<>();
        list.add(data);
        list.add(data1);
        list.add(data2);
        System.out.println("请求进入这里");

        return list;
    }

    @CacheEvict(key = "#code+#name")
    public List<MyData> cacheEvict(String code, String name){
        ArrayList<MyData> list = new ArrayList<>();
        System.out.println("请求进入这里cacheEvict");
        return list;
    }



}
