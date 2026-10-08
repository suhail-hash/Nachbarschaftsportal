package de.hsos.swa.messaging.boundary.pages;
import de.hsos.swa.messaging.control.MessageDirectory;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import static java.util.Objects.requireNonNull;


@Path("/inbox")
@ApplicationScoped
public class InboxPage {

    @Inject
    MessageDirectory messageDirectory;

    @Inject
    JsonWebToken jwt;

    private final Template page;

    public InboxPage(Template inbox) {
        this.page = requireNonNull(inbox);
    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    @Authenticated
    public TemplateInstance get() {

        Long userId = messageDirectory.idByEmail(jwt.getClaim("email"));

        return page.data("userId", userId);
    }
}