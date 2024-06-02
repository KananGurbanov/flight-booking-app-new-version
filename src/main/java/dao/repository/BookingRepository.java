package dao.repository;

import dao.entity.BookingEntity;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    void save(BookingEntity booking);

    void delete(BookingEntity booking);

    List<BookingEntity> findAll();

    Optional<BookingEntity> findById(Long id);

    void update(Long id, BookingEntity updatedBooking);

    //boolean existById(BookingEntity booking);

}