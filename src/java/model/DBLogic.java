/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author 604772006
 */
public class DBLogic implements DBConn{
    private static Connection kapcsolat;
    public static String kapcsolatTeszt()
    {
        String result="";
        try {
            kapcsolatNyit();
            Statement s=kapcsolat.createStatement();
            String sql = "select * from COUNTRIES where REGION_ID=2";
            ResultSet rs=s.executeQuery(sql);
            while (rs.next()){
                result+=rs.getString("COUNTRY_NAME");
           }
            kapcsolatZár();
            
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    
    public static boolean tablaLetezik()
    {
        try {
            kapcsolatNyit();
            Statement s=kapcsolat.createStatement();
            String sql = "select count(*) from all_tables where table_name='RECEPT'";
            int tablak_szama=0;
            ResultSet rs=s.executeQuery(sql);
            while (rs.next())  tablak_szama=rs.getInt(1);
            kapcsolatZár();
            //Tesztelésre - Recept táblák száma
            //System.out.println("recept táblák száma:"+tablak_szama);
            if(tablak_szama>0) return true;
        }
        catch(SQLException e) {
            
            System.out.println(e.getMessage());
        }
        
        return false;
        
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Táblák létrehozása ">
    public static void tablatLetrehoz() {
        try {
            kapcsolatNyit();
            Statement s = kapcsolat.createStatement();

            //System.out.println("User tábla létrehozása");
            String sql_user_tabla
                    = "CREATE TABLE User(nev varchar(255) PRIMARY KEY,password varchar(255))";
            s.executeUpdate(sql_user_tabla);
            //System.out.println("Recept tábla létrehozása");
            String sql_recept_tabla
                    = "CREATE TABLE Recept(id int NOT NULL PRIMARY KEY,user_name varchar(255),nev varchar(255),elkeszites varchar(2000))";
            s.executeUpdate(sql_recept_tabla);
            //System.out.println("Recept sequence létrehozása");
            String sql_recept_sequence
                    = "CREATE SEQUENCE seq_recept MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 100";
            s.executeUpdate(sql_recept_sequence);
            //System.out.println("Összetevő tábla létrehozása");
            String sql_osszetevo_table
                    = "CREATE TABLE Osszetevo"
                    + "("
                    + "id int NOT NULL PRIMARY KEY,"
                    + "nev varchar(255)"
                    + ")";
            s.executeUpdate(sql_osszetevo_table);
            //System.out.println("Összetevő sequence létrehozása");
            String sql_osszetevo_sequence
                    = "CREATE SEQUENCE seq_osszetevo MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 100";
            s.executeUpdate(sql_osszetevo_sequence);
            //System.out.println("Mennyiség tábla létrehozása");
            String sql_mennyiseg_tabla
                    = "CREATE TABLE Mennyiseg"
                    + "("
                    + "id int NOT NULL PRIMARY KEY,"
                    + "nev varchar(255)"
                    + ")";
            s.executeUpdate(sql_mennyiseg_tabla);
            //System.out.println("Mennyiség sequence létrehozása");
            String sql_mennyiseg_sequence
                    = "CREATE SEQUENCE seq_mennyiseg MINVALUE 1 START WITH 1 INCREMENT BY 1 CACHE 100";
            s.executeUpdate(sql_mennyiseg_sequence);
            //System.out.println("Központi tábla létrehozása");
            String sql_kozponti_tabla
                    = "CREATE TABLE Kozponti"
                    + "("
                    + "recept_id int NOT NULL,"
                    + "osszetevo_id int NOT NULL,"
                    + "mennyiseg int,"
                    + "mennyiseg_id int NOT NULL,"
                    + "CONSTRAINT fk_recept FOREIGN KEY(recept_id) REFERENCES Recept(id),"
                    + "CONSTRAINT fk_osszetevo FOREIGN KEY(osszetevo_id) REFERENCES Osszetevo(id),"
                    + "CONSTRAINT fk_mennyiseg FOREIGN KEY(mennyiseg_id) REFERENCES Mennyiseg(id)"
                    + ")";
            s.executeUpdate(sql_kozponti_tabla);
            
            kapcsolatZár();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Tábla feltöltése tesztadatokkal ">
    public static void tablaTesztAdatok() {
        try {
            kapcsolatNyit();
            Statement s = kapcsolat.createStatement();
            //System.out.println("Recept tesztadatok beszúrása Recept táblába");
            String sql_recept_hozzad1 = "INSERT INTO Recept(id,user_name,nev,elkeszites) VALUES "
                    + "(seq_recept.nextval,'default','Rántott csirkecomb kukoricás rizzsel','A csirkecombokat enyhén sós vízben megfőzzük, aztán bepanírozzuk először lisztbe, aztán tojásba, és végül a zsemlemorzsába forgatjuk bele.Bő olajban kisütjük a rántott csirkecombokat.Közben megfőzzük a rizst, kukoricát pirítunk hozzá, és összekeverjük a kukoricát a rizzsel.')";
            s.executeUpdate(sql_recept_hozzad1);
            String sql_recept_hozzad2 = "INSERT INTO Recept(id,user_name,nev,elkeszites) VALUES "
                    + "(seq_recept.nextval,'default','Holstein szelet hasábburgonyával','A húst klopfoljuk ki vékonyra, majd ízlés szerint sózzuk, borsozzuk, és hagyjuk kicsit állni, hogy az ízek jól átjárják."
                    + "Egy teflon serpenyőben hevítsük fel az olajat, majd az előzőleg a lisztbe megforgatott húst helyezzük bele, mérsékeljük a lángot, és sütés közben többször átfordítva, szép lassan süssük mindkét oldalát pirosra. Tálalásig tartsuk melegen."
                    + "A tojást szintén teflon serpenyőben, egy kevés olajon süssük meg, a sütési idő attól függ, hogy mennyire keményre szeretnénk sütni a sárgáját."
                    + "A krumplit pucoljuk meg, vágjuk hasábokra. Enyhén sós vízben főzzük kb. 5 percig, majd szűrjük le, hűtsük ki, ne maradjon vizes a krumpli! Tegyük be a fagyasztóba, hogy teljesen kifagyjon. (Érdemes egyszerre nagyobb mennyiséget előkészíteni, majd a fagyasztóban tárolni felhasználásig.)"
                    + "Öntsük bele az olajat egy serpenyőbe, hevítsük fel. A forró olajba óvatosan tegyünk bele kb. 2 nagy marék krumplit, keverjük kicsit össze, majd pár perc alatt süssük szép pirosra. (Sütés közben ne nagyon kevergessük, mert különben széttörik a krumpli.) Szűrőkanállal szedjük ki a megsült krumplit papírtörlőre, és tálalásig tartsuk melegen."
                    + "Tálalás előtt ízlés szerint sózzuk meg a krumplit, majd halmozzuk tányérra, helyezzünk a tetejére egy szelet sült húst és egy tükörtojást. Ízlés szerint szórjuk meg egy csipet pirospaprikával, majd úgy kínáljuk.')";
            s.executeUpdate(sql_recept_hozzad2);
            String sql_recept_hozzad3 = "INSERT INTO Recept(id,user_name,nev,elkeszites) VALUES "
                    + "(seq_recept.nextval,'default','Rakott krumpli','A krumplikat 10-15 percre beáztatjuk hideg vízbe, majd alaposan megsikáljuk és leöblítjük. A tojásokat megmossuk."
                    + "A krumplit annyi hideg vízzel öntjük fel, hogy bőven ellepje. Hozzáadunk 1 evőkanál sót (ettől ízletesebb lesz), és a forrástól számított 20 percig, fedő alatt, szelíd tűzön puhára főzzük. A tojásokat erősen sós, hideg vízben feltesszük főni, és a forrástól számított 8 percig főzzük, majd hideg vízbe merítve azonnal lehűtjük. A krumpli alól leöntjük a vizet, még melegen meghámozzuk, és hűlni hagyjuk. A kihűlt tojásokat ugyancsak meghámozzuk, elnegyedeljük vagy felkarikázzuk."
                    + "A kolbászt meleg vízzel leöblítjük (így könnyen lehúzhatjuk a héját), azután vékonyan felkarikázzuk. Elkeverjük a tejfölben a tojássárgákat. A sütőt 200 °C-ra (gázsütő 3. fokozat) előmelegítjük."
                    + "A margarinnal kikenünk egy lapos tűzálló tálat, aljára karikázzuk a krumpli felét, kissé megsózzuk, lazán összekeverjük, és elsimítjuk. Arányosan elosztva a tetejére rendezzük a tojást és a kolbászkarikákat, majd megkenjük a tejföl felével. A maradék krumplit felkarikázzuk, és kissé egymásra csúsztatva a tetejére rendezzük. Egyenletesen bevonjuk a maradék tejföllel, végül a tetejére göndörítjük a szalonnaszeleteket."
                    + "A sütőben addig sütjük, amíg a tejföl és a szalonna aranybarnára pirul (35-40 perc). Ezután tálalhatjuk.')";
            s.executeUpdate(sql_recept_hozzad3);
            String sql_recept_hozzad4 = "INSERT INTO Recept(id,user_name,nev,elkeszites) VALUES "
                    + "(seq_recept.nextval,'default','Mákos tészta','A darált mákot a porcukorral összekeverjük, majd a tésztát kifőzzük, leszűrjük. Ezután a vajat felmelegítjük egy lábosban. Majd beletesszük a tésztát, elkeverjük. Végül ozzháadjuk a porcukros mákot, majd összekeverjük.')";
            s.executeUpdate(sql_recept_hozzad4);
            String sql_recept_hozzad5 = "INSERT INTO Recept(id,user_name,nev,elkeszites) VALUES "
                    + "(seq_recept.nextval,'default','Babgulyás','A babot előző este beáztatjuk jó sok vízbe. Másnap a répákat felkarikázzuk, a paradicsomot, és a húst is felaprítjuk. A babot egy edénybe tesszük, teszünk bele babérlevelet, felengedjük vízzel, ami ellepi és elkezdjük főzni had, puhuljon. A hagymát apróra vágjuk, kevés olajon üvegesre dinszteljük. Beletesszük az összenyomott fokhagymát, hozzáadjuk a megmosott felkockázott húst és fehéredésig főzzük. Hozzáadjuk, a pirospaprikát elkeverjük. Ezután mehet bele az apró kocára vágott paradicsom, a karikára vágott répa és zöldség. Felöntjük vízzel és hozzáadjuk a kisebb darabokra vágott füstölt húst. Fél óra főzés után a babot is beletesszük, és a bab főzőlevét is adjuk hozzá, nagyon finom lesz tőle. Együtt főzzük, amíg a hús és a bab is puha lesz. Addig elkészítjük a csipetkét. Egy tálba tesszük a lisztet közepébe a tojást, sót, és jól összegyúrjuk, ha kell, még adunk hozzá lisztet az a lényeg a tészta ne ragadjon a kezünkhöz. Ha puha a hús és a bab is, akkor a tésztát tegyük a levesbe, és jól forraljuk össze. A kész tészták feljönnek a leves tetejére.')";
            s.executeUpdate(sql_recept_hozzad5);

            //System.out.println("Recept tesztadatok beszúrása Összetevő táblába");
            String sql_osszetevo_hozzaad01
                    = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'csirkecomb')";
            s.executeUpdate(sql_osszetevo_hozzaad01);
            String sql_osszetevo_hozzaad02 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'rizs')";
            s.executeUpdate(sql_osszetevo_hozzaad02);
            String sql_osszetevo_hozzaad03 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'étolaj')";
            s.executeUpdate(sql_osszetevo_hozzaad03);
            String sql_osszetevo_hozzaad04 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'só')";
            s.executeUpdate(sql_osszetevo_hozzaad04);
            String sql_osszetevo_hozzaad05 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'víz')";
            s.executeUpdate(sql_osszetevo_hozzaad05);
            String sql_osszetevo_hozzaad06 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'kukorica')";
            s.executeUpdate(sql_osszetevo_hozzaad06);
            String sql_osszetevo_hozzaad07 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'liszt')";
            s.executeUpdate(sql_osszetevo_hozzaad07);
            String sql_osszetevo_hozzaad08 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'tojás')";
            s.executeUpdate(sql_osszetevo_hozzaad08);
            String sql_osszetevo_hozzaad09 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'zsemlemorzsa')";
            s.executeUpdate(sql_osszetevo_hozzaad09);
            String sql_osszetevo_hozzaad10 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'sertéskaraj')";
            s.executeUpdate(sql_osszetevo_hozzaad10);
            String sql_osszetevo_hozzaad11 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'bors')";
            s.executeUpdate(sql_osszetevo_hozzaad11);
            String sql_osszetevo_hozzaad12 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'burgonya')";
            s.executeUpdate(sql_osszetevo_hozzaad12);
            String sql_osszetevo_hozzaad13 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'pirospaprika')";
            s.executeUpdate(sql_osszetevo_hozzaad13);
            String sql_osszetevo_hozzaad14 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'kolbász')";
            s.executeUpdate(sql_osszetevo_hozzaad14);
            String sql_osszetevo_hozzaad15 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'tejföl')";
            s.executeUpdate(sql_osszetevo_hozzaad15);
            String sql_osszetevo_hozzaad16 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'margarin')";
            s.executeUpdate(sql_osszetevo_hozzaad16);
            String sql_osszetevo_hozzaad17 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'szalonna')";
            s.executeUpdate(sql_osszetevo_hozzaad17);
            String sql_osszetevo_hozzaad18 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'mák')";
            s.executeUpdate(sql_osszetevo_hozzaad18);
            String sql_osszetevo_hozzaad19 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'porcukor')";
            s.executeUpdate(sql_osszetevo_hozzaad19);
            String sql_osszetevo_hozzaad20 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'tészta')";
            s.executeUpdate(sql_osszetevo_hozzaad20);
            String sql_osszetevo_hozzaad21 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'vaj')";
            s.executeUpdate(sql_osszetevo_hozzaad21);
            String sql_osszetevo_hozzaad22 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'szárazbab')";
            s.executeUpdate(sql_osszetevo_hozzaad22);
            String sql_osszetevo_hozzaad23 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'sárgarépa')";
            s.executeUpdate(sql_osszetevo_hozzaad23);
            String sql_osszetevo_hozzaad24 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'babérlevél')";
            s.executeUpdate(sql_osszetevo_hozzaad24);
            String sql_osszetevo_hozzaad25 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'vöröshagyma')";
            s.executeUpdate(sql_osszetevo_hozzaad25);
            String sql_osszetevo_hozzaad26 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'fokhagyma')";
            s.executeUpdate(sql_osszetevo_hozzaad26);
            String sql_osszetevo_hozzaad27 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'paradicsom')";
            s.executeUpdate(sql_osszetevo_hozzaad27);
            String sql_osszetevo_hozzaad28 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'csipetke')";
            s.executeUpdate(sql_osszetevo_hozzaad28);
            String sql_osszetevo_hozzaad29 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'sertéstarja')";
            s.executeUpdate(sql_osszetevo_hozzaad29);
            String sql_osszetevo_hozzaad30 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'sertéscsülök')";
            s.executeUpdate(sql_osszetevo_hozzaad30);
            String sql_osszetevo_hozzaad31 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'petrezselyem')";
            s.executeUpdate(sql_osszetevo_hozzaad31);
            String sql_osszetevo_hozzaad32 = "INSERT INTO Osszetevo(id,nev) VALUES "
                    + "(seq_osszetevo.nextval,'fűszerpaprika')";
            s.executeUpdate(sql_osszetevo_hozzaad32);

            //System.out.println("Tesztadatok beszúrása Mennyiség táblába");
            String sql_mennyiseg_hozzaad1
                    = "INSERT INTO Mennyiseg(id,nev)"
                    + "VALUES"
                    + "(seq_mennyiseg.nextval,'db')";
            s.executeUpdate(sql_mennyiseg_hozzaad1);
            String sql_mennyiseg_hozzaad2 = "INSERT INTO Mennyiseg(id,nev) VALUES"
                    + "(seq_mennyiseg.nextval,'gramm')";
            s.executeUpdate(sql_mennyiseg_hozzaad2);
            String sql_mennyiseg_hozzaad3 = "INSERT INTO Mennyiseg(id,nev) VALUES"
                    + "(seq_mennyiseg.nextval,'ml')";
            s.executeUpdate(sql_mennyiseg_hozzaad3);

            //System.out.println("Recept tesztadatok beszúrása Központi táblába");
            String sql_kozponti_hozzaad01
                    = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id)"
                    + "VALUES"
                    + "(1,1,8,1)";
            s.executeUpdate(sql_kozponti_hozzaad01);
            String sql_kozponti_hozzaad02 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(1,7,150,2)";
            s.executeUpdate(sql_kozponti_hozzaad02);
            String sql_kozponti_hozzaad03 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(1,8,4,1)";
            s.executeUpdate(sql_kozponti_hozzaad03);
            String sql_kozponti_hozzaad04 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(1,9,300,2)";
            s.executeUpdate(sql_kozponti_hozzaad04);
            String sql_kozponti_hozzaad05 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(1,4,7,2)";
            s.executeUpdate(sql_kozponti_hozzaad05);
            String sql_kozponti_hozzaad06 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(1,3,400,3)";
            s.executeUpdate(sql_kozponti_hozzaad06);
            String sql_kozponti_hozzaad07 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(2,10,400,2)";
            s.executeUpdate(sql_kozponti_hozzaad07);
            String sql_kozponti_hozzaad08 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(2,8,4,1)";
            s.executeUpdate(sql_kozponti_hozzaad08);
            String sql_kozponti_hozzaad09 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(2,7,20,2)";
            s.executeUpdate(sql_kozponti_hozzaad09);
            String sql_kozponti_hozzaad10 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(2,4,2,2)";
            s.executeUpdate(sql_kozponti_hozzaad10);
            String sql_kozponti_hozzaad11 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(2,11,2,2)";
            s.executeUpdate(sql_kozponti_hozzaad11);
            String sql_kozponti_hozzaad12 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(2,3,100,3)";
            s.executeUpdate(sql_kozponti_hozzaad12);
            String sql_kozponti_hozzaad13 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(2,12,1000,2)";
            s.executeUpdate(sql_kozponti_hozzaad13);
            String sql_kozponti_hozzaad14 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(2,3,500,3)";
            s.executeUpdate(sql_kozponti_hozzaad14);
            String sql_kozponti_hozzaad15 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES "
                    + "(3,12,1000,2)";
            s.executeUpdate(sql_kozponti_hozzaad15);
            String sql_kozponti_hozzaad16 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(3,8,8,1)";
            s.executeUpdate(sql_kozponti_hozzaad16);
            String sql_kozponti_hozzaad17 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(3,14,100,2)";
            s.executeUpdate(sql_kozponti_hozzaad17);
            String sql_kozponti_hozzaad18 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(3,16,20,2)";
            s.executeUpdate(sql_kozponti_hozzaad18);
            String sql_kozponti_hozzaad19 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(3,17,100,2)";
            s.executeUpdate(sql_kozponti_hozzaad19);
            String sql_kozponti_hozzaad20 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(4,20,500,2)";
            s.executeUpdate(sql_kozponti_hozzaad20);
            String sql_kozponti_hozzaad21 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(4,18,200,2)";
            s.executeUpdate(sql_kozponti_hozzaad21);
            String sql_kozponti_hozzaad22 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(4,19,150,2)";
            s.executeUpdate(sql_kozponti_hozzaad22);
            String sql_kozponti_hozzaad23 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(4,21,50,2)";
            s.executeUpdate(sql_kozponti_hozzaad23);
            String sql_kozponti_hozzaad24 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(5,25,2,1)";
            s.executeUpdate(sql_kozponti_hozzaad24);
            String sql_kozponti_hozzaad25 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(5,26,4,1)";
            s.executeUpdate(sql_kozponti_hozzaad25);
            String sql_kozponti_hozzaad26 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(5,29,600,2)";
            s.executeUpdate(sql_kozponti_hozzaad26);
            String sql_kozponti_hozzaad27 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(5,30,300,2)";
            s.executeUpdate(sql_kozponti_hozzaad27);
            String sql_kozponti_hozzaad28 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(5,23,3,1)";
            s.executeUpdate(sql_kozponti_hozzaad28);
            String sql_kozponti_hozzaad29 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(5,31,3,1)";
            s.executeUpdate(sql_kozponti_hozzaad29);
            String sql_kozponti_hozzaad30 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(5,27,2,1)";
            s.executeUpdate(sql_kozponti_hozzaad30);
            String sql_kozponti_hozzaad31 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(5,22,500,2)";
            s.executeUpdate(sql_kozponti_hozzaad31);
            String sql_kozponti_hozzaad32 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(5,24,10,1)";
            s.executeUpdate(sql_kozponti_hozzaad32);
            String sql_kozponti_hozzaad33 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(5,32,2,1)";
            s.executeUpdate(sql_kozponti_hozzaad33);
            String sql_kozponti_hozzaad34 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(5,4,2,2)";
            s.executeUpdate(sql_kozponti_hozzaad34);
            String sql_kozponti_hozzaad35 = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) VALUES"
                    + "(5,3,100,3)";
            
            s.executeUpdate(sql_kozponti_hozzaad35);
            kapcsolatZár();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

