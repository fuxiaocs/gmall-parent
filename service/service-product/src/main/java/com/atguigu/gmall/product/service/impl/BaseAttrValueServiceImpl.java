package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class BaseAttrValueServiceImpl extends ServiceImpl<BaseAttrValueMapper, BaseAttrValue>
    implements BaseAttrValueService{

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrValue> findAttrValueListByAttrId(Long attrId) {
        QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_id",attrId);
        return baseAttrValueMapper.selectList(queryWrapper);
    }

    @Transactional
    @Override
    public void updateAttrValue(BaseAttrInfo baseAttrInfo) {

        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

        //1.先把 带来集合中没有 , 库中存在的id 删掉
        //存带来的id
        List<Long> attrIdList = new ArrayList<>();
        for (BaseAttrValue baseAttrValue : attrValueList) {
            Long id = baseAttrValue.getId();
            if (id != null) attrIdList.add(baseAttrValue.getId());
        }

        Long attrId = baseAttrInfo.getId();

        QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_id", attrId);
        if (attrIdList.size() > 0){
            queryWrapper.notIn("id",attrIdList);
        }
        baseAttrValueMapper.delete(queryWrapper);


        for (BaseAttrValue baseAttrValue : attrValueList) {
            Long id = baseAttrValue.getId();
            //2.带来的对象没有id 添加
            if (id == null){
                baseAttrValue.setAttrId(attrId);
                baseAttrValueMapper.insert(baseAttrValue);
            } else {
                //3.带来的有id 修改
                baseAttrValueMapper.updateById(baseAttrValue);
            }
        }
    }
}




