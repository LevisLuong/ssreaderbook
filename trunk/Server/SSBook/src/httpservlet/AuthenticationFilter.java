package httpservlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {
	private ServletContext context;

	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
		this.context.log("AuthenticationFilter initialized");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		String uri = req.getRequestURI();
		this.context.log("Requested Resource::" + uri);

		if (uri.contains("api") || uri.contains("css")
				|| uri.contains("jquery") || uri.contains("zerocopy")
				|| uri.contains("images") || uri.endsWith("login.jsp")
				|| uri.endsWith("Login") || uri.endsWith("index.jsp")) {
			chain.doFilter(request, response);
			return;
		}

		if (session != null) {
			// pass the request along the filter chain
			this.context.log("username: " + session.getAttribute("username"));
			this.context.log("role: " + session.getAttribute("role"));
			if (session.getAttribute("username") == null) {
				this.context.log("Unauthorized access request");
				res.sendRedirect("login.jsp");
			} else {
				chain.doFilter(request, response);
			}
		} else {
			this.context.log("Unauthorized access request");
			res.sendRedirect("login.jsp");
		}
	}

	public void destroy() {
		// close any resources here
	}

}
