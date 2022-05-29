package com.atguigu.gmall.product;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;

@SpringBootTest
public class TTest {

    @Test
    public void exTest(){

        SpelExpressionParser parser = new SpelExpressionParser();

        String str = "#{1+1} + #{#args[0]}";

        Expression expression = parser.parseExpression(
                str,
                ParserContext.TEMPLATE_EXPRESSION
        );

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("args",Arrays.asList(11,22,33));

        Object value = expression.getValue(context, Object.class);

        System.out.println(value);
        System.out.println(value.getClass());
    }
}
