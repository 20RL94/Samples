package at.htl.beatles.web;

import at.htl.beatles.business.AlbumFacade;
import at.htl.beatles.entity.Album;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class IndexController implements Serializable {

    @Inject
    private AlbumFacade albumFacade;

    private String uploadedText;

    List<Album> albums;

    public IndexController() {
    }

    @PostConstruct
    private void init() {
        loadAlbums();
    }

    public void fileUploadHandler(FileUploadEvent event) {
        uploadedText = new String(event.getFile().getContents());
        try {
            albumFacade.readJsonContent(uploadedText);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception e) {
            System.err.println("*** parsing to json faild");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Import failed", event.getFile().getFileName() + " is not uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        loadAlbums();

        // Update der dataTable
        RequestContext.getCurrentInstance().update("albumTable");
        RequestContext.getCurrentInstance().update("certificationPanel");
    }

    public int getSilverCertifications() {
        return albumFacade.countRiaaCertifications("Silver");
    }

    public int getGoldCertifications() {
        return albumFacade.countRiaaCertifications("Gold");
    }

    public int getPlatinumCertifications() {
        return albumFacade.countRiaaCertifications("Platinum");
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public String getUploadedText() {
        return uploadedText;
    }

    public void setUploadedText(String uploadedText) {
        this.uploadedText = uploadedText;
    }

    private void loadAlbums() {
        albums = albumFacade.findAll();
    }

}
