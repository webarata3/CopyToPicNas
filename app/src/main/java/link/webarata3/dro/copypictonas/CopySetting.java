package link.webarata3.dro.copypictonas;

import android.support.annotation.NonNull;

public class CopySetting {
    private String localPath;
    private String serverPath;
    private String serverId;
    private String serverPassword;

    public CopySetting(@NonNull String localPath, @NonNull String serverPath,
                       @NonNull String serverId, @NonNull String serverPassword) {
        this.localPath = localPath;
        this.serverPath = serverPath;
        this.serverId = serverId;
        this.serverPassword = serverPassword;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }
}
