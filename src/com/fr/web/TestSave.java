package com.fr.web;

import com.fr.fs.base.entity.User;
import com.fr.fs.control.UserControl;
import com.fr.general.FRLogger;
import com.fr.stable.CodeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class for Servlet: TestSave
 *
 */
public class TestSave extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
//public class TestSave extends BaseServlet{
    public TestSave() {
        super();
    }



    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("fr_username");
        String password = request.getParameter("fr_password");
        String loginFrom = request.getParameter("from");
        String strticket = request.getParameter("ticket");
        FRLogger.getLogger().error("ticket："+strticket);

        String username0;
        String password0="";
        try {
            User user = UserControl.getInstance().getByUserName(username);
            username0 = user.getUsername();
            password0 = user.getPassword();
            FRLogger.getLogger().error("username0000："+username0);
            FRLogger.getLogger().error("password0000："+password0);


        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
            response.getWriter().write("false");
        }

//        if(strticket==null){
//            response.getWriter().write("false");
//            return;
//        }

        FRLogger.getLogger().error("username："+username);
        FRLogger.getLogger().error("password："+password);

        if(username==null)username = "";
        if(password==null)password = "";
        if(password0==null)password0 = "";

        //先进行本地密码对比
        if (CodeUtils.customPasswordEncode(password).equals(password0)) {
            FRLogger.getLogger().error("=================tfsso成功");
            response.getWriter().write(username);
        } else {
            //如果本地密码不匹配，到认证中心对比
            if(check(password).equals("")){
                FRLogger.getLogger().error("=================tfsso失败");
                response.getWriter().write("false");
            }else {
                response.getWriter().write(username);
            }
        }

        /*
        return;


        if(loginFrom!=null&&loginFrom.equals("A8")) {
            String tfusername = check(strticket);
            FRLogger.getLogger().error("tfusername："+tfusername);
            if(tfusername==null||tfusername.equals("")){
                response.getWriter().write("false");
            }else {
                response.getWriter().write(tfusername);
            }

        }else{
            response.getWriter().write("false");
        }
*/

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request,response);
    }

//sean

    public String check(String ticket){
        String authUrl = "http://192.168.1.105/seeyon/thirdpartyController.do?ticket="+ticket;
        FRLogger.getLogger().error("authUrl："+authUrl);
        String result =HttpUtils.sendGet(authUrl);
        if(result==null){
            result="";
        }
        return result;
    }


    /*
    public String moduleToStart()
    {
        return createLegalModuleClassName();
    }

    private String createLegalModuleClassName()
    {
        String str1 = EngineModule.class.getName();
        try
        {
            String str2 = "com.fr.fs.FSModule";
            Class.forName(str2);
            return str2;
        }
        catch (Exception localException)
        {
            FRLogger.getLogger().error("Error in class com.fr.fs.FSModule.");
        }
        return str1;
    }

    public void init(ServletConfig paramServletConfig)
            throws ServletException
    {
        super.init(paramServletConfig);
        try
        {
            ServletContext.fireServletStartListener();
        }
        catch (Exception localException)
        {
            FRContext.getLogger().error(localException.getMessage());
        }
    }

    public void destroy()
    {
        try
        {
            ServletContext.fireServletStopListener();
            ModuleContext.stopModules();
        }
        catch (Throwable localThrowable)
        {
            FRContext.getLogger().error("detroy timer and embdb failed");
        }
        finally
        {
            super.destroy();
        }
    }

     */
}
