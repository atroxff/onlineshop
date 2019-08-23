package cn.ff.onlineshop.demoTest;

import cn.ff.onlineshop.constant.ConfigParam;
import cn.ff.onlineshop.constant.Sysconfig;
import cn.ff.onlineshop.mapper.pojo.User;
import cn.ff.onlineshop.mapper.service.MybatisUserService;
import cn.ff.onlineshop.mapper.service.UserBatchService;
import cn.ff.onlineshop.tools.JSONObject;
import cn.ff.onlineshop.utils.FileUtil;
import cn.ff.onlineshop.utils.SysUtil;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
public class JsonTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    //Logger logger = LoggerFactory.getLogger(JsonTest.class);

    @Autowired
    ConfigParam configParam;
    @Autowired
    MybatisUserService mybatisUserService;
    @Autowired
    UserBatchService userBatchService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    /*html测试*/
    @RequestMapping("/test/page.do")
    public String test(Model model){
        //return "commons/header";
        //return "login/register1";
        //return "login/login1";
        //return "show/index";
        //return "show/product_list";
        //return "admin/home";

        //return "model1/login";
        //return "model1/404";
        //return "ajax/index";
        model.addAttribute("msg", "初始信息");

        return "show/search";
    }

    /*ajax测试*/
    @RequestMapping(value = "/test/ajax.do",method = RequestMethod.POST)
    public @ResponseBody Object ajax(){
        JSONObject data = new JSONObject();
        data.put("msg", "ajax返回的信息");
        return data;
    }

    /*文件上传-get*/
    @GetMapping("/test/upload.do")
    public String upload(Model model, HttpServletRequest request){
        return "test/upload";
    }

    /*文件上传-post*/
    @PostMapping("/test/upload.do")
    public String Postupload(Model model, HttpServletRequest request, MultipartFile file){
        try {
            //上传目录地址
            //String uploadDir = request.getSession().getServletContext().getRealPath("/") + "upload/";
            String uploadDir = Sysconfig.Upload.url;
            logger.info("上传目录地址:"+uploadDir);

            //目录不存在 自动创建新文件夹
            File dir = new File(uploadDir);
            if(!dir.exists()){
                dir.mkdir();
            }

            //文件后缀
            String suffix=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            //上传文件名
            String filename = SysUtil.getUUID();
            //服务端保存的文件对象
            File serverFile = new File(uploadDir + filename);
            //上传的文件写到服务器端文件内
            file.transferTo(serverFile);
        } catch (IOException e) {

            logger.info("上传失败："+e.toString());
            e.printStackTrace();
        }
        logger.info("上传成功");
        model.addAttribute("msg","上传成功");
        return "message";
    }

    /*restful风格*/
    @GetMapping("/test/rest/{id}/{name}/{sex}")
    public @ResponseBody Object rest(
            @PathVariable("id") String id,
            @PathVariable("name") String name,
            @PathVariable("sex") String sex){
        System.out.println("id:"+id+"name:"+name+"sex:"+sex);
        Map<String,String> map =new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("sex", sex);
        return map;
    }


    /*读取properties  通过FileInputStream和InputStreamReader*/
    @RequestMapping(value = "/test/prop.do")
    public @ResponseBody Object url(){
        Map<String, Object> map = new LinkedHashMap();
        Map<String, Object> config = new LinkedHashMap();

        //获取配置文件路径
        URL url = this.getClass().getClassLoader().getResource("config.properties");
        String configPath = "";
        if(url!=null) {
             configPath = url.getPath();
        }
        logger.info("configPath:"+configPath);

        //获取配置文件属性
        File configFile = new File(configPath);

        String resoucePath = configFile.getParentFile()+File.separator+"resouces";
        logger.info("resoucePath:"+resoucePath);
        String staticPath=resoucePath+File.separator+"static";
        logger.info("staticPath:"+staticPath);

        FileInputStream inputStream = null;
        InputStreamReader isr = null;
        try {
            inputStream = new FileInputStream(configFile);
            isr = new InputStreamReader(inputStream, "UTF-8");

            Properties configProps = new Properties();
            configProps.load(isr);

            //将properties中的属性写入java对象中，可以对方法进行封装
            Set entries = configProps.keySet();
            Iterator iterator = entries.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = configProps.getProperty(key);
                config.put(key, value);
            }
            logger.info("config:"+config);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                   try {
                       inputStream.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
            }
            if (isr != null) {
                   try {
                       isr.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
            }
        }
        return config;
    }

    /*读取properties springboot启动后就使用注解自动装配  适用于一些启动配置*/
    @RequestMapping(value = "/test/prop_anno.do")
    public @ResponseBody Object propanno(){
        Map<String, Object> map = new HashMap<>();
        //从config.properties获取
        map.put("config",configParam.getApplication());
        //从config1.properties获取
        map.put("config1",configParam.getApp());
        return  map;
    }

    /*读取properties PropertiesConfiguration 要引入依赖*/
    @RequestMapping(value = "/test/prop_conf.do")
    public @ResponseBody Object propconf() throws ConfigurationException {
        PropertiesConfiguration config = new PropertiesConfiguration("config.properties");
        PropertiesConfiguration config1 = new PropertiesConfiguration("config1.properties");
        String ip = config.getString("ip");
        String app = config1.getString("app");
        logger.info("ip:"+ip+",app:"+app);
        return "ip:"+ip+",app:"+app;
    }

    /*文件路径*/
    @RequestMapping(value = "/test/filepath.do")
    public @ResponseBody Object filepath(){
        //获取应用所在目录
        String directory = System.getProperty("user.dir");
        logger.info("directory:"+directory);
        //获取resource下static目录
        try {
            File file = ResourceUtils.getFile("classpath:" + "application.properties");
            String resourcesPath=file.getParent();//classes目录
            logger.info("resourcesPath:"+resourcesPath);
            String staticPath = resourcesPath + File.separator + "static";
            logger.info("staticPath:"+staticPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "true";
    }

    /*文件操作*/
    @RequestMapping(value = "/test/file.do")
    public @ResponseBody Object file(){

        //resources目录下static路径
        URL url = this.getClass().getClassLoader().getResource("application.properties");
        String path = url.getPath();
        File file = new File(path);
        String staticPath = file.getParentFile() + File.separator +"static";
        logger.info("staticPath:" + staticPath);
        try {
            File file2 = FileUtil.getFileFromClasspath("application.properties");
            String staticPath2 = file2.getParentFile() + File.separator +"static";
            logger.info("staticpath2:" + staticPath2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //处理products中的图片
        Set<String> allFile = new HashSet<>();
        File products = new File(staticPath + File.separator + "products");
        if (products.exists() && products.isDirectory()) {//products是一个目录
            if (products.list() != null) {//products目录不为空
                 allFile = FileUtil.getAllFile(products.getPath(), false);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("pic",allFile);
        return map;

    }

    /*mybatis 查询数据*/
    @RequestMapping(value = "/test/mybatis/user/{id}",method = RequestMethod.GET)
    public @ResponseBody Object mybatis(@PathVariable("id") long id){
        return mybatisUserService.getUser(id);
    }

    /*mybatis 插入数据*/
    @RequestMapping(value = "/test/mybatis/user/{userName}/{note}",method = RequestMethod.GET)
    public @ResponseBody Object insertUser(@PathVariable("userName")String userName ,
                                           @PathVariable("note")String note){
        User user = new User();
        user.setUserName(userName);
        user.setNote(note);
        int update = mybatisUserService.insertUser(user);
        Map<String, Object> result = new HashMap<>();
        result.put("success", update==1);
        result.put("user", user);
        return result;
    }

    /*测试传播行为*/
    @RequestMapping(value = "/test/mybatis/users.do",method = RequestMethod.GET)
    public @ResponseBody Object insertUsers(String username1,String note1,String username2,String note2){
        User user1 = new User();
        user1.setUserName(username1);
        user1.setNote(note1);
        User user2 = new User();
        user2.setUserName(username2);
        user2.setNote(note2);

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        int inserts = userBatchService.insertUsers(userList);

        Map<String, Object> map = new HashMap<>();
        map.put("success", inserts > 0);
        map.put("userList",userList);
        return map;
    }

    /*redis操作 string */
    @RequestMapping("/test/redis/string.do")
    public @ResponseBody Object testRedisTemplate(){

        //查看redis连接属性
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        //获取jedis连接
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();

        //保存Strkey，注意设置过期时间
        redisTemplate.opsForValue().set("Strkey", "abc",1, TimeUnit.HOURS);//设置为1小时后过期
        //批量保存，每个key都要单独设置过期时间
        Map<String, String> maps = new HashMap<>();
        maps.put("Strkey1", "StrValue1");
        maps.put("Strkey2", "StrValue2");
        maps.put("Strkey3", "StrValue3");
        redisTemplate.opsForValue().multiSet(maps);
        redisTemplate.expire("Strkey1", 60, TimeUnit.SECONDS);
        redisTemplate.expire("Strkey2", 60, TimeUnit.SECONDS);
        redisTemplate.expire("Strkey3", 60, TimeUnit.SECONDS);
        //查询
        String Strkey = (String) redisTemplate.opsForValue().get("Strkey");
        System.out.println(Strkey);
        //批量查询
        List<String> strlist = new ArrayList<>();
        strlist.add("Strkey1");
        strlist.add("Strkey2");
        strlist.add("Strkey3");
        List result = redisTemplate.opsForValue().multiGet(strlist);
        System.out.println(result);
        //删除
        redisTemplate.delete("Strkey");


        redisTemplate.opsForValue().set("key", "5",1, TimeUnit.HOURS);//使用序列化保存的不是整数不能运算
        stringRedisTemplate.opsForValue().set("int", "6",1, TimeUnit.HOURS);
        stringRedisTemplate.opsForValue().increment("int", 1);//使用运算


        return "true";
    }

    /*redis操作 List*/
    @RequestMapping("/test/redis/list.do")
    public @ResponseBody Object testList(){
        //模拟简单list数据，左头======右尾
        List<String> list = new ArrayList<>();
        list.add("listvalue1");
        list.add("listvalue2");
        list.add("listvalue3");
        //保存
        redisTemplate.delete("listkey");//清空
        redisTemplate.opsForList().leftPushAll("listkey",list);//按list顺序依次左插入
        redisTemplate.expire("listkey", 1, TimeUnit.HOURS);
        //绑定对象,多次操作时推荐
        BoundListOperations ops=redisTemplate.boundListOps("listkey");
        //插入元素
        ops.leftPush("listvalue4");//返回列表长度 4 3 2 1
        ops.rightPush("listvalue5");//4 3 2 1 5
        ops.leftPushAll("listvalue6", "listvalue7");
        ops.rightPushAll("listvalue8", "listvalue9");
        //截取下标区间
        List range = ops.range(0, -1);//负数表示截取整个队列7 6 4 3 2 1 5 8 9
        ops.range(0, ops.size() - 2);//截取并不删除原数据
        //删除指定value  l>0,从头到尾第一个匹配项,l<0相反,l=0删除所有匹配项
        ops.remove(1, "listvalue1");
        //左删除、右删除
        ops.leftPop();
        ops.rightPop();
        //批量左删除
        //ops.trim(100,ops.size());
        //完成操作以后更新过期时间
        ops.expire(1, TimeUnit.HOURS);

        range = ops.range(0, -1);

        return range;
    }

    /*redis操作  存储java对象  删除bigkey*/
    @RequestMapping("/test/redis/delbigkey.do")
    public @ResponseBody Object testList2(){

        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();

        //java对象List Hash set Zset
        List<User> userList = new ArrayList<>();
        Map<String, User> userHashMap = new HashMap<>();
        Set<User> userSet = new HashSet<>();
        Set<ZSetOperations.TypedTuple<Object>> userZset = new HashSet<>();

        for(int i=0;i<1000;i++){
            User user = new User();
            user.setId((long) i);
            user.setUserName("user_"+i);
            user.setNote("note_"+i);
            user.setSex("男");

            userList.add(user);
            userHashMap.put(String.valueOf(i), user);
            userSet.add(user);

            ZSetOperations.TypedTuple<Object> Zsetelement = new DefaultTypedTuple<>(user, (double) i);
            userZset.add(Zsetelement);
        }
        //List
        redisTemplate.delete("listkey");//清空
        redisTemplate.opsForList().rightPushAll("listkey",userList);
        redisTemplate.expire("listkey", 1, TimeUnit.HOURS);
        //删除bigkeyList
        BoundListOperations ops=redisTemplate.boundListOps("listkey");
        int count =0;
        int step = 100;
        Long size = ops.size();
        while (count < size) {
            ops.trim(step,size);//渐进式删除，每次删除左侧100个元素
            count += step;
        }
        //最终删除key
        redisTemplate.delete("listkey");

        //Hash
        redisTemplate.delete("hashkey");//清空
        redisTemplate.opsForHash().putAll("hashkey", userHashMap);
        redisTemplate.expire("hashkey", 1, TimeUnit.HOURS);
        //删除bigkeyHash
        ScanParams scanParams = new ScanParams().count(100);
        String cursor = "0";//游标
        do {
            ScanResult<Map.Entry<String, String>> scanResult = jedis.hscan("hashkey", cursor, scanParams);
            List<Map.Entry<String, String>> entries = scanResult.getResult();
            if (entries != null && !entries.isEmpty()) {
                for (Map.Entry<String, String> entry : entries) {
                    jedis.hdel("hashkey", entry.getKey());//遍历100个元素并在haskey中删除
                }
            }
            cursor = scanResult.getStringCursor();
        } while (!"0".equals(cursor));

        //最终删除key
        jedis.del("hashkey");

        //Set
        redisTemplate.delete("setkey");//清空
        for(User user:userSet) {
            redisTemplate.opsForSet().add("setkey", user);//set只能一个个添加
        }
        redisTemplate.expire("setkey", 1, TimeUnit.HOURS);
        //删除bigsetkey
//        scanParams = new ScanParams().count(100);
//        cursor = "0";
//        do {
//            ScanResult<String> scanResult = jedis.sscan("setkey", cursor, scanParams);
//            List<String> entries = scanResult.getResult();
//            if ( entries!= null && !entries.isEmpty()) {
//                for (String entry : entries) {
//                    jedis.srem("setkey", entry);//遍历100个元素并在haskey中删除
//                }
//            }
//            cursor = scanResult.getStringCursor();
//        } while (!"0".equals(cursor));

        BoundSetOperations setops = redisTemplate.boundSetOps("setkey");
        count=0;
        step=100;
        size=setops.size();
        while (count < size) {
            List<User> result = setops.randomMembers(step);
            for (User user : result) {
                setops.remove(user);
            }
            count+=step;    
        }
        //最终删除key
        jedis.del("setkey");


        //Zset
        redisTemplate.delete("zsetkey");//清空
        redisTemplate.opsForZSet().add("zsetkey", userZset);
        redisTemplate.expire("zsetkey", 1, TimeUnit.HOURS);
        //删除bigzsetkey
//        scanParams = new ScanParams().count(100);
//        cursor = "0";
//        do {
//            ScanResult<Tuple> scanResult = jedis.zscan("zsetkey", cursor, scanParams);
//            List<Tuple> entries = scanResult.getResult();
//            if ( entries!= null && !entries.isEmpty()) {
//                for (Tuple entry : entries) {
//                    jedis.zrem("zsetkey", entry.getElement());//遍历100个元素并在haskey中删除
//                }
//            }
//            cursor = scanResult.getStringCursor();
//        } while (!"0".equals(cursor));

        BoundZSetOperations zsetops = redisTemplate.boundZSetOps("zsetkey");
        count=0;
        step=100;
        size=zsetops.size();
        while(count<size){
            zsetops.removeRange(0,step);
            count += step;
        }

        //最终删除key
        jedis.del("zsetkey");

        return "true";
    }

    /*redis操作 hash*/
    @RequestMapping("/test/redis/hash.do")
    public @ResponseBody Object testHash(){
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();

        //hash
        //模拟需要存入redis的hash数据
        Map<String, Object> hash = new HashMap<>();
        hash.put("field1", "value1");
        hash.put("field2", "value2");
        //批量保存
        redisTemplate.delete("hashKey");
        redisTemplate.opsForHash().putAll("hashKey",hash);
        redisTemplate.expire("hashKey", 1, TimeUnit.HOURS);//过期时间
        //对已经保存的hash单独插入key
        redisTemplate.opsForHash().put("hashKey", "field3", "value3");

        //为多个键分别取出它们的值
        List<String> keys = new ArrayList<>();
        keys.add("field2");
        keys.add("field3");
        List hashValue = redisTemplate.opsForHash().multiGet("hashKey", keys);
        System.out.println(hashValue);
        //查询简单数据
        Map<Object, Object> hashFromRedis = redisTemplate.opsForHash().entries("hashKey");

        //绑定key
        BoundHashOperations ops = redisTemplate.boundHashOps("hashKey");
        //获取hash所有key
        ops.keys();
        //增删hash数据
        ops.delete("field1", "field2");
        ops.put("field4", "value4");
        Map entries = ops.entries();

        //scan  游标cursor实现迭代
        String pattern = "*";//匹配规则
        Long count= 100l;
        Cursor cursor = redisTemplate.opsForHash().scan("hashKey", ScanOptions.scanOptions().match(pattern).count(count).build());
        while(cursor.hasNext()){
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) cursor.next();
            System.out.println(entry.getKey()+":"+entry.getValue());
            //实际操作...
        }

        Map<String, Object> map = new HashMap<>();
        map.put("hash", entries);
        map.put("success", entries != null);


        return map;
    }

    /*redis操作 set*/
    @RequestMapping("/test/redis/set.do")
    public @ResponseBody Object testSet(){

        redisTemplate.delete("setKey");
        redisTemplate.delete("setKey1");
        //模拟set数据
        String[] setKey = new String[]{"setvalue1","setvalue2"};
        //插入
        redisTemplate.opsForSet().add("setKey",setKey);
        redisTemplate.expire("setKey",1,TimeUnit.HOURS);
        redisTemplate.opsForSet().add("setKey1",Arrays.asList("setvalue1","setvalue2","setvalue3"));
        redisTemplate.expire("setKey1",1,TimeUnit.HOURS);

        //绑定
        BoundSetOperations ops = redisTemplate.boundSetOps("setKey");
        Set members = ops.members();
        System.out.println(members);
        //插入一个或多个
        ops.add("setvalue3", "setvalue4","setvalue5");
        System.out.println(ops.members());
        //删除一个或多个
        ops.remove("setvalue5","setvalue4");
        System.out.println(ops.members());
        //删除随机一个
        Object pop = ops.pop();
        System.out.println("pop的值："+pop);
        //scan  游标cursor实现迭代
        String pattern = "*";//匹配规则
        Long count= 100l;
        Cursor cursor = ops.scan(ScanOptions.scanOptions().match(pattern).count(count).build());
        while(cursor.hasNext()){
            String s= (String) cursor.next();
            System.out.println(s);
            //实际操作...
        }
        //与其他set求交集(一个或多个)
        members=ops.intersect("setKey1");
        System.out.println(members);
        ops.intersect(Arrays.asList("setKey1", "setKey2"));
        ops.intersectAndStore("setKey1","destSet");//保存交集
        ops.intersectAndStore(Arrays.asList("setKey1", "setKey2"),"destSet");
        //求并集
        ops.union("setKey1");
        //求差集
        ops.diff("setKey1");
        return "true";
    }

    /*redis操作 zset*/
    @RequestMapping("/test/redis/zset.do")
    public @ResponseBody Object testZset(){

        //清空
        redisTemplate.delete("zsetKey");
        //模拟数据
        ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<>("value1", 1.0);
        ZSetOperations.TypedTuple<Object> objectTypedTuple2 = new DefaultTypedTuple<>("value2", 2.0);
        ZSetOperations.TypedTuple<Object> objectTypedTuple3 = new DefaultTypedTuple<>("value3", 3.0);
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
        tuples.add(objectTypedTuple1);
        tuples.add(objectTypedTuple2);
        tuples.add(objectTypedTuple3);
        //批量保存
        redisTemplate.opsForZSet().add("zsetKey",tuples);
        redisTemplate.expire("zsetKey", 1, TimeUnit.HOURS);
        //单个保存
        redisTemplate.opsForZSet().add("zsetKey", "value4", 4.0);
        //绑定
        BoundZSetOperations ops = redisTemplate.boundZSetOps("zsetKey");
        Set range = ops.range(0, -1);
        System.out.println(range);
        //增加元素的score分数值
        Double score = ops.incrementScore("value1", 1.5);
        System.out.println("value1增加后的score："+score);
        score = ops.score("value2");
        System.out.println("value2的score："+score);
        //排序，返回指定元素的排名（rank从小到大,reverseRank从大到小）
        Long rank = ops.rank("value1");
        rank = ops.reverseRank("value2");
        //截取区间  从小到大
        System.out.println("排序[0,1]区间"+ops.range(0, 1));
        Set<ZSetOperations.TypedTuple<Object>> zset = ops.rangeWithScores(0, -1);//包含分数信息的对象
        Iterator iterator = zset.iterator();
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<Object> element = (ZSetOperations.TypedTuple<Object>) iterator.next();
            System.out.println("value=" + element.getValue() + ",score=" + element.getScore());
        }
        //截取区间满足分数在指定区间，按分数递增排序
        range = ops.rangeByScore(2.2, 4.0);
        System.out.println("截取区间满足分数在指定区间:"+range);
        //计算满足分数区间的个数
        Long count = ops.count(0, 5);
        System.out.println("0-5分数区间个数：" + count);
        System.out.println("总个数：" + ops.size() + ":" + ops.zCard());
        //移除一个或多个
        Long remove = ops.remove("value4");
        System.out.println(ops.range(0, -1));
        //按区间移除,头尾都包含[0,1]
        ops.removeRange(0, 1);
        System.out.println(ops.range(0, -1));
        //按分数区间移除，头尾都包含[0,2]
        ops.add(tuples);
        ops.removeRangeByScore(2,3);
        System.out.println(ops.range(0, -1));

        //scan 遍历
        ops.add(tuples);
        String pattern="*";
        long c = 100;
        Cursor cursor = ops.scan(ScanOptions.scanOptions().match(pattern).count(c).build());
        while (cursor.hasNext()) {
            ZSetOperations.TypedTuple<Object> element = (ZSetOperations.TypedTuple<Object>) cursor.next();
            System.out.println("value=" + element.getValue() + ",score=" + element.getScore());
            //实际操作
        }

        return "true";
    }


}