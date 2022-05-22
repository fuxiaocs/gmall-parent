package com.atguigu.gmall.model.to;

import com.atguigu.gmall.model.base.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class CategoryAndChild {

    private Long categoryId;
    private String categoryName;
    private List<CategoryAndChild> categoryChild;
}
