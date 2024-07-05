package employeemanagementsystem.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class EmployeeApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		// If you have a separate configuration for business services, data sources,
		// etc.,
		// that is not specific to the web layer, you can specify it here.
		// For example, RootConfig.class, if you don't have such a configuration, return
		// null or an empty array.
		return new Class<?>[] { EmployeeRootConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// Here, you specify the configuration class that configures the components
		// specific to the Spring MVC application.
		return new Class<?>[] { EmployeeAppConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		// This maps the DispatcherServlet to the root path ("/").
		// All web application requests will be handled by the Spring MVC
		// DispatcherServlet.
		return new String[] { "/" };
	}
}
