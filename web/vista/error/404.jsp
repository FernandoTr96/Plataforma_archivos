<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="../includes/head.jsp" %>
<div class="container-error">
    <h1>La pagina que buscas no existe !! Error 404</h1>
    <p>ups... la pagina esta sufriemdo amsiedad. </p>
    <br/>
    <br/>
    <br/>
    <img src="<% out.print(BASE_URL); %>/public/res/chems.jpeg" alt="chems">
</div>
<%@include file="../includes/endDoc.jsp" %>

