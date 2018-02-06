package database;

import databag.Rit;
import database.connect.ConnectionManager;
import exception.DBException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

public class RitDB implements InterfaceRitDB {

    /**
     * Voegt een rit toe. Het id wordt automatisch gegenereerd door de database
     *
     * @param rit de rit die toegevoegd moet worden
     * @return gegenereerd id van de rit die net werd toegevoegd of null indien
     * geen rit werd opgegeven.
     * @throws exception.DBException Exception die duidt op een verkeerde
     * installatie van de database of een fout in de query.
     * @throws java.sql.SQLException Exception die duidt op een verkeerde
     * table van de database die aangesproken wordt
     */
    public Integer toevoegenRit(Rit rit) throws DBException, SQLException {
        if (rit != null) {
            Integer primaryKey = null;
            
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();) {
                // preparedStatement opstellen (en automatisch sluiten)
                try (PreparedStatement stmt = conn.prepareStatement(
                        "insert into rit(starttijd"
                        + " , eindtijd"
                        + " , prijs"
                        + " , lid_rijksregisternummer"
                        + " , fiets_registratienummer"
                        + " ) values(?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);) {
                    
                // wordt in private methodes geplaats    
                // omzetten van LocalDateTime naar String    
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    // omzetten STARTTIJD van LocalDateTime naar String
                LocalDateTime getStartTijd = rit.getStarttijd();   
                String ritStartTijd = getStartTijd.format(formatter);
                    // omzetten EINDTIJD van LocalDateTime naar String
                LocalDateTime getEindTijd = rit.getEindtijd();   
                String ritEindTijd = getEindTijd.format(formatter);
                
                
                stmt.setString(1, ritStartTijd);
                stmt.setString(2, ritEindTijd);
                stmt.setBigDecimal(3, rit.getPrijs());
                stmt.setString(4, rit.getLidRijksregisternummer());
                stmt.setInt(5, rit.getFietsRegistratienummer());
                stmt.execute();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    primaryKey = generatedKeys.getInt(1);
                }
            } catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in toevoegenRit "
                        + "- statement" + sqlEx);
            }
        }catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in toevoegenRit "
                        + "- connection" + sqlEx);
            }
        return primaryKey;
    }
        else {
            return null;
    }
}

/**
 * Sluit een rekening met meegegeven rekeningnummer.
 *
 * @param rit ritnummer van de rit die gesloten moet worden.
 * @throws exception.DBException Exception die duidt op een verkeerde
 * installatie van de database of een fout in de query.
 */
@Override
public void afsluitenRit(Rit rit)  throws DBException, SQLException {
        if (rit != null) {
            // connectie tot stand brengen (en automatisch sluiten)
                try (Connection conn = ConnectionManager.getConnection();) {
                    // preparedStatement opstellen (en automatisch sluiten)
        try (PreparedStatement stmt = conn.
                        prepareStatement(
                                "update rekening "
                                + " set status = ? "
                                + " where rekeningnummer = ?");) {

                    //stmt.setString(1, RekeningStatus.GESLOTEN.toString());
                    //stmt.setString(2, rekeningnummer);
                    stmt.execute();
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in verwijderRekening" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in verwijderRekening - connection" + sqlEx);
            }
        }
    }

    
    
    
    
    
    
    @Override
        public ArrayList zoekAlleRitten() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
        public Rit zoekRit(Integer ritID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
        public int zoekEersteRitVanLid(String rr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
        public ArrayList zoekActieveRittenVanLid(String rr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
        public ArrayList zoekActieveRittenVanFiets(Integer regnr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
