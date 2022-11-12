<html>
<body>
<h2>Hello World!</h2>
<%@page import="glavnipaket.entiteti.*"%>
<%@page import="glavnipaket.baza.*"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="glavnipaket.strane.util.*"%>
<%@page import="glavnipaket.strane.*"%>

 <form action="products" method="post">
        <label> Stiklirajte polja po kojima zelite da se vrsi pretraga </label> <br>

        <input type="checkbox" name="productidcheckbox">
        <label for="productid"> product_id: </label>
        <input type="text" name="productid"> <br>


         <input type="checkbox" name="nazivcheckbox">
         <label for="naziv"> naziv: </label>
         <input type="text" name="naziv"> <br>

          <input type="checkbox" name="proizvodjaccheckbox">
          <label for="naziv"> proizvodjac: </label>
          <input type="text" name="proizvodjac"> <br>

          <input type="checkbox" name="drzavaproizvodnjecheckbox">
          <label for="drzavaproizvodnje"> drzava proizvodnje: </label>
          <input type="text" name="drzavaproizvodnje"> <br>

          <input type="checkbox" name="cenacheckbox">
          <label for="minimalnacena"> minimalna cena: </label>
          <input type="text" name="minimalnacena"> <br>

          <label for="maksimalnacena"> maksimalna cena: </label>
          <input type="text" name="maksimalnacena"> <br>

        <input type="submit" value="idi na stranu products">
  </form>

  <form action="buyers" method="post">

     <label> Stiklirajte polja po kojima zelite da se vrsi pretraga </label> <br>

            <input type="checkbox" name="buyeridcheckbox">
            <label for="buyerid"> buyer_id: </label>
            <input type="text" name="buyerid"> <br>


             <input type="checkbox" name="imecheckbox">
             <label for="ime"> ime: </label>
             <input type="text" name="ime"> <br>

              <input type="checkbox" name="prezimecheckbox">
              <label for="prezime"> prezime: </label>
              <input type="text" name="prezime"> <br>

              <input type="text" name="maksimalnacena"> <br>
    <input type="submit" value="Idite na stranu buyers">

  </form>

  <form action="sales" method="post">
  <label> Stiklirajte polja po kojima zelite da se vrsi pretraga. Prvo idu polja za buyers </label> <br>
             <input type="checkbox" name="buyeridcheckbox">
              <label for="buyerid"> buyer_id: </label>
              <input type="text" name="buyerid"> <br>


               <input type="checkbox" name="imecheckbox">
               <label for="ime"> ime: </label>
               <input type="text" name="ime"> <br>

                <input type="checkbox" name="prezimecheckbox">
                <label for="prezime"> prezime: </label>
                <input type="text" name="prezime"> <br>

            <label> Sada idu polja za product </label> <br>
               <input type="checkbox" name="productidcheckbox">
               <label for="productid"> product_id: </label>
               <input type="text" name="productid"> <br>


               <input type="checkbox" name="nazivcheckbox">
               <label for="naziv"> naziv: </label>
               <input type="text" name="naziv"> <br>

               <input type="checkbox" name="proizvodjaccheckbox">
               <label for="proizvodjac"> proizvodjac: </label>
               <input type="text" name="proizvodjac"> <br>

               <input type="checkbox" name="drzavaproizvodnjecheckbox">
               <label for="drzavaproizvodnje"> drzava proizvodnje: </label>
               <input type="text" name="drzavaproizvodnje"> <br>

               <input type="checkbox" name="cenacheckbox">
               <label for="minimalnacena"> minimalna cena: </label>
               <input type="text" name="minimalnacena"> <br>

               <label for="maksimalnacena"> maksimalna cena: </label>
               <input type="text" name="maksimalnacena"> <br>
    <input type="submit" value="Idite na stranu sales">
  </form>


    <%
        String buyerUbazu = request.getParameter("buyerubazu");
        if(buyerUbazu != null){
            if(buyerUbazu.equals("on")){
                try{
                    Buyer buyer = new Buyer(request.getParameter("ime"),
                                        request.getParameter("prezime"));
                    BazaPodataka baza = new BazaPodataka();
                    baza.ubaciteUBazu(buyer, "buyers");
                } catch(Exception e){
                    e.printStackTrace();
                    response.sendRedirect("greska.jsp");
                }
            }
        }
    %>
    <%
        String productUbazu = request.getParameter("productubazu");
        if(productUbazu != null){
            if(productUbazu.equals("on")){
                try{
                    Product product = new Product(request.getParameter("naziv"),
                                                               request.getParameter("drzavaproizvodnje"),
                                                               request.getParameter("proizvodjac"),
                                                               new BigDecimal(request.getParameter("cena")));
                    BazaPodataka baza = new BazaPodataka();
                    baza.ubaciteUBazu(product, "products");
                } catch(Exception exceptionCena){
                    response.sendRedirect("greska.jsp");
                }
            }
        }
    %>



