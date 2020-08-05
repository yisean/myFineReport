package com.fr;

import com.fr.base.FRContext;
import com.fr.fs.base.entity.User;
import com.fr.fs.base.entity.UserInfo;
import com.fr.fs.control.UserControl;
import com.fr.fs.privilege.auth.FSAuthentication;
import com.fr.fs.privilege.base.FServicePrivilegeLoader;
import com.fr.fs.privilege.entity.DaoFSAuthentication;
import com.fr.privilege.session.PrivilegeInfoSessionMananger;
//import org.jasig.cas.client.validation.Assertion;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter  implements Filter {
        public AuthFilter() {
        }

        public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest re = (HttpServletRequest) req;
            HttpServletResponse resp = (HttpServletResponse) res;
            HttpSession session = re.getSession(true);

            String username = re.getParameter("fr_username");
            String userpass = re.getParameter("fr_password");
            String loginFrom = re.getParameter("loginfrom");

            FRContext.getLogger().error("===AuthFilter   username:"+username);

            if(username==null) username="";
            if(userpass==null) userpass="";

/*            Object object = re.getSession().getAttribute("_const_cas_assertion_");
            if (object != null) {
                Assertion assertion = (Assertion) object;
                username = assertion.getPrincipal().getName();
            } else {
                username = (String) session.getAttribute("edu.yale.its.tp.cas.client.filter.user");
                if (username == null || "".equals(username)) {
                    resp.sendRedirect("http://www.baidu.com");
                }
            }*/

            try {
                User user1 = UserControl.getInstance().getByUserName(username);
                FRContext.getLogger().error("user1.username:"+user1.getUsername());
                FRContext.getLogger().error("session fr_fs_auth_key:"+session.getAttribute("fr_fs_auth_key"));


                if (user1 != null && session.getAttribute("fr_fs_auth_key") == null) {
                    if(username.equals(userpass)) {
                        FSAuthentication authentication = new DaoFSAuthentication(new UserInfo(user1.getId(), user1.getUsername(), user1.getPassword()));
                        long userid = authentication.getUserInfo().getId();
                        PrivilegeInfoSessionMananger.login(new FServicePrivilegeLoader(username, UserControl.getInstance().getAllSRoleNames(userid), UserControl.getInstance().getUserDP(userid)), session, resp);
                        session.setAttribute("fr_fs_auth_key", authentication);
                        UserControl.getInstance().login(userid);
                        System.out.println("fr FrFilter is over with username is -" + username + "- and userpass  is -" + userpass + "-");
                        FRContext.getLogger().error("fr FrFilter is over with username is ###" + username + "### and userpass  is ###" + userpass + "###");
                        filterChain.doFilter(req, res);
                    }else{
                        resp.sendRedirect("http://www.hao123.com");
                    }

                } else if (user1 != null & session.getAttribute("fr_fs_auth_key") != null) {
                    filterChain.doFilter(req, res);
                    FRContext.getLogger().error("no need");

                } else {
                    resp.sendRedirect("http://localhost:8075/WebReport/ReportServer?op=fs_load&cmd=fs_signin");
                }
            } catch (Exception var14) {
                var14.printStackTrace();
            }

        }

        public void init(FilterConfig filterconfig) throws ServletException {
        }

        public void destroy() {
        }
    }
