package ma.projet.arrosageintellegentv2.beans;

import java.util.List;

public class reponse {
    private AppUser user;
    private List<EspaceVert> espacesVerts;

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public List<EspaceVert> getEspacesVerts() {
        return espacesVerts;
    }

    public void setEspacesVerts(List<EspaceVert> espacesVerts) {
        this.espacesVerts = espacesVerts;
    }
// Constructors, getters, and setters
}

