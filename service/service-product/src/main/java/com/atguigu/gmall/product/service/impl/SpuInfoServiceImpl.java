package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Slf4j
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
    implements SpuInfoService{

    @Autowired
    SpuInfoMapper spuInfoMapper;

    @Autowired
    SpuImageService spuImageService;

    @Autowired
    SpuSaleAttrService spuSaleAttrService;

    @Autowired
    SpuSaleAttrValueService spuSaleAttrValueService;

    /**
     * 添加 SpuInfo 信息
     * @param spuInfo
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        log.info("spu保存信息:{}",spuInfo);
        //往spu_info表中添加信息
        spuInfoMapper.insert(spuInfo);

        //添加成功后 会有自增id
        Long spuId = spuInfo.getId();

        //往spu_image表中添加信息
        //需要封装给spu_id
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        for (SpuImage spuImage : spuImageList) {
            spuImage.setSpuId(spuId);
            spuImageService.save(spuImage);
        }

        //往spu_sale_attr表中添加信息
        //需要封装spu_id
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
            spuSaleAttr.setSpuId(spuId);
            spuSaleAttrService.save(spuSaleAttr);
            //往spu_sale_attr_value表中添加信息
            //需要封装 spu_id 和 sale_attr_name
            String saleAttrName = spuSaleAttr.getSaleAttrName();
            List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                spuSaleAttrValue.setSpuId(spuId);
                spuSaleAttrValue.setSaleAttrName(saleAttrName);
                spuSaleAttrValueService.save(spuSaleAttrValue);
            }
        }
    }
}




