package at.htl.beatles.business;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.ejb.Stateless;
import javax.json.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.htl.beatles.entity.Album;

import static javax.json.JsonValue.*;
import static javax.json.JsonValue.ValueType.*;

/**
 * DAO for Album
 */
@Stateless
public class AlbumFacade {

    @PersistenceContext
    private EntityManager em;

    public void create(Album entity) {
        em.persist(entity);
    }

    public void deleteById(Long id) {
        Album entity = em.find(Album.class, id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    public Album update(Album entity) {
        return em.merge(entity);
    }

    public List<Album> findAll() {
        TypedQuery<Album> query = em.createNamedQuery("Album.findAll", Album.class);
        return query.getResultList();
    }

    public Album findById(Long id) {
        return em.find(Album.class, id);
    }

    public List<Album> listAll(Integer startPosition, Integer maxResult) {
        TypedQuery<Album> findAllQuery = em.createQuery(
                "SELECT DISTINCT a FROM Album a ORDER BY a.id", Album.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        return findAllQuery.getResultList();
    }

    public void readJsonContent(String json) {

        JsonStructure structure;

        try (StringReader stringReader = new StringReader(json)) {
            structure = Json.createReader(stringReader).read();
        } catch (Exception e) {
            System.err.println("Json FileFormat not correct: " + e.getMessage());
            throw new JsonException("Json FileFormat not correct");
            //return false;
        }

        switch (structure.getValueType()) {
            case ARRAY:
                JsonArray jsonArray = (JsonArray) structure;

                for (JsonValue jsonValue : jsonArray) {
                    saveJsonObject((JsonObject) jsonValue);
                }
                break;
            case OBJECT:
                saveJsonObject((JsonObject) structure);
                break;
            default:
                throw new JsonException("Unexpected Error while parsing Json");
                //return false;
        }
        //return true;
    }

    private void saveJsonObject(JsonObject jsonAlbum) {
        Album album = new Album();

        LocalDate released;
        album.setTitle(jsonAlbum.getString("Title"));

        try {
            released = LocalDate.parse(jsonAlbum.getString("Released"), DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        } catch (Exception e) {
            released = LocalDate.parse(jsonAlbum.getString("Released"), DateTimeFormatter.ofPattern("d MMMM yyyy"));
        }
        album.setReleased(released);

        album.setLabel(jsonAlbum.getString("Label"));

        try {
            Integer ukChartPos = Integer.valueOf(jsonAlbum.getString("UK Chart Position"));
            System.out.println("uk " + ukChartPos);
            album.setUkChartPosition(ukChartPos);
        } catch (Exception e) {
            System.err.println("'" + jsonAlbum.getString("UK Chart Position") + "' --> " + e.getMessage());
        }

        try {
            Integer usChartPos = Integer.valueOf(jsonAlbum.getString("US Chart Position"));
            System.out.println("us " + usChartPos);
            album.setUsChartPosition(usChartPos);
        } catch (Exception e) {
            System.err.println("'" + jsonAlbum.getString("US Chart Position") + "' --> " + e.getMessage());
        }

        album.setBpiCertification(jsonAlbum.getString("BPI Certification"));
        album.setRiaaCertification(jsonAlbum.getString("RIAA Certification"));

        create(album);
        System.out.println(album);
    }

    public int countRiaaCertifications(String certification) {

        int countCert = 0;

        List<String> certifications = em
                .createQuery("select a.riaaCertification from Album a where a.riaaCertification like :CERT",String.class)
                .setParameter("CERT", "%" + certification)
                .getResultList();

        for (String cert : certifications) {
            String[] elements = cert.split("x");
            if (elements.length == 1) {
                countCert++;
            } else {
                countCert += Integer.parseInt(elements[0]);
            }
        }

        return countCert;
    }
}
