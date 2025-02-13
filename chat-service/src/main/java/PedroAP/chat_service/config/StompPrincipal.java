package PedroAP.chat_service.config;

import java.security.Principal;

public class StompPrincipal implements Principal {

    private String nick;

    public StompPrincipal(String nick) {
        this.nick = nick;
    }

    @Override
    public String getName() {
        return nick;
    }
}
