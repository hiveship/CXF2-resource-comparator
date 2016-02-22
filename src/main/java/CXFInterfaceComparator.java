import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;

import org.apache.cxf.jaxrs.ext.ResourceComparator;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Message;

public class CXFInterfaceComparator implements ResourceComparator {

	private static final FAKE_LOGGER LOGGER = new FAKE_LOGGER();

	@Override
	public int compare(ClassResourceInfo cri1, ClassResourceInfo cri2, Message message) {
		String requestVerb = (String) message.get(Message.HTTP_REQUEST_METHOD);
		String requestURI = (String) message.get(Message.REQUEST_URI);
		String requestPath = requestURI.replace((String) message.get(Message.BASE_PATH), "");
		LOGGER.info("The request path for match analysis is -> " + requestPath + " . The HTTP verb of the request is -> " + requestVerb);

		if (analyseInterface(cri1, requestPath, requestVerb)) {
			return -1; // Indicate that 'cri1' interface should be preferred
		} else if (analyseInterface(cri2, requestPath, requestVerb)) {
			return 1; // Indicate that 'cri2' interface should be preferred
		} else {
			LOGGER.info("No match found for request URI -> " + message.get(Message.REQUEST_URI) + " in interfaces -> "
					+ cri1.getServiceClass().getInterfaces()[0] + " and " + cri2.getServiceClass().getInterfaces()[0]);
			return 0; // Nothing match, leave CXF decision
		}
	}

	/**
	 * Analyse each methods provided to check if there is a match with the
	 * message request path and request http verb.
	 * 
	 * @param cri
	 *            the interface to be analysed
	 * @param requestPath
	 *            the path of the request. Do not contains the host and base
	 *            path
	 * @return true if a method match the request, false otherwise
	 */
	private static boolean analyseInterface(ClassResourceInfo cri, String requestPath, String requestVerb) {
		assert cri.getServiceClass() != null;
		assert cri.getServiceClass().getInterfaces() != null;
		assert cri.getServiceClass().getInterfaces()[0] != null;
		assert cri.getServiceClass().getInterfaces()[0].getMethods().length > 0;
		
		Method[] methods = cri.getServiceClass().getInterfaces()[0].getMethods();
		// Java reflexion. Check all the methods of an interface.
		for (Method method : methods) {
			Path pathAnnotation = method.getAnnotation(javax.ws.rs.Path.class);
			if (pathAnnotation != null && pathAnnotation.value() != null) {
				String pathValue = pathAnnotation.value();
				String methodHttpVerb = getMethodHttpVerb(method);

				// Also check the HTTP verb since a single path can be match do multiple request, depending of the HTTP request verb.
				if (requestVerb.equals(methodHttpVerb) && match(pathValue, requestPath)) {
					return true;
				}
			}
		}
		return false;
	}

	private static String getMethodHttpVerb(Method method) {
		if (method.getAnnotation(javax.ws.rs.POST.class) != null) {
			return HttpMethod.POST;
		} else if (method.getAnnotation(javax.ws.rs.GET.class) != null) {
			return HttpMethod.GET;
		} else if (method.getAnnotation(javax.ws.rs.PUT.class) != null) {
			return HttpMethod.PUT;
		} else if (method.getAnnotation(javax.ws.rs.OPTIONS.class) != null) {
			return HttpMethod.OPTIONS;
		} else if (method.getAnnotation(javax.ws.rs.DELETE.class) != null) {
			return HttpMethod.DELETE;
		} else if (method.getAnnotation(javax.ws.rs.HEAD.class) != null) {
			return HttpMethod.HEAD;
		} 
		return null;
	}

	/**
	 * Check whether if the pathValue match with the requestPath parameter.
	 * Every path params are considered to be declared as '{param}'. The tokens to start and close path params declaration are '{' and '}'.
	 * 
	 * @param valueFromAnnotation
	 * @param valueFromRequest
	 * @return true if there is a match, false otherwise
	 */
	private static boolean match(String valueFromAnnotation, String valueFromRequest) {
		String patternFinal = valueFromAnnotation.replaceAll("\\{(.*?)\\}", "([^/]*)").replace("/", "\\/");
		Matcher matcher = Pattern.compile(patternFinal).matcher(valueFromRequest);
		if (matcher.matches()) {
			return true;
		} 
		return false;
	}

	@Override
	public int compare(OperationResourceInfo arg0, OperationResourceInfo arg1, Message arg2) {
		return 0;
	}

}