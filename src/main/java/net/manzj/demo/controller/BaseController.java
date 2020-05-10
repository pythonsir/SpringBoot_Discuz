package net.manzj.demo.controller;

import com.google.code.kaptcha.Constants;
import net.manzj.demo.ucenter.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BaseController {

    @Value("${ucenter.url}")
    private String bbsUrl;

    @Autowired
    private Client client;

    @RequestMapping("/")
    public String index(){
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Map loginOn(@RequestBody LoginForm loginForm,HttpSession session){
        Map<String,Object> map = new HashMap<>();

        session.setAttribute("username",loginForm.getUserName());
        session.setAttribute("password",loginForm.getPassword());
        map.put("ret",200);
        map.put("msg","登录成功");
        return  map;
    }

    @RequestMapping("/main")
    public String main(Model model,HttpSession session){

        String userName = (String) session.getAttribute("username");
        String password = (String)session.getAttribute("password");

        String uid = client.uc_user_login(userName,password);
        String html = client.uc_user_synlogin(Integer.parseInt(uid));

        int start = html.indexOf("src=");
        int end = html.indexOf("reload");

        String html_ = html.substring(start+"src=".length(),end);
        html_ = html_.replace("\"","").trim();

        model.addAttribute("html_",html_);
        model.addAttribute("bbsUrl",bbsUrl);

        return "main";
    }

}
