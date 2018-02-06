/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import databag.Fiets;
import databag.Lid;
import datatype.Geslacht;
import datatype.Rijksregisternummer;
import datatype.Standplaats;
import datatype.Status;
import exception.ApplicationException;
import exception.DBException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author r0659835
 */
public class TestDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws DBException  {
        // TODO code application logic here
            // opmaak lid objecten
            Lid lid = new Lid();
            LidDB liddb = new LidDB();
            
            //opmaak fiets objecten
            Fiets fiets = new Fiets();
            FietsDB fietsdb = new FietsDB();
            
            Removal remove = new Removal();
         
        // LID TESTEN
        try{
            Rijksregisternummer r = new Rijksregisternummer("99121200156");
            lid.setNaam("Mike");
            lid.setRijksregisternummer(r);
            lid.setVoornaam("Pietje");
            lid.setGeslacht(Geslacht.M);
            lid.setTelnr("05616516");
            lid.setEmailadres("pietje.pinte@gmail.ont");
            lid.setStart_lidmaatschap(LocalDate.now());
            liddb.toevoegenLid(lid);
        }
        catch(DBException ex)
        {
            System.out.println("Deze fout trad op: " + ex.getMessage());
        } catch (ApplicationException ex) {
            System.out.println("Deze fout trad op (application): " + ex.getMessage());
        }
       
        
        try {
            lid.setVoornaam("Xander");
            liddb.wijzigenLid(lid);
        }
        catch(DBException ex)
        {
            System.out.println("Deze fout trad op: " + ex.getMessage());
        }
        
        try {
            liddb.uitschrijvenLid("99121200156");
        }
        catch(DBException ex)
        {
            System.out.println("Deze fout trad op: " + ex.getMessage());
        }
        
        try {
            Lid lidTest = liddb.zoekLid("99121200156");
            System.out.println(lidTest.toString());
        }
        catch(DBException ex)
        {
            System.out.println("Deze fout trad op: " + ex.getMessage());
        } catch (ApplicationException ex) {
            System.out.println("Deze fout trad op (application): " + ex.getMessage());
        }
        try {
            ArrayList<Lid> ld = new ArrayList<>();
            ld = liddb.zoekAlleLeden();
            for(Lid l : ld)
            {
                System.out.println(l.toString());
            }
        }
        catch(DBException ex)
        {
            System.out.println("Deze fout trad op: " + ex.getMessage());
        } catch (ApplicationException ex) {
            System.out.println("Deze fout trad op (application): " + ex.getMessage());
        }
        
        // FIETS TESTEN
        try{
            fiets.setRegistratienummer(2);
            fiets.setStatus(Status.actief);
            fiets.setStandplaats(Standplaats.Tielt);
            fiets.setOpmerking("nieuwe fiets");
            fietsdb.toevoegenFiets(fiets);
        }
        catch(DBException ex)
        {
            System.out.println("Deze fout trad op: " + ex.getMessage());
        } 
        
        try{
            Fiets fietsTest = fietsdb.zoekFiets(2);
            System.out.println(fietsTest.toString());
        }
        catch(DBException ex)
        {
            System.out.println("Deze fout trad op: " + ex.getMessage());
        } 
        
        
        finally
        {
            remove.verwijderenLid("99121200156");
            System.out.println("Lid succesvol verwijderd!");
        }
    }
    
}
