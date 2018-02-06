package database;

import databag.Fiets;
import database.connect.ConnectionManager;
import datatype.Status;
import exception.DBException;
import static java.lang.String.valueOf;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class FietsDB implements InterfaceFietsDB {

    
    // TODO
    @Override
    public Integer toevoegenFiets(Fiets f) throws DBException  {
        if (f != null) {
            Integer primaryKey = null;
            
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();) {
                // preparedStatement opstellen (en automatisch sluiten)

                try (PreparedStatement stmt = conn.prepareStatement(
                        "insert into fiets(status"
                        + " , standplaats"
                        + " , opmerkingen"
                        + " ) values(?,?,?)", 
                        Statement.RETURN_GENERATED_KEYS);) {
                    stmt.setString(1, valueOf(f.getStatus()));
                    stmt.setString(2, valueOf(f.getStandplaats()));
                    stmt.setString(3, f.getOpmerking());
                    stmt.execute();
                    
                }
            } catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in toevoegenLid "
                        + "- statement" + sqlEx);
            }
        }
        return 5;
    }

    @Override
    public void wijzigenToestandFiets(Integer regnr, Status status)  {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void wijzigenOpmerkingFiets(Integer regnr, String opmerking)  {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Fiets zoekFiets(Integer regnr)  {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Fiets> zoekAlleFietsen()  {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}
