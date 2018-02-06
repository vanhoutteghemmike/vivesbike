package database;

import databag.Lid;
import database.connect.ConnectionManager;
import datatype.Geslacht;
import datatype.Rijksregisternummer;
import exception.ApplicationException;
import exception.DBException;
import static java.lang.String.valueOf;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDate;

public class LidDB implements InterfaceLidDB {

    /**
     * Voegt een lid toe. Het id wordt automatisch gegenereerd door de database
     *
     * @param lid de lid die toegevoegd moet worden
     *
     * @throws exception.DBException Exception die duidt op een verkeerde
     * installatie van de database of een fout in de query.
     */
    @Override
    public void toevoegenLid(Lid lid) throws DBException {
        // controleren dat het object niet leeg is
        if (lid != null) {
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();) {
                // preparedStatement opstellen (en automatisch sluiten)

                try (PreparedStatement stmt = conn.prepareStatement(
                        "insert into lid(rijksregisternummer"
                        + " , voornaam"
                        + " , naam"
                        + " , geslacht"
                        + " , telnr"
                        + " , emailadres"
                        + " , start_lidmaatschap"
                        + " , opmerkingen"
                        + " ) values(?,?,?,?,?,?,?,?)");) {
                    stmt.setString(1, lid.getRijksregisternummer());
                    stmt.setString(2, lid.getVoornaam());
                    stmt.setString(3, lid.getNaam());
                    stmt.setString(4, valueOf(lid.getGeslacht()));
                    stmt.setString(5, lid.getTelnr());
                    stmt.setString(6, lid.getEmailadres());
                    stmt.setDate(7, Date.valueOf(LocalDate.now()));
                    stmt.setString(8, lid.getOpmerkingen());

                    stmt.execute();
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in toevoegenLid "
                            + "- statement" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in toevoegenLid "
                        + "- connection" + sqlEx);
            }
        }
    }

    /**
     * Wijzigt een lid adhv zijn id.
     *
     * @param lid lid die gewijzigd moet worden
     *
     * @throws exception.DBException Exception die duidt op een verkeerde
     * installatie van de database of een fout in de query.
     */
    @Override
    public void wijzigenLid(Lid lid) throws DBException {
        // controleren dat het object niet leeg is
        if (lid != null) {
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();) {
                // preparedStatement opstellen (en automatisch sluiten)
                try (PreparedStatement stmt = conn.
                        prepareStatement("update lid "
                                + " set voornaam =?"
                                + "   , naam = ?"
                                + "   , geslacht = ?"
                                + "   , telnr = ? "
                                + "   , emailadres = ? "
                                + "   , start_lidmaatschap = ? "
                                + "   , opmerkingen = ? "
                                + " where rijksregisternummer = ?");) {

                    stmt.setString(1, lid.getVoornaam());
                    stmt.setString(2, lid.getNaam());
                    stmt.setString(3, valueOf(lid.getGeslacht()));
                    stmt.setString(4, lid.getTelnr());
                    stmt.setString(5, lid.getEmailadres());
                    stmt.setDate(6, Date.valueOf(lid.getStart_lidmaatschap()));
                    stmt.setString(7, lid.getOpmerkingen());
                    // als het id null is, kan deze niet zomaar omgezet worden naar een int
                    if (lid.getRijksregisternummer() != null) {
                        stmt.setString(8, lid.getRijksregisternummer());
                    } else {
                        stmt.setNull(8, java.sql.Types.NULL);
                    }
                    stmt.execute();
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in wijzigenLid" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in wijzigenLid - connection" + sqlEx);
            }
        }
    }

    /**
     * Schrijft de lid met meegegeven rijksregisternummer uit.
     *
     * @param rijksregisternummer rijksregisternummer van het lid die
     * uitgeschreven moet worden
     *
     * @throws exception.DBException Exception die duidt op een verkeerde
     * installatie van de database of een fout in de query.
     */
    @Override
    public void uitschrijvenLid(String rijksregisternummer) throws DBException {
        // controleren dat het rijksregister niet leeg is
        if (!rijksregisternummer.equals("")) {
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();) {
                // preparedStatement opstellen (en automatisch sluiten)
                try (PreparedStatement stmt = conn.prepareStatement(
                        "update lid "
                        + " set einde_lidmaatschap = ?"
                        + " where rijksregisternummer = ?");) {
                    stmt.setDate(1, Date.valueOf(LocalDate.now()));
                    stmt.setString(2, rijksregisternummer);
                    stmt.execute();
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in uitschrijvenLid - statement" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in uitschrijvenLid - connection" + sqlEx);
            }
        }
    }

    /**
     * Zoekt adhv het rijksregisternummer een lid op. Wanneer geen lid werd
     * gevonden, wordt null teruggegeven.
     *
     * @param rijksregisternummer lid die gezocht moet worden
     * (rijksregisternummer)
     *
     * @return lid die gezocht wordt, null indien het lid niet werd gevonden.
     *
     * @throws exception.DBException Exception die duidt op een verkeerde
     * installatie van de database of een fout in de query.
     *
     * @throws exception.ApplicationException Exception die duidt op een
     * verkeerde input van rijksregisternummer
     */
    @Override
    public Lid zoekLid(String rijksregisternummer) throws DBException, ApplicationException {
        // controleren dat het rijksregister niet leeg is
        if (!rijksregisternummer.equals("")) {
            Lid returnLid = null;
            // connectie tot stand brengen (en automatisch sluiten)
            try (Connection conn = ConnectionManager.getConnection();) {
                // preparedStatement opstellen (en automatisch sluiten)
                try (PreparedStatement stmt = conn.prepareStatement(
                        "select voornaam"
                        + " , naam"
                        + " , geslacht"
                        + " , telnr"
                        + " , emailadres"
                        + " , start_lidmaatschap "
                        + " , einde_lidmaatschap "
                        + " , opmerkingen "
                        + " from lid "
                        + " where rijksregisternummer = ? ");) {

                    stmt.setString(1, rijksregisternummer);
                    stmt.execute();
                    // result opvragen (en automatisch sluiten)
                    try (ResultSet r = stmt.getResultSet()) {
                        if (r.next()) {
                            // geen controle op null, id moet ingevuld zijn in DB
                            Lid lr = new Lid();
                            // applicatieStatement controleren (en toevoegen)
                            try {
                                Rijksregisternummer rijksnr = new Rijksregisternummer(rijksregisternummer);
                                lr.setRijksregisternummer(rijksnr);
                            } catch (ApplicationException AEx) {
                                throw new ApplicationException("Application-exception - rijksregisternummer ongeldig" + AEx);
                            }
                            lr.setNaam(r.getString("naam"));
                            lr.setVoornaam(r.getString("voornaam"));
                            lr.setGeslacht(Geslacht.valueOf(r.getString("geslacht")));
                            lr.setTelnr(r.getString("telnr"));
                            lr.setEmailadres(r.getString("emailadres"));
                            lr.setStart_lidmaatschap(r.getDate("start_lidmaatschap").toLocalDate());
                            lr.setEinde_lidmaatschap(r.getDate("einde_lidmaatschap").toLocalDate());
                            lr.setOpmerkingen(r.getString("opmerkingen"));
                            returnLid = lr;
                        }
                        return returnLid;

                    } catch (SQLException sqlEx) {
                        throw new DBException("SQL-exception in zoekLid - resultset" + sqlEx);
                    }
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in zoekLid - statement" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException(
                        "SQL-exception in zoekLid - connection" + sqlEx);
            }
        } else {
            // geen lid opgegeven
            return null;
        }
    }

    /**
     * Geeft alle leden terug in een lijst, gesorteerd op naam, voornaam
     *
     * @return lijst van alle leden
     *
     * @throws exception.DBException Exception die duidt op een verkeerde
     * installatie van de database of een fout in de query.
     *
     * @throws exception.ApplicationException Exception die duidt op een
     * verkeerde input van rijksregisternummer
     */
    @Override
    public ArrayList<Lid> zoekAlleLeden() throws DBException, ApplicationException {
        ArrayList<Lid> ld = new ArrayList<>();
        // connectie tot stand brengen (en automatisch sluiten)
        try (Connection conn = ConnectionManager.getConnection();) {

            // preparedStatement opstellen (en automatisch sluiten)
            try (PreparedStatement stmt = conn.
                    prepareStatement(
                            "select * "
                            + " from lid "
                            + " order by naam"
                            + "        , voornaam");) {
                stmt.execute();
                // result opvragen (en automatisch sluiten)
                try (ResultSet r = stmt.getResultSet()) {
                    // van alle leden uit de database Lid-objecten maken
                    // en in een lijst steken
                    while (r.next()) {
                        Lid l = new Lid();
                        // geen controle op null, id moet ingevuld zijn in DB
                        Rijksregisternummer rijksnr = new Rijksregisternummer(r.getString("rijksregisternummer"));
                        l.setRijksregisternummer(rijksnr);
                        l.setNaam(r.getString("naam"));
                        l.setVoornaam(r.getString("voornaam"));
                        l.setGeslacht(Geslacht.valueOf(r.getString("geslacht")));
                        l.setTelnr(r.getString("telnr"));
                        l.setEmailadres(r.getString("emailadres"));
                        l.setStart_lidmaatschap(r.getDate("start_lidmaatschap").toLocalDate());
                        if (r.getDate("einde_lidmaatschap") != null) {
                            l.setEinde_lidmaatschap(r.getDate("einde_lidmaatschap").toLocalDate());
                        } else {
                            l.setEinde_lidmaatschap(null);
                        }
                        l.setOpmerkingen(r.getString("opmerkingen"));
                        ld.add(l);
                    }
                    return ld;
                } catch (SQLException sqlEx) {
                    throw new DBException("SQL-exception in zoekIngeschrevenLeden - resultset" + sqlEx);
                }
            } catch (SQLException sqlEx) {
                throw new DBException("SQL-exception in zoekIngeschrevenLeden - statement" + sqlEx);
            }
        } catch (SQLException sqlEx) {
            throw new DBException("SQL-exception in zoekIngeschrevenLeden - connection" + sqlEx);
        }
    }
}
