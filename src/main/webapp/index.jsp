<html>
<body>
<h2>Hello World!</h2>
<%@page import="glavnipaket.entiteti.*"%>
<%@page import="glavnipaket.baza.*"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="glavnipaket.strane.util.*"%>

 <form action="Shop/products" method="post">
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

  <form action="Shop/buyers" method="post">

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

  <form action="Shop/sales" method="post">
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


    <form action="index.jsp" method="POST">
        <label for="buyerubazu">Stiklijate ovde i popunite polja kako biste ubacili kupca (buyer) u bazu. </label>
        <input type="checkbox" name="buyerubazu"> <br>

        <label for="ime">ime: </label>
        <input type="text" name="ime"> <br>

        <label for="prezime">prezime: </label>
        <input type="text" name="prezime">
        <input type="submit" value="Ubacite kupca (buyer) u bazu.">
    </form>

    <form action="index.jsp" method="POST">
        <label for="productubazu"> Stiklirajte ovde i popunite polja kako biste ubacili proizvod (product) u bazu. </label>
        <input type="checkbox" name="productubazu"> <br>


        <label for="naziv">naziv: </label>
        <input type="text" name="naziv"> <br>

        <label for="drzavaproizvodnje">drzava proizvodnje: </label>
        <input type="text" name="drzavaproizvodnje"> <br>

        <label for="proizvodjac">proizvodjac: </label>
        <input type="text" name="proizvodjac"> <br>

        <label for="cena">cena: </label>
        <input type="text" name="cena"> <br>

        <input type="submit" value="Ubacite proizvod (product) u bazu">
    </form>


<%
    String buyerUbazu = request.getParameter("buyerubazu");
    if(buyerUbazu != null){
        if(buyerUbazu.equals("on")){
            Buyer buyer = new Buyer(request.getParameter("ime"),
                                    request.getParameter("prezime"));
            BazaPodataka baza = new BazaPodataka();
            baza.ubaciteUBazu(buyer, "buyers");
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

    <br>
    <br>
    <form action="index.jsp" method="POST">
        <label> Stiklirajte ovde ako zelite da izbrisete product iz baze. </label>
        <input type="checkbox" name="brisiproduct"> <br>
        <label for="idproductzabrisanje"> Upisite id product-a kog zelite da obrisete. </label>
        <input type="text" name="idproductzabrisanje">
        <input type="submit" value="Izbrisite product.">
    </form>

    <%
        if(UrlUtil.uslovPostoji("brisiproduct", request)){
            try{
                new BazaPodataka().izbrisiteEntitet(Integer.parseInt(request.getParameter("idproductzabrisanje")), "products", "product_id");
            catch(Exception e){
                e.printStackTrace();
            }
        }
    %>

     <br>
        <form action="index.jsp" method="POST">
            <label> Stiklirajte ovde ako zelite da izbrisete kupca (buyer) iz baze. </label>
            <input type="checkbox" name="brisikupca"> <br>
            <label for="idbuyerzabrisanje"> Upisite id product-a kog zelite da obrisete. </label>
            <input type="text" name="idbuyerzabrisanje">
            <input type="submit" value="Izbrisite kupca (buyer).">
        </form>

        <%
            if(UrlUtil.uslovPostoji("brisikupca", request)){
                try{
                    new BazaPodataka().izbrisiteEntitet(Integer.parseInt(request.getParameter("idbuyerzabrisanje")), "buyers", "buyer_id");
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        %>
</body>
</html>
