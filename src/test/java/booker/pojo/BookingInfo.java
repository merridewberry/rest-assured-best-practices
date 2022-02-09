package booker.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingInfo {

    private int bookingid;
    private Booking booking;

    public BookingInfo() {
    }

    @Override
    public String toString() {
        return "bookingid: " + this.bookingid + "; booking: " + this.booking;
    }

}
