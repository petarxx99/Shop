<html>
<body>
<h2></h2>
<%@page import="glavnipaket.entiteti.*"%>
<%@page import="glavnipaket.baza.*"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="glavnipaket.strane.util.UrlUtil"%>



<%
    boolean podaciUbaceni = false;

    String saleUbazu = request.getParameter("saleubazu");
    if(UrlUtil.uslovPostoji("saleubazu", request)){
        try{
            int productId = Integer.parseInt(request.getParameter("kupljenproizvodid"));
            int buyerId = Integer.parseInt(request.getParameter("idkupcabuyerid"));
            new BazaPodataka().ubaciteUsales(productId, buyerId);
            podaciUbaceni = true;
        } catch(Exception e){
            e.printStackTrace();
            response.sendRedirect("greska.jsp");
        }
    }

    if(podaciUbaceni){
        out.println("Sve je proslo kako treba.");
    } else {
        out.println("Podaci nisu ubaceni u bazu.");
    }
%>

 <a href="index.jsp">Kliknite ovde da biste se vratili na prethodnu stranicu </a>

</body>
</html>
