package ua.nure.thao.SummaryTask4.web.command.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.thao.SummaryTask4.Path;
import ua.nure.thao.SummaryTask4.db.DBManager;
import ua.nure.thao.SummaryTask4.db.bean.UserTestBean;
import ua.nure.thao.SummaryTask4.db.entity.Category;
import ua.nure.thao.SummaryTask4.db.entity.User;
import ua.nure.thao.SummaryTask4.exception.AppException;
import ua.nure.thao.SummaryTask4.web.command.Command;

public class StatisticResultsCommand extends Command {

	private static final long serialVersionUID = -6417843293853558658L;
	
	private static final Logger LOG = Logger.getLogger(StatisticResultsCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		
		LOG.debug("Command starts");
		
		DBManager manager = DBManager.getInstance();
		
		List<Category> categories = manager.findCategories();
		LOG.trace("Found in DB: categories --> " + categories);
		request.setAttribute("categories", categories);
		LOG.trace("Set the request attribute: categories --> " + categories);
		
		String categoryId = request.getParameter("categoryId");
		LOG.trace("Request parameter: categoryId --> " + categoryId);
		
		List<User> users = manager.findUserByRole(1);
		
		for (User u : users) {
			u.setUtbeans(manager.findUserTestBeans(u.getId()));
			for(UserTestBean utb : u.getUtbeans()) {
				utb.setTest(manager.findTestById(utb.getTestId()));
			}
		}
		
		request.setAttribute("users", users);
		
		LOG.debug("Command finished");
		return Path.PAGE_ALL_USER_TEST_BEAN;
	}

}