<%
 String saleUbazu = request.getParameter("saleubazu");
    if(UrlUtil.uslovPostoji("saleubazu", request)){
        try{
            int productId = Integer.parseInt(request.getParameter("kupljenproizvodid"));
            int buyerId = Integer.parseInt(request.getParameter("idkupcabuyerid"));
            new BazaPodataka().ubaciteUsales(productId, buyerId);
        } catch(Exception e){
            e.printStackTrace();
            response.sendRedirect("greska.jsp");
        }
    }
%>


<%
  if(UrlUtil.uslovPostoji(ProductServlet.UPDATE_URL, request)){

        try {
            int id = Integer.parseInt(request.getParameter(ProductServlet.ID_ZA_UPDATE_URL));
            String novNaziv = HtmlUtil.izvuciUpdejt(request, ProductServlet.UPDATE_NAZIV_URL, ProductServlet.CHECKBOX);
            NazivVrednostPolja nvpNaziv = new NazivVrednostPolja("naziv", novNaziv, true);


            String novProizvodjac = HtmlUtil.izvuciUpdejt(request, ProductServlet.UPDATE_PROIZVODJAC_URL, ProductServlet.CHECKBOX);
            NazivVrednostPolja nvpProizvodjac = new NazivVrednostPolja("proizvodjac", novProizvodjac, true);

            String novaDrzava = HtmlUtil.izvuciUpdejt(request, ProductServlet.UPDATE_DRZAVA_URL, ProductServlet.CHECKBOX);
            NazivVrednostPolja nvpDrzava = new NazivVrednostPolja("drzava_proizvodnje", novaDrzava, true);

            String novaCenaString = HtmlUtil.izvuciUpdejt(request, ProductServlet.UPDATE_CENA_URL, ProductServlet.CHECKBOX);
            BigDecimal novaCena = null;
            if(novaCenaString != null){
                novaCena = new BigDecimal(novaCenaString);
            }
            NazivVrednostPolja nvpCena = new NazivVrednostPolja("cena", novaCena, false);

            new BazaPodataka().update(new NazivVrednostPolja[]{nvpNaziv, nvpProizvodjac, nvpDrzava, nvpCena}, "products", "product_id", id);
        } catch(Exception e){
            e.printStackTrace();
        }
  }
%>




<%
    if(UrlUtil.uslovPostoji(BuyerServlet.UPDATE_URL, request)){
         try {
                    int id = Integer.parseInt(request.getParameter(BuyerServlet.ID_ZA_UPDATE_URL));
                    String novoIme = HtmlUtil.izvuciUpdejt(request, BuyerServlet.UPDATE_IME, BuyerServlet.CHECKBOX);
                    NazivVrednostPolja nvpIme = new NazivVrednostPolja("ime", novoIme, true);

                    String novoPrezime = HtmlUtil.izvuciUpdejt(request, BuyerServlet.UPDATE_PREZIME, BuyerServlet.CHECKBOX);
                    NazivVrednostPolja nvpPrezime = new NazivVrednostPolja("prezime", novoPrezime, true);

                    new BazaPodataka().update(new NazivVrednostPolja[]{nvpIme, nvpPrezime}, "buyers", "buyer_id", id);
                } catch(Exception e){
                    e.printStackTrace();
                }
    }


%>

</body>
</html>
