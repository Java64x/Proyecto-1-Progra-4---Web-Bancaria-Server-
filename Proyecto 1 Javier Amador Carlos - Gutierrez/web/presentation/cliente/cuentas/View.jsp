
<%@page import="banca.logic.Cuenta"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<% List<Cuenta> cuentas = new ArrayList(); 
cuentas.add(new Cuenta("1-111",100.00));
cuentas.add(new Cuenta("2-222",200.00));
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
 <%@ include file="/presentation/Head.jsp" %>
 <title>Principal</title> 
</head>
<body >
    <%@ include file="/presentation/Header.jsp" %>

    <div><H1 >Cuentas</H1></div>
    
    
    <div style = "width:50%;margin:auto">
        
        <h1>Listado de Cuentas del Cliente</h1>   
    
             <table> 
        
                 <thead>
                     <tr> <td>Numero </td> <td>Saldo </td> </tr>
                </thead>
                <tbody>
                     <% for(Cuenta c:cuentas){ %>
                        <tr> <td> <%=c.getNumero()%> </td>  <td><%=c.getSaldo()%> </td> </tr>
                    <%}%>
                 </tbody>
    
                </table>     
                   

              
                 
    </div>
                 
                 
                 
     <%@ include file="/presentation/Footer.jsp" %>
</body>
</html>