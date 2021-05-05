package users.location;

import domain.AggregateRoot;
import users.location.event.AddressChangedEvent;
import users.location.event.LocationCreatedEvent;
import users.location.event.LocationEvent;
import users.location.vo.Address;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Location extends AggregateRoot<UUID, LocationEvent> {

    private final UUID id;
    private Address address;

    static Location create(UUID id, Address address) {
        final Location location = new Location(id, address, new ArrayList<>());
        location.registerEvent(new LocationCreatedEvent(id, address));
        return location;
    }

    static Location restore(UUID id, Address address) {
        return new Location(id, address, new ArrayList<>());
    }

    private Location(UUID id, Address address, List<LocationEvent> events) {
        super(events);
        this.id = id;
        this.address = address;
    }

    void changeAddress(Address candidate) {
        if(!sameAddress(candidate)) {
            this.address = candidate;
            this.registerEvent(new AddressChangedEvent(id, address));
        }
    }

    private boolean sameAddress(Address candidate) {
        return address.equals(candidate);
    }
}
