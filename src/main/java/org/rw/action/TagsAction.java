package org.rw.action;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.rw.action.model.Author;
import org.rw.action.model.UserAware;
import org.rw.config.Application;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Tags action class
 * @author Austin Delamar
 * @date 4/20/2017
 */
public class TagsAction extends ActionSupport implements UserAware, ServletResponseAware, ServletRequestAware {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> tags = new ArrayList<String>();
	
	public String execute() {
				
		// /tag
		
		// this shows the all the tags available
		try {
			// gather tags
			tags = Application.getDatabaseSource().getArchiveTags();
			
			// sort alphabetically
			Collections.sort(tags, new java.util.Comparator<String>() {
			    @Override
			    public int compare(String s1, String s2) {
			        return s1.compareToIgnoreCase(s2);
			    }
			});
			
			// set attributes
			servletRequest.setCharacterEncoding("UTF-8");
			servletRequest.setAttribute("tags", tags);
			
			return SUCCESS;
		
		} catch (Exception e) {
			addActionError("Error: "+e.getClass().getName()+". Please try again later.");
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

	@Override
	public void setUser(Author user) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
}