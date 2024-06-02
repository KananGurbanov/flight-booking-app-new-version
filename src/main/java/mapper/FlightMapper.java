package mapper;


import dao.entity.FlightEntity;
import model.dto.FlightDto;

import java.util.ArrayList;
import java.util.List;

public class FlightMapper {

    public static FlightDto toDto(FlightEntity flightEntity) {
        return new FlightDto(flightEntity.getId(),
                flightEntity.getOrigin(),
                flightEntity.getDestination(),
                flightEntity.getDepartureTime(),
                flightEntity.getAvailableSeats());
    }

    public static FlightEntity toEntity(FlightDto flightDto) {
        return new FlightEntity(flightDto.getId(),
                flightDto.getOrigin(),
                flightDto.getDestination(),
                flightDto.getDepartureTime(),
                flightDto.getAvailableSeats());
    }

    public static List<FlightEntity> toEntityList(List<FlightDto> flightDtos) {
        List<FlightEntity> flightEntities = new ArrayList<>();
        for (FlightDto flightDto : flightDtos) {
            flightEntities.add(new FlightEntity(flightDto.getId(),
                    flightDto.getOrigin(),
                    flightDto.getDestination(),
                    flightDto.getDepartureTime(),
                    flightDto.getAvailableSeats()));
        }
        return flightEntities;
    }

    public static List<FlightDto> toDtoList(List<FlightEntity> flightEntities) {
        List<FlightDto> flightDtos = new ArrayList<>();
        for (FlightEntity flightEntity : flightEntities) {
            flightDtos.add(new FlightDto(flightEntity.getId(),
                    flightEntity.getOrigin(),
                    flightEntity.getDestination(),
                    flightEntity.getDepartureTime(),
                    flightEntity.getAvailableSeats()));
        }
        return flightDtos;
    }


}
