package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
        implements BaseAttrInfoService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueService baseAttrValueService;

    /**
     * 根据分类id 查询平台属性 键 与 值
     *
     * @param c1Id
     * @param c2Id
     * @param c3Id
     * @return
     */
    @Override
    public List<BaseAttrInfo> findAttrInfoAndValueByCategoryId(Long c1Id, Long c2Id, Long c3Id) {
        return baseAttrInfoMapper.selectAttrInfoAndValueByCategoryId(c1Id, c2Id, c3Id);
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        //添加平台属性
        baseAttrInfoMapper.insert(baseAttrInfo);
        //添加平台属性的值
        Long attrId = baseAttrInfo.getId();
        for (BaseAttrValue baseAttrValue : baseAttrInfo.getAttrValueList()) {
            baseAttrValue.setAttrId(attrId);
            baseAttrValueService.save(baseAttrValue);
        }
    }


    @Override
    public void saveOrUpdateAttrInfo(BaseAttrInfo baseAttrInfo) {
        Long attrInfoId = baseAttrInfo.getId();

        if (attrInfoId == null){
            this.saveAttrInfo(baseAttrInfo);
        } else {
            this.updateAttrInfo(baseAttrInfo);
        }
    }

    @Transactional
    @Override
    public void updateAttrInfo(BaseAttrInfo baseAttrInfo) {

        baseAttrInfoMapper.updateById(baseAttrInfo);

        baseAttrValueService.updateAttrValue(baseAttrInfo);

    }
}




