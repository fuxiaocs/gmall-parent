package com.atguigu.gmall.model.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartUserIdTo {

    private String userId;
    private String userTempId;
}
