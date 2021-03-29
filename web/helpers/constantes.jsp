<%
    
final String url = request.getRequestURL().toString();
final String BASE_URL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
final HttpSession http = request.getSession();

%>
