package booker.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Booking {

    private String firstname;
    private String lastname;
    private Integer totalprice;
    private Boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;

    public Booking() {
    }

    public Booking(String firstname, String lastname, Integer totalprice, Boolean depositpaid, BookingDates bookingdates, String additionalneeds) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = bookingdates;
        this.additionalneeds = additionalneeds;
    }

    @Override
    public String toString() {
        return "firstname: " + this.firstname + "; lastname: " + this.lastname + "; totalprice: " + this.totalprice + "; depositpaid: " + this.depositpaid + "; bookingdates: " + this.bookingdates + "; additionalneeds: " + this.additionalneeds;
    }
}

