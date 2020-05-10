package net.manzj.demo.ucenter.controller;


import net.manzj.demo.ucenter.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UcenterController {


    @Autowired
    private Client client;

    public static boolean IN_DISCUZ= true;
    public static String UC_CLIENT_VERSION="1.5.0";	//note UCenter 版本标识
    public static String UC_CLIENT_RELEASE="20081031";

    public static boolean API_DELETEUSER=true;		//note 用户删除 API 接口开关
    public static boolean API_RENAMEUSER=true;		//note 用户改名 API 接口开关
    public static boolean API_GETTAG=true;		//note 获取标签 API 接口开关
    public static boolean API_SYNLOGIN=true;		//note 同步登录 API 接口开关
    public static boolean API_SYNLOGOUT=true;		//note 同步登出 API 接口开关
    public static boolean API_UPDATEPW=true;		//note 更改用户密码 开关
    public static boolean API_UPDATEBADWORDS=true;	//note 更新关键字列表 开关
    public static boolean API_UPDATEHOSTS=true;		//note 更新域名解析缓存 开关
    public static boolean API_UPDATEAPPS=true;		//note 更新应用列表 开关
    public static boolean API_UPDATECLIENT=true;		//note 更新客户端缓存 开关
    public static boolean API_UPDATECREDIT=true;		//note 更新用户积分 开关
    public static boolean API_GETCREDITSETTINGS=true;	//note 向 UCenter 提供积分设置 开关
    public static boolean API_GETCREDIT=true;		//note 获取用户的某项积分 开关
    public static boolean API_UPDATECREDITSETTINGS=true;	//note 更新应用积分设置 开关

    public static String API_RETURN_SUCCEED   =    "1";
    public static String API_RETURN_FAILED    =   "-1";
    public static String API_RETURN_FORBIDDEN =   "-2";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/api/uc.php")
    public String index(){

        String code = request.getParameter("code");
        if("".equals(code) || null == code){
            return API_RETURN_FAILED;
        }

        Map<String,String> params = new HashMap<String, String>();
        code = client.uc_authcode(code, "DECODE");
        parse_str(code, params);

        if(params.isEmpty()){
            return "Invalid Request";
        }

//        if(time() - tolong(request.getParameter("time")) > 3600){
//            return "Authracation has expiried";
//        }

        String action = params.get("action");

        if(null == action || "".equals(action)){
            return API_RETURN_FAILED;
        }

        if(action.equals("test")){
            return API_RETURN_SUCCEED;
        }else if(action.equals("deleteuser")){
            return API_RETURN_SUCCEED;
        }else if(action.equals("renameuser")) {
            return API_RETURN_SUCCEED;
        } else if(action.equals("gettag")) {

            if(!API_GETTAG ){
                return API_RETURN_FORBIDDEN;
            }

            //同步代码

            return API_RETURN_SUCCEED;


        } else if(action.equals("synlogin")) {

            if(!API_SYNLOGIN ){
                return (API_RETURN_FORBIDDEN);
            }
            String uid = params.get("uid");
            String password = params.get("password");
            String username = params.get("username");

            //note 同步登录 API 接口
            //obclean();
            response.addHeader("P3P","CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");

            int $cookietime = 31536000;
            Cookie user = new Cookie("loginuser",username);
            user.setMaxAge($cookietime);
            response.addCookie(user);

        } else if(action.equals("synlogout")) {

            if(!API_SYNLOGOUT ){
                return API_RETURN_FORBIDDEN;
            }

            //note 同步登出 API 接口
            //obclean();
            response.addHeader("P3P"," CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
            Cookie user = new Cookie("loginuser","");
            user.setMaxAge(-31536000);
            response.addCookie(user);

        } else if(action.equals("updateclient")) {

            if(!API_UPDATECLIENT ){ return API_RETURN_FORBIDDEN;}


            //同步代码

            return API_RETURN_SUCCEED;

        } else if(action.equals("updatepw")) {

            if(!API_UPDATEPW) { return API_RETURN_FORBIDDEN;}

            //同步代码

            return API_RETURN_SUCCEED;

        } else if(action.equals("updatebadwords")) {

            if(!API_UPDATEBADWORDS) { return API_RETURN_FORBIDDEN;}

            //同步代码

            return API_RETURN_SUCCEED;

        } else if(action.equals("updatehosts")) {

            if(!API_UPDATEHOSTS ) {return API_RETURN_FORBIDDEN;}


            return API_RETURN_SUCCEED;

        } else if(action.equals("updateapps")) {

            if(!API_UPDATEAPPS ) {return API_RETURN_FORBIDDEN;}


            return API_RETURN_SUCCEED;

        } else if(action.equals("updatecredit")) {

            //if(!UPDATECREDIT ) return API_RETURN_FORBIDDEN;

            return API_RETURN_SUCCEED;

        } else if(action.equals("getcreditsettings")) {

            //if(!GETCREDITSETTINGS ) return API_RETURN_FORBIDDEN;

            return "";//积分值

        } else if(action.equals("updatecreditsettings")) {

            if(!API_UPDATECREDITSETTINGS) {return API_RETURN_FORBIDDEN;}


            //同步代码

            return API_RETURN_SUCCEED;

        } else {

            return (API_RETURN_FORBIDDEN);

        }

        return "";

    }



    private void parse_str(String str, Map<String,String> sets){
        if(str==null||str.length()<1){
            return;
        }
        String[] ps = str.split("&");
        for(int i=0;i<ps.length;i++){
            String[] items = ps[i].split("=");
            if(items.length==2){
                sets.put(items[0], items[1]);
            }else if(items.length ==1){
                sets.put(items[0], "");
            }
        }
    }

    protected long time(){
        return System.currentTimeMillis()/1000;
    }

    private static long tolong(Object s){
        if(s!=null){
            String ss = s.toString().trim();
            if(ss.length()==0){
                return 0L;
            }else{
                return Long.parseLong(ss);
            }
        }else{
            return 0L;
        }
    }

}
