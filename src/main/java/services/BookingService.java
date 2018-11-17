package services;

import models.Booking;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class BookingService {

    @PersistenceContext(unitName = "MyPU")
    EntityManager em;

    public List<Booking> getAll() {
        return em.createNamedQuery("Booking.findAll", Booking.class).getResultList();
    }

    public Booking getBooking(int id) {
        return em.createNamedQuery("Booking.findById", Booking.class).setParameter("id", id).getSingleResult();
    }

    public Booking placeBooking(Booking booking) {
        em.persist(booking);
        return booking;
    }

}
