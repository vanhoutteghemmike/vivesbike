/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import database.connect.ConnectionManager;
import exception.DBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author r0659835
 */
public class Removal {
    
       /**
     * Verwijderd een lid adh van een rijksregisternummer
     * 
     * @param rijksregisternummer lid die verwijderd moet worden
     * (rijksregisternummer)
     * 
     * @throws exception.DBException Exception die duidt op een verkeerde
     * installatie van de database of een fout in de query.
     */
    public void verwijderenLid(String rijksregisternummer) throws DBException {
        if (!rijksregisternummer.equals("")) {
            try (Connection conn = ConnectionManager.getConnection();) {
                try (PreparedStatement stmt = conn.
                        prepareStatement(
                                "delete"
                                + " from lid "
                                + " where rijksregisternummer = ?");) {

                    stmt.setString(1, rijksregisternummer);
                    stmt.execute();

                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in zoekKlant - statement" + sqlEx);
                }
            } catch (SQLException ex) {
                throw new DBException("SQL-exception in zoekKlant - connection");
            }
        }
    }
    
}
