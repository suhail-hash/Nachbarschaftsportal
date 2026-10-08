package de.hsos.swa.friendship.boundary.pages;

import de.hsos.swa.friendship.control.FriendshipDirectory;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import static java.util.Objects.requireNonNull;

@Path("/friends")
public class FriendsPage {

    @Inject
    FriendshipDirectory friendshipDirectory;

    @Inject
    JsonWebToken jwt;


    private final Template page;

    public FriendsPage(Template friends) {
        this.page = requireNonNull(friends);
    }



    @Authenticated
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {

        Long userId = friendshipDirectory.idByEmail(jwt.getClaim("preferred_username"));

        return page.data("userId", userId);
    }
}
