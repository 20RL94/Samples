package at.htl.beatles.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;

@Entity
@Table(name = "B_ALBUM")
@NamedQuery(name = "Album.findAll", query = "select a from Album a")
public class Album implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALB_id", updatable = false, nullable = false)
    private Long id;
    @Version
    @Column(name = "ALB_version")
    private int version;

    @Column(name = "ALB_TITLE")
    private String title;

    @Column(name = "ALB_RELEASE_DATE")
    private LocalDate released;

    @Column(name = "ALB_LABEL")
    private String label;

    @Column(name = "ALB_UK_CHART_POS")
    private Integer ukChartPosition;

    @Column(name = "ALB_US_CHART_POS")
    private Integer usChartPosition;

    @Column(name = "ALB_BPI_CERT")
    private String bpiCertification;

    @Column(name = "ALB_RIAA_CERT")
    private String riaaCertification;

    public Album() {
    }

    public Album(String title, LocalDate released, String label, int ukChartPosition, int usChartPosition, String bpiCertification, String riaaCertification) {
        this.title = title;
        this.released = released;
        this.label = label;
        this.ukChartPosition = ukChartPosition;
        this.usChartPosition = usChartPosition;
        this.bpiCertification = bpiCertification;
        this.riaaCertification = riaaCertification;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public LocalDate getReleased() {
        return released;
    }

    public void setReleased(LocalDate released) {
        this.released = released;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getUkChartPosition() {
        return ukChartPosition;
    }

    public void setUkChartPosition(Integer ukChartPosition) {
        this.ukChartPosition = ukChartPosition;
    }

    public Integer getUsChartPosition() {
        return usChartPosition;
    }

    public void setUsChartPosition(Integer usChartPosition) {
        this.usChartPosition = usChartPosition;
    }

    public String getBpiCertification() {
        return bpiCertification;
    }

    public void setBpiCertification(String bpiCertification) {
        this.bpiCertification = bpiCertification;
    }

    public String getRiaaCertification() {
        return riaaCertification;
    }

    public void setRiaaCertification(String riaaCertification) {
        this.riaaCertification = riaaCertification;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Album)) {
            return false;
        }
        Album other = (Album) obj;
        if (id != null) {
            if (!id.equals(other.id)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (label != null && !label.trim().isEmpty())
            result += "label: " + label;
        result += ", ukChartPosition: " + ukChartPosition;
        result += ", usChartPosition: " + usChartPosition;
        if (bpiCertification != null && !bpiCertification.trim().isEmpty())
            result += ", bpiCertification: " + bpiCertification;
        if (riaaCertification != null && !riaaCertification.trim().isEmpty())
            result += ", riaaCertification: " + riaaCertification;
        if (title != null && !title.trim().isEmpty())
            result += ", title: " + title;
        result += released.format(DateTimeFormatter.ISO_DATE);
        return result;
    }
}