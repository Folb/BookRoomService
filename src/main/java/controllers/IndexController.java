package controllers;

import models.Booking;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;
import services.BookingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.logging.Logger;

@Path("/")
@ApplicationScoped
@Transactional
public class IndexController {

    @Inject
    @ConfigurationValue("some.string.property")
    private String stringProperty;

    @Inject
    private BookingService bookingService;

    private static final Logger LOG = Logger.getLogger(IndexController.class.getName());

    @GET
    @Path("all")
    @Produces("application/json")
    public List<Booking> get() {
        return bookingService.getAll();
    }

    @GET
    @Path("str")
    @Produces("plain/text")
    public String getStr() {
        return stringProperty;
    }

    @GET
    @Path("isBooked/{id}")
    @Produces("application/json")
    public Booking isRoomBooked(@PathParam("id") String id) {
        Integer parsedId = null;

        try {
            parsedId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            LOG.warning("Could not parse " + id + " to int.");
        }

        Booking booking = null;

        try {
            booking = bookingService.getBooking(parsedId);
        } catch (NoResultException e) {
            LOG.warning("Could not find booking with id: " + parsedId);
        }

        return booking;
    }


}
