package database;

import databag.Fiets;
import database.connect.ConnectionManager;
import datatype.Standplaats;
import datatype.Status;
import exception.ApplicationException;
import exception.DBException;
import static java.lang.String.valueOf;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class FietsDB implements InterfaceFietsDB {
  
     /**
     * Voegt een fiets toe. Het id wordt automatisch gegenereerd door de database
     *
     * @param fiets de fiets die toegevoegd moet worden
     * 
     * @return 
     *
     * @throws exception.DBException Exception die duidt op een verkeerde
     * installatie van de database of een fout in de query.
     */
    @Override
    public Integer toevoegenFiets(Fiets fiets) throws DBException  {
        // controleren dat het object niet leeg is
        if (fiets != null) {   
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();) {
                // preparedStatement opstellen (en automatisch sluiten)

                try (PreparedStatement stmt = conn.prepareStatement(
                        "insert into fiets(status"
                        + " , standplaats"
                        + " , opmerkingen"
                        + " ) values(?,?,?)");) {
                    stmt.setString(1, valueOf(fiets.getStatus()));
                    stmt.setString(2, valueOf(fiets.getStandplaats()));
                    stmt.setString(3, fiets.getOpmerking());
                    stmt.execute();
                    
                } catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in toevoegenFiets "
                        + "- statement" + sqlEx);
            }
            }catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in toevoegenFiets "
                        + "- statement" + sqlEx);
            }
        }
        return null; // Statement was verandert naar void maar moet in integer, wat teruggeven?
    }

    
    @Override
    public void wijzigenToestandFiets(Integer regnr, Status status)  {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void wijzigenOpmerkingFiets(Integer regnr, String opmerking)  {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
     /**
     * Zoekt adhv het registratienummer een fiets op. Wanneer geen fiets werd
     * gevonden, wordt null teruggegeven.
     *
     * @param regnr fiets die gezocht moet worden
     * (registratienummer)
     *
     * @return fiets die gezocht wordt, null indien de fiets niet werd gevonden.
     *
     * @throws exception.DBException Exception die duidt op een verkeerde
     * installatie van de database of een fout in de query.
     *
     */
    @Override
    public Fiets zoekFiets(Integer regnr) throws DBException {
        // controleren dat het regnr niet leeg is
        if (regnr != null) {
            Fiets returnFiets = null;
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();) {
                // preparedStatement opstellen (en automatisch sluiten)
                try (PreparedStatement stmt = conn.prepareStatement(
                        "select status"
                        + " , standplaats"
                        + " , opmerkingen"
                        + " from fiets "
                        + " where registratienummer = ? ");) {
                    
                        stmt.setInt(1, regnr);
                        stmt.execute();
                        // result opvragen (en automatisch sluiten)
                        try (ResultSet r = stmt.getResultSet()) {
                            if (r.next()) {
                            // geen controle op null, id moet ingevuld zijn in DB
                            Fiets fiets = new Fiets();

                                Integer registratienummer = regnr;
                                fiets.setRegistratienummer(registratienummer);
                                
                                fiets.setStatus(Status.valueOf(r.getString("status")));
                                fiets.setStandplaats(Standplaats.valueOf(r.getString("standplaats")));
                                fiets.setOpmerking(r.getString("opmerkingen"));
                                returnFiets = fiets;
                            }
                            return returnFiets; // geeft geen fiets terug in de TestDB?
                    } catch (SQLException sqlEx) {
                        throw new DBException("SQL-exception in zoekFiets - resultset" + sqlEx);
                    }
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in zoekFiets - statement" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in zoekFiets - connection" + sqlEx);
            }
        } else {
            // geen fiets opgegeven
            return null;
        }
    }

    @Override
    public ArrayList<Fiets> zoekAlleFietsen()  {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}
