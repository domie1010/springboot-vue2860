







package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 住户
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/zhuhu")
public class ZhuhuController {
    private static final Logger logger = LoggerFactory.getLogger(ZhuhuController.class);

    @Autowired
    private ZhuhuService zhuhuService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    //级联表service



    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");
        else if("住户".equals(role))
            params.put("zhuhuId",request.getSession().getAttribute("userId"));
        if(params.get("orderBy")==null || params.get("orderBy")==""){
            params.put("orderBy","id");
        }
        PageUtils page = zhuhuService.queryPage(params);

        //字典表数据转换
        List<ZhuhuView> list =(List<ZhuhuView>)page.getList();
        for(ZhuhuView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ZhuhuEntity zhuhu = zhuhuService.selectById(id);
        if(zhuhu !=null){
            //entity转view
            ZhuhuView view = new ZhuhuView();
            BeanUtils.copyProperties( zhuhu , view );//把实体数据重构到view中

            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody ZhuhuEntity zhuhu, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,zhuhu:{}",this.getClass().getName(),zhuhu.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");

        Wrapper<ZhuhuEntity> queryWrapper = new EntityWrapper<ZhuhuEntity>()
            .eq("username", zhuhu.getUsername())
            .or()
            .eq("zhuhu_phone", zhuhu.getZhuhuPhone())
            .or()
            .eq("zhuhu_id_number", zhuhu.getZhuhuIdNumber())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ZhuhuEntity zhuhuEntity = zhuhuService.selectOne(queryWrapper);
        if(zhuhuEntity==null){
            zhuhu.setInsertTime(new Date());
            zhuhu.setCreateTime(new Date());
            zhuhu.setPassword("123456");
            zhuhuService.insert(zhuhu);
            return R.ok();
        }else {
            return R.error(511,"账户或者住户手机号或者住户身份证号已经被使用");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody ZhuhuEntity zhuhu, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,zhuhu:{}",this.getClass().getName(),zhuhu.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");
        //根据字段查询是否有相同数据
        Wrapper<ZhuhuEntity> queryWrapper = new EntityWrapper<ZhuhuEntity>()
            .notIn("id",zhuhu.getId())
            .andNew()
            .eq("username", zhuhu.getUsername())
            .or()
            .eq("zhuhu_phone", zhuhu.getZhuhuPhone())
            .or()
            .eq("zhuhu_id_number", zhuhu.getZhuhuIdNumber())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ZhuhuEntity zhuhuEntity = zhuhuService.selectOne(queryWrapper);
        if("".equals(zhuhu.getZhuhuPhoto()) || "null".equals(zhuhu.getZhuhuPhoto())){
                zhuhu.setZhuhuPhoto(null);
        }
        if(zhuhuEntity==null){
            //  String role = String.valueOf(request.getSession().getAttribute("role"));
            //  if("".equals(role)){
            //      zhuhu.set
            //  }
            zhuhuService.updateById(zhuhu);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"账户或者住户手机号或者住户身份证号已经被使用");
        }
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        zhuhuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        try {
            List<ZhuhuEntity> zhuhuList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            ZhuhuEntity zhuhuEntity = new ZhuhuEntity();
//                            zhuhuEntity.setUsername(data.get(0));                    //账户 要改的
//                            //zhuhuEntity.setPassword("123456");//密码
//                            zhuhuEntity.setZhuhuName(data.get(0));                    //住户姓名 要改的
//                            zhuhuEntity.setZhuhuPhone(data.get(0));                    //住户手机号 要改的
//                            zhuhuEntity.setZhuhuIdNumber(data.get(0));                    //住户身份证号 要改的
//                            zhuhuEntity.setZhuhuAddress(data.get(0));                    //现住地址 要改的
//                            zhuhuEntity.setZhuhuPhoto("");//照片
//                            zhuhuEntity.setZhuhuAge(Integer.valueOf(data.get(0)));   //年龄 要改的
//                            zhuhuEntity.setSexTypes(Integer.valueOf(data.get(0)));   //性别 要改的
//                            zhuhuEntity.setZhuhuYimiaoTypes(Integer.valueOf(data.get(0)));   //是否注射疫苗 要改的
//                            zhuhuEntity.setZhuhuLaiyuandi(data.get(0));                    //来源地 要改的
//                            zhuhuEntity.setZhuhuHesuanTypes(Integer.valueOf(data.get(0)));   //是否有7日内核酸证明 要改的
//                            zhuhuEntity.setZhuhuXingcheng(data.get(0));                    //行程轨迹 要改的
//                            zhuhuEntity.setZhuhuContent("");//照片
//                            zhuhuEntity.setZhuhuTypes(Integer.valueOf(data.get(0)));   //类型 要改的
//                            zhuhuEntity.setZhuhuTime(new Date(data.get(0)));          //入住时间 要改的
//                            zhuhuEntity.setInsertTime(date);//时间
//                            zhuhuEntity.setCreateTime(date);//时间
                            zhuhuList.add(zhuhuEntity);


                            //把要查询是否重复的字段放入map中
                                //账户
                                if(seachFields.containsKey("username")){
                                    List<String> username = seachFields.get("username");
                                    username.add(data.get(0));//要改的
                                }else{
                                    List<String> username = new ArrayList<>();
                                    username.add(data.get(0));//要改的
                                    seachFields.put("username",username);
                                }
                                //住户手机号
                                if(seachFields.containsKey("zhuhuPhone")){
                                    List<String> zhuhuPhone = seachFields.get("zhuhuPhone");
                                    zhuhuPhone.add(data.get(0));//要改的
                                }else{
                                    List<String> zhuhuPhone = new ArrayList<>();
                                    zhuhuPhone.add(data.get(0));//要改的
                                    seachFields.put("zhuhuPhone",zhuhuPhone);
                                }
                                //住户身份证号
                                if(seachFields.containsKey("zhuhuIdNumber")){
                                    List<String> zhuhuIdNumber = seachFields.get("zhuhuIdNumber");
                                    zhuhuIdNumber.add(data.get(0));//要改的
                                }else{
                                    List<String> zhuhuIdNumber = new ArrayList<>();
                                    zhuhuIdNumber.add(data.get(0));//要改的
                                    seachFields.put("zhuhuIdNumber",zhuhuIdNumber);
                                }
                        }

