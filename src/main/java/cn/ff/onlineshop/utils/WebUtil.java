package cn.ff.onlineshop.utils;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.springframework.stereotype.Component;


/**
 * 工具类
 */
@Component
public class WebUtil {
    public static final Integer userId=0;
    /**
     * 表单数据封装到bean  从src到dest
     * @param src
     * @param dest
     */
    public static void  copyBean(Object src,Object dest){

        ConvertUtils.register(new Converter(){
            //自定义日期格式转换
            public Object convert(Class type, Object value) {
                if(value==null){
                    return null;
                }
                String str= (String) value;
                if(str.trim().equals("")){
                    return null;
                }
                SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return df.parse(str);
                } catch (ParseException e) {
                    throw new RuntimeException(e);

                }
            }

        }, Date.class);

        try {
            BeanUtils.copyProperties(dest, src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
