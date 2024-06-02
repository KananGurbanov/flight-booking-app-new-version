package mapper;

import dao.entity.BookingEntity;
import model.dto.BookingDto;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    public static BookingDto toDto(BookingEntity bookingEntity){
        return new BookingDto(
                bookingEntity.getId(),
                bookingEntity.getFlightId(),
                bookingEntity.getPassengers());
    }

    public static BookingEntity toEntity(BookingDto bookingDto){
        return new BookingEntity(
                bookingDto.getId(),
                bookingDto.getFlightId(),
                bookingDto.getPassengers());
    }

    public static List<BookingEntity> toEntityList(List<BookingDto> bookingDtos) {
        List<BookingEntity> bookingEntities = new ArrayList<>();
        for (BookingDto bookingDto : bookingDtos) {
            bookingEntities.add(new BookingEntity(
                    bookingDto.getId(),
                    bookingDto.getFlightId(),
                    bookingDto.getPassengers()));
        }
        return bookingEntities;
    }

    public static List<BookingDto> toDtoList(List<BookingEntity> bookingEntities) {
        List<BookingDto> bookingDtos = new ArrayList<>();
        for (BookingEntity bookingEntity : bookingEntities) {
            bookingDtos.add(new BookingDto(
                    bookingEntity.getId(),
                    bookingEntity.getFlightId(),
                    bookingEntity.getPassengers()));
        }
        return bookingDtos;
    }


}
