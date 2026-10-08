package de.hsos.swa.messaging.boundary.pages;
import de.hsos.swa.messaging.control.MessageDirectory;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import static java.util.Objects.requireNonNull;


@Path("/chat")
@Authenticated
public class ChatPage {

    @Inject
    MessageDirectory messageDirectory;

    @Inject
    JsonWebToken jwt;

    private final Template page;

    public ChatPage(Template chat) {
        this.page = requireNonNull(chat);
    }

    @GET
    @Path("/{partnerId}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@PathParam("partnerId") Long partnerId) {

        Long userId = messageDirectory.idByEmail(jwt.getClaim("email"));

        return page
                .data("userId", userId)
                .data("partnerId", partnerId);
    }
}