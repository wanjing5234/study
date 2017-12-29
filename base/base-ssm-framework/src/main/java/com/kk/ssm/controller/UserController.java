package com.kk.ssm.controller;

import com.kk.ssm.model.User;
import com.kk.ssm.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private IUserService userService;

    @RequestMapping("/showUser")
    public String toIndex(HttpServletRequest request, Model model) {
        //int userId = Integer.parseInt(request.getParameter("id"));
        User user = this.userService.getUserById(1);
        model.addAttribute("user", user);
        return "showUser";
    }

    @RequestMapping("/list")
    public @ResponseBody List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        User user = null;
        for (int i = 1; i <= 2; i++) {
            user = new User();
            user.setUserId(i);
            user.setUsername("kk_" + i);
            user.setPassword("123456");
            userList.add(user);
        }
        return userList;
    }

}