                        //查询是否重复
                         //账户
                        List<ZhuhuEntity> zhuhuEntities_username = zhuhuService.selectList(new EntityWrapper<ZhuhuEntity>().in("username", seachFields.get("username")));
                        if(zhuhuEntities_username.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(ZhuhuEntity s:zhuhuEntities_username){
                                repeatFields.add(s.getUsername());
                            }
                            return R.error(511,"数据库的该表中的 [账户] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                         //住户手机号
                        List<ZhuhuEntity> zhuhuEntities_zhuhuPhone = zhuhuService.selectList(new EntityWrapper<ZhuhuEntity>().in("zhuhu_phone", seachFields.get("zhuhuPhone")));
                        if(zhuhuEntities_zhuhuPhone.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(ZhuhuEntity s:zhuhuEntities_zhuhuPhone){
                                repeatFields.add(s.getZhuhuPhone());
                            }
                            return R.error(511,"数据库的该表中的 [住户手机号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                         //住户身份证号
                        List<ZhuhuEntity> zhuhuEntities_zhuhuIdNumber = zhuhuService.selectList(new EntityWrapper<ZhuhuEntity>().in("zhuhu_id_number", seachFields.get("zhuhuIdNumber")));
                        if(zhuhuEntities_zhuhuIdNumber.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(ZhuhuEntity s:zhuhuEntities_zhuhuIdNumber){
                                repeatFields.add(s.getZhuhuIdNumber());
                            }
                            return R.error(511,"数据库的该表中的 [住户身份证号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        zhuhuService.insertBatch(zhuhuList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }

    /**
    * 登录
    */
    @IgnoreAuth
    @RequestMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        ZhuhuEntity zhuhu = zhuhuService.selectOne(new EntityWrapper<ZhuhuEntity>().eq("username", username));
        if(zhuhu==null || !zhuhu.getPassword().equals(password))
            return R.error("账号或密码不正确");
        //  // 获取监听器中的字典表
        // ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        // Map<String, Map<Integer, String>> dictionaryMap= (Map<String, Map<Integer, String>>) servletContext.getAttribute("dictionaryMap");
        // Map<Integer, String> role_types = dictionaryMap.get("role_types");
        // role_types.get(.getRoleTypes());
        String token = tokenService.generateToken(zhuhu.getId(),username, "zhuhu", "住户");
        R r = R.ok();
        r.put("token", token);
        r.put("role","住户");
        r.put("username",zhuhu.getZhuhuName());
        r.put("tableName","zhuhu");
        r.put("userId",zhuhu.getId());
        return r;
    }

    /**
    * 注册
    */
    @IgnoreAuth
    @PostMapping(value = "/register")
    public R register(@RequestBody ZhuhuEntity zhuhu){
//    	ValidatorUtils.validateEntity(user);
        Wrapper<ZhuhuEntity> queryWrapper = new EntityWrapper<ZhuhuEntity>()
            .eq("username", zhuhu.getUsername())
            .or()
            .eq("zhuhu_phone", zhuhu.getZhuhuPhone())
            .or()
            .eq("zhuhu_id_number", zhuhu.getZhuhuIdNumber())
            ;
        ZhuhuEntity zhuhuEntity = zhuhuService.selectOne(queryWrapper);
        if(zhuhuEntity != null)
            return R.error("账户或者住户手机号或者住户身份证号已经被使用");
        zhuhu.setInsertTime(new Date());
        zhuhu.setCreateTime(new Date());
        zhuhuService.insert(zhuhu);
        return R.ok();
    }

    /**
     * 重置密码
     */
    @GetMapping(value = "/resetPassword")
    public R resetPassword(Integer  id){
        ZhuhuEntity zhuhu = new ZhuhuEntity();
        zhuhu.setPassword("123456");
        zhuhu.setId(id);
        zhuhu.setInsertTime(new Date());
        zhuhuService.updateById(zhuhu);
        return R.ok();
    }


    /**
     * 忘记密码
     */
    @IgnoreAuth
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request) {
        ZhuhuEntity zhuhu = zhuhuService.selectOne(new EntityWrapper<ZhuhuEntity>().eq("username", username));
        if(zhuhu!=null){
            zhuhu.setPassword("123456");
            boolean b = zhuhuService.updateById(zhuhu);
            if(!b){
               return R.error();
            }
        }else{
           return R.error("账号不存在");
        }
        return R.ok();
    }


    /**
    * 获取用户的session用户信息
    */
    @RequestMapping("/session")
    public R getCurrZhuhu(HttpServletRequest request){
        Integer id = (Integer)request.getSession().getAttribute("userId");
        ZhuhuEntity zhuhu = zhuhuService.selectById(id);
        if(zhuhu !=null){
            //entity转view
            ZhuhuView view = new ZhuhuView();
            BeanUtils.copyProperties( zhuhu , view );//把实体数据重构到view中

            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }
    }


    /**
    * 退出
    */
    @GetMapping(value = "logout")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok("退出成功");
    }





}