// </editor-fold>
    
    public static void inic()
    {
        
        if (!tablaLetezik()) tablatLetrehoz();
        if (!tesztAdatBetoltve())tablaTesztAdatok();
    }
    
    
    
    public static void kapcsolatNyit() {
        try {
            Class.forName(DRIVER);
            kapcsolat = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (ClassNotFoundException e) {
            //System.out.println("Hiba! Hiányzik a JDBC driver.");
            throw new RuntimeException("Hiba! Hiányzik a JDBC driver.");
        }
        catch (SQLException e) {
            //System.out.println("Hiba! Nem sikerült megnyitni a kapcsolatot az adatbázis-szerverrel.");
            throw new RuntimeException("Hiba! Nem sikerült megnyitni a kapcsolatot az adatbázis-szerverrel. Kérem ellenőrizze, hogy a \"hr\" felhasználó megfelelően van konfigurálva az Oracle adatbázisban!");
        }
  }

  public static void kapcsolatZár() {
        try {
            kapcsolat.close();
        }
        catch (SQLException e) {
            System.out.println("Hiba! Nem sikerült lezárni a kapcsolatot az adatbázis-szerverrel.");
        }
  }
    
    
    
    
    
    
    public static ArrayList otevoMennyTipusok()
    {
        ArrayList<String> eredmeny = new ArrayList<>() ;
        try {
            kapcsolatNyit();
            Statement s=kapcsolat.createStatement();
            
            String sql = "SELECT nev FROM Mennyiseg";
            ResultSet rs=s.executeQuery(sql);
            while(rs.next()) 
            {
                
                eredmeny.add(rs.getString("nev"));
            }
            kapcsolatZár();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return eredmeny;
    }
    
    public static boolean receptLetezik(String receptNev)
    {
        try {
            kapcsolatNyit();
            Statement s=kapcsolat.createStatement();
            String sql = "select count(id) from Recept where nev='"+receptNev+"'";
            int tablak_szama=0;
            ResultSet rs=s.executeQuery(sql);
            while (rs.next())    tablak_szama=rs.getInt(1);
            System.out.println(sql);
            kapcsolatZár();
            if(tablak_szama>0) return true;
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    
    public static boolean tesztAdatBetoltve()
    {
        try {
            kapcsolatNyit();
            Statement s=kapcsolat.createStatement();
            String sql = "select count(*) from Recept";
            int sorok_szama=0;
            ResultSet rs=s.executeQuery(sql);
            while (rs.next()) sorok_szama=rs.getInt(1);
            kapcsolatZár();
            if(sorok_szama>0) return true;
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    public static void receptetBeszur (Recept recept)
    {
        System.out.println("receptet beszúr");
        try {
            kapcsolatNyit();
            Statement s=kapcsolat.createStatement();
            String sql_recept_id="select seq_recept.nextval from dual";
            ResultSet rs1=s.executeQuery(sql_recept_id);
            rs1.next();
            String recept_id=rs1.getString(1);
            System.out.println("recept id sequence kiválasztva");
            String sql_recept_tabla = "INSERT INTO Recept(id,nev,elkeszites)" +
                    "VALUES" +
                    "('"+recept_id+"','"+recept.getMegnevezes()+"','"+recept.getLeiras()+"')";
            s.executeUpdate(sql_recept_tabla);
            System.out.println("recept hozzáadva");
            
            for (Osszetevok otevo: recept.getOsszetevok())
            {
                String sql_otevo_id="select seq_osszetevo.nextval from dual";
                ResultSet rs2=s.executeQuery(sql_otevo_id);
                rs2.next();
                String otevo_id=rs2.getString(1);
                System.out.println("összetevő id sequence kiválasztva");
                
                String sql_otevo_hozzad = "INSERT INTO Osszetevo(id,nev)" +
                    "VALUES" +
                    "('"+otevo_id+"','"+otevo.getOsszetevo_fajta()+"')";
                s.executeUpdate(sql_otevo_hozzad);
                System.out.println("összetevő hozzáadva");
                
                String sql_mennyiseg_id="select id from Mennyiseg where nev='"+otevo.getMennyiseg_tipus()+"'";
                ResultSet rs3=s.executeQuery(sql_mennyiseg_id);
                rs3.next();
                String mennyiseg_id=rs3.getString(1);
                System.out.println("mennyiség id sequence kiválasztva");
                String sql_kozponti_hozzaad = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id)" +
                    "VALUES" +
                    "('"+recept_id+"','"+otevo_id+"','"+otevo.getMennyiseg_egyseg()+"','"+mennyiseg_id+ "')";
                s.executeUpdate(sql_kozponti_hozzaad);
                System.out.println("központi tábla update-elve");
            }
            kapcsolatZár();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void receptetMent(Recept recept)
    {
        if (!receptLetezik(recept.getMegnevezes())) receptetBeszur(recept);
                else{
                    //exception logika -recept már létezik
                    throw new RuntimeException("Recept már létezik ezzel a névvel. Kérem adjon meg másik recept nevet.");
            //System.out.println("Recept már létezik");
                }
    }
    
    
    
    public static void receptetSzerkeszt(String aktualis, Recept uj)
    {
        try {
            kapcsolatNyit();
            Statement s=kapcsolat.createStatement();
            //Recept nevének és elékszítésének módosítása
            String sql_recept_id="select id from Recept where nev='"+aktualis+"'";
            ResultSet rs1=s.executeQuery(sql_recept_id);
            rs1.next();
            String recept_id=rs1.getString(1);
            
            String sql_recept_tabla = "update Recept SET nev='"+uj.getMegnevezes()+"', elkeszites='"+uj.getLeiras()+"' where id='"+recept_id+"'";
            s.executeUpdate(sql_recept_tabla);
            
            //Összetevőket töröljük ill központi táblát karban tartjuk
            ArrayList<String> osszetevo_id = new ArrayList<>();
            String sql_osszetevok_lista ="select id from Osszetevo where id in (select osszetevo_id FROM Kozponti "
                    + "FULL OUTER JOIN Recept ON Kozponti.recept_id=Recept.id "
                    + "WHERE Recept.nev ='"+recept_id+"')";
            ResultSet rsOsszetevok = s.executeQuery(sql_osszetevok_lista);
            while (rsOsszetevok.next())
            {
                osszetevo_id.add(rsOsszetevok.getString("id"));
            }
            
            String sql2 = "delete from Kozponti where recept_id ='"+recept_id+"'";
            s.executeUpdate(sql2);
            System.out.println("delete from központi");
            for (String id : osszetevo_id)
            {
            String sql1 = "delete from Osszetevo where id ='"+id+"'";
            s.executeUpdate(sql1);
            }
            System.out.println("delete összetevő");
            //Módosított összetevők hozzáadsa, központi tábla karban tartása
            for (Osszetevok otevo: uj.getOsszetevok())
            {
                String sql_otevo_id="select seq_osszetevo.nextval from dual";
                ResultSet rs2=s.executeQuery(sql_otevo_id);
                rs2.next();
                String otevo_id=rs2.getString(1);
                
                String sql_otevo_hozzad = "INSERT INTO Osszetevo(id,nev) " +
                    "VALUES" +
                    "('"+otevo_id+"','"+otevo.getOsszetevo_fajta()+"')";
                s.executeUpdate(sql_otevo_hozzad);
                String sql_mennyiseg_id="select id from Mennyiseg where nev='"+otevo.getMennyiseg_tipus()+"'";
                ResultSet rs3=s.executeQuery(sql_mennyiseg_id);
                rs3.next();
                String mennyiseg_id=rs3.getString(1);
                String sql_kozponti_hozzaad = "INSERT INTO Kozponti(recept_id,osszetevo_id,mennyiseg,mennyiseg_id) " +
                    "VALUES" +
                    "('"+recept_id+"','"+otevo_id+"','"+otevo.getMennyiseg_egyseg()+"','"+mennyiseg_id+ "')";
                s.executeUpdate(sql_kozponti_hozzaad);
                System.out.println("összetevő hozzáadása");
            }
            kapcsolatZár();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static ArrayList<Recept> keresMegnevezesre (String kulcs)
    {
        ArrayList<Recept> eredmeny=new ArrayList<>();
        try {
            kapcsolatNyit();
            Statement s=kapcsolat.createStatement();
            
            String sql = "SELECT nev, elkeszites FROM Recept WHERE nev LIKE '%"+ kulcs+"%'";
            ResultSet rs=s.executeQuery(sql);
            while(rs.next()) 
            {
                
                eredmeny.add(new Recept(rs.getString("nev"), rs.getString("elkeszites")));
            }
            kapcsolatZár();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return eredmeny;
    }
    
    public static ArrayList<Osszetevok> keresOsszetevoRecepthez(String kulcs)
    
    {
        System.out.println("Keres összetevőt recepthez");
        ArrayList<Osszetevok> eredmeny = new ArrayList<>();
        try {
            kapcsolatNyit();
            Statement s=kapcsolat.createStatement();
            
            String sql = "SELECT Kozponti.Mennyiseg,Mennyiseg.Nev,Osszetevo.Nev FROM Kozponti FULL OUTER JOIN Recept ON Kozponti.Recept_id=Recept.id FULL OUTER JOIN Osszetevo ON Kozponti.Osszetevo_id=Osszetevo.id FULL OUTER JOIN Mennyiseg ON Kozponti.Mennyiseg_id=Mennyiseg.id WHERE Recept.nev = '"+ kulcs+"'";
            ResultSet rs=s.executeQuery(sql);
            while(rs.next()) 
            {
                
                eredmeny.add(new Osszetevok(rs.getString(1),rs.getString(2),rs.getString(3)));
                
            }
            kapcsolatZár();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return eredmeny;
    }
    
    
    public static void receptetTorol(String receptNev)
    {
        try {
            kapcsolatNyit();
            Statement s=kapcsolat.createStatement();
            String sql_recept_id="select id from Recept where nev='"+receptNev+"'";
            ResultSet rs1=s.executeQuery(sql_recept_id);
            rs1.next();
            String recept_id=rs1.getString(1);
            
            ArrayList<String> osszetevo_id = new ArrayList<>();
            String sql_osszetevok_lista ="select id from Osszetevo where id in (select osszetevo_id FROM Kozponti "
                    + "FULL OUTER JOIN Recept ON Kozponti.recept_id=Recept.id "
                    + "WHERE Recept.nev ='"+recept_id+"')";
            ResultSet rsOsszetevok = s.executeQuery(sql_osszetevok_lista);
            while (rsOsszetevok.next())
            {
                osszetevo_id.add(rsOsszetevok.getString("id"));
            }
            
            String sql2 = "delete from Kozponti where recept_id ='"+recept_id+"'";
            s.executeUpdate(sql2);
            System.out.println("delete from központi");
            for (String id : osszetevo_id)
            {
            String sql1 = "delete from Osszetevo where id ='"+id+"'";
            s.executeUpdate(sql1);
            }
            System.out.println("delete összetevő");
            
            
            String sql3 = "delete from Recept where nev='"+receptNev+"'";
            s.executeUpdate(sql3);
            kapcsolatZár();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
  
}
