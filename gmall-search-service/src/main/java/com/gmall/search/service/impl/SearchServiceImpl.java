package com.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.bean.PmsSearchParam;
import com.gmall.bean.PmsSearchSkuInfo;
import com.gmall.bean.PmsSkuAttrValue;
import com.gmall.bean.PmsSkuInfo;
import com.gmall.service.SearchService;
import com.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/3/4 15:06
 * @Describe
 * @Version 1.0
 **/

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private JestClient jestClient;

    /*@Reference
    private SkuService skuService;*/

    @Override
    public List<PmsSearchSkuInfo> getSearchInfo(PmsSearchParam pmsSearchParam){

        String dsl = getSearchQueryDsl(pmsSearchParam);
        System.out.println(dsl);

        List<PmsSearchSkuInfo> list = new ArrayList<>();
        Search search = new Search.Builder(dsl).addIndex("gmall").addType("PmsSkuInfo").build();
        SearchResult result = null;
        try {
            result = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = result.getHits(PmsSearchSkuInfo.class);
        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo pmsSearchSkuInfo = hit.source;
            list.add(pmsSearchSkuInfo);
        }
        return list;
    }

    @Override
    public void updateSkuInfoData() {

       /* List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        List<PmsSkuInfo> pmsSkuInfos = skuService.getAllPmsSkuInfo();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            Index put = new Index.Builder(pmsSearchSkuInfo).index("gmall").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId()).build();
            try {
                jestClient.execute(put);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public String getSearchQueryDsl(PmsSearchParam pmsSearchParam){
        List<PmsSkuAttrValue> skuAttrValues = pmsSearchParam.getSkuAttrValueList();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (skuAttrValues != null){
            for (PmsSkuAttrValue skuAttrValue : skuAttrValues) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",skuAttrValue.getValueId());
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }
        if (!StringUtils.isEmpty(pmsSearchParam.getKeyword())){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",pmsSearchParam.getKeyword());
            boolQueryBuilder.must(matchQueryBuilder);
        }
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        if (!StringUtils.isEmpty(catalog3Id)){
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("id", SortOrder.DESC);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(20);
        searchSourceBuilder.highlight();
        return searchSourceBuilder.toString();
    }
}
