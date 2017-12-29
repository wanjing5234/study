package com.kk.ssm.controller;

import com.kk.ssm.model.Plupload;
import com.kk.ssm.service.PluploadService;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Controller
@RequestMapping("/plupload")
public class PluploadController {

    private static final Logger logger = LoggerFactory.getLogger(PluploadController.class);

    /**Plupload文件上传处理方法*/
    @RequestMapping(value="/upload")
    public void upload(Plupload plupload, HttpServletRequest request, HttpServletResponse response) {
        logger.info("{}, plupload:{}, request:{}", "upload", plupload.toString(), request);
        String FileDir = "pluploadDir";//文件保存的文件夹

        plupload.setRequest(request);//手动传入Plupload对象HttpServletRequest属性

        //int userId = ((BIConversion.User)request.getSession().getAttribute("user")).getUserId();

        int userId = 1;

        //文件存储绝对路径,会是一个文件夹，项目相应Servlet容器下的"pluploadDir"文件夹，还会以用户唯一id作划分
        File dir = new File(request.getSession().getServletContext().getRealPath("/") + FileDir+"/"+userId);
        if(!dir.exists()){
            dir.mkdirs();//可创建多级目录，而mkdir()只能创建一级目录
        }
        //开始上传文件
        PluploadService.upload(plupload, dir);
    }

}
