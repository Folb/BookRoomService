package controllers;

import models.Booking;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;
import services.BookingService;
import utils.Parser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import java.time.LocalDateTime;
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

    @POST
    @Path("bookRoom/{roomId}")
    @Produces("application/json")
    public Booking bookRoom(@PathParam("roomId") String roomID,
                            @HeaderParam("uId") String uId,
                            @HeaderParam("startDate") String startDate,
                            @HeaderParam("endDate") String endDate
                            ) {
        Integer parsedRoomId = Parser.parseInt("room", roomID);
        Integer parsedUserId = Parser.parseInt("user", uId);
        LocalDateTime parsedStartDate = Parser.parseDate("start", startDate);
        LocalDateTime parsedEndDate = Parser.parseDate("end", endDate);

        Booking booking = new Booking(parsedRoomId, parsedUserId, parsedStartDate, parsedEndDate);
        booking = bookingService.placeBooking(booking);
        return booking;
    }

    @GET
    @Path("user/{userId}/bookings")
    @Produces("application/json")
    public List<Booking> getBookingsByUserId(@PathParam("userId") String userId) {
        Integer parsedUserId = null;

        try {
            parsedUserId = Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            LOG.warning("Could not parse " + userId + " to int.");
        }

        return bookingService.getAllByUserId(parsedUserId);

    }

}
