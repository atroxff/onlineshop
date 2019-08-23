package cn.ff.onlineshop.constant;

import java.util.HashMap;
import java.util.Map;

public class Sysconfig {
    public static final Integer num=0;
    public static class ERROR{
        public static final String ERROR="";
    }
    public static class URL{
        public static final String CONTEXTPATH="http://localhost:8080";
    }

    public static class Category{
        public static int Count=5;
    }

    public static class SearchType{
        public static Map<String, String> map = new HashMap<String, String>(){
            private static final long serialVersionUID = 1L;
            {
                put("0", "商品编号");
                put("1", "商品名称");
                put("2", "商品分类");
            }
        };
    }

    public static class Upload{
        // E:\workspace\pic\
        public static String url = "E:\\workspace\\pic\\";

    }
}
