package org.oddox.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.oddox.config.Application;
import org.oddox.objects.Post;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Home action class
 * 
 * @author Austin Delamar
 * @date 11/30/2015
 */
public class HomeAction extends ActionSupport implements ServletResponseAware, ServletRequestAware {

    private static final long serialVersionUID = 1L;

    private List<Post> posts = null;

    /**
     * Returns blog information and lists recent posts.
     * 
     * @return Action String
     */
    public String execute() {

        // /home
        try {
            // gather posts
            posts = Application.getDatabaseService().getPosts(1,
                    Application.getInt("default.limit"), false);

            // set attributes
            servletRequest.setAttribute("posts", posts);

            return SUCCESS;

        } catch (Exception e) {
            addActionError("Error: " + e.getClass().getName() + ". Please try again later.");
            e.printStackTrace();
            return ERROR;
        }
    }

    protected HttpServletResponse servletResponse;

    @Override
    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    protected HttpServletRequest servletRequest;

    @Override
    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}