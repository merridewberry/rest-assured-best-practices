package booker.test;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import booker.pojo.Booking;
import booker.pojo.BookingDates;
import booker.pojo.BookingInfo;
import io.qameta.allure.Description;

import static booker.endpoints.booking.Booking.createBooking;
import static booker.endpoints.booking.Booking.createBooking_valid;
import static booker.endpoints.booking.Booking.getBookings;
import static booker.endpoints.booking.Booking.getBookings_valid;
import static booker.endpoints.booking.id.Id.getBooking;
import static booker.endpoints.booking.id.Id.getBooking_valid;
import static booker.endpoints.booking.id.Id.updateBooking;
import static booker.utils.SetToken.setToken;
import static org.hamcrest.Matchers.hasItem;

public class MainTest {
    BookingDates bookingDates = new BookingDates("2021-08-31", "2021-09-10");
    Booking booking = new Booking("John", "Doe", 100, true, bookingDates, "Breakfast");

    @BeforeTest
    public void token() {
        setToken();
    }

    @Test
    @Description("Creating new booking creation")
    public void createBookingTest() {
        BookingInfo newBooking = postAndGetInfo();

        Assert.assertEquals(newBooking.getBooking().toString(), booking.toString());
    }

    @Test
    @Description("Getting the list of all the bookings' IDs")
    public void getBookingsTest() {
        getBookings_valid(
                getBookings());
    }

    @Test
    @Description("Getting the list of all the bookings' IDs and checking whether created booking is present within it")
    public void getBookingsWithSpecificIdTest() {
        BookingInfo newBooking = postAndGetInfo();

        getBookings_valid(
                getBookings())
                .body("bookingid", hasItem(newBooking.getBookingid()));
    }

    @Test
    @Description("Getting booking details by ID")
    public void getBookingTest() {
        BookingInfo newBooking = postAndGetInfo();

        getBooking_valid(
                getBooking(newBooking.getBookingid()));

        Assert.assertEquals(newBooking.getBooking().toString(), booking.toString());
    }

    @Test
    @Description("Updating booking details")
    public void updateBookingTest() {
        BookingInfo newBooking = postAndGetInfo();
        booking.setFirstname("Jane");

        Booking updatedBooking = getBooking_valid(
                updateBooking(newBooking.getBookingid(),
                        booking))
                .extract().body().as(Booking.class);

        Assert.assertEquals(updatedBooking.toString(), booking.toString());
    }

    private BookingInfo postAndGetInfo() {
        return createBooking_valid(
                createBooking(booking))
                .extract().body().as(BookingInfo.class);
    }

}

