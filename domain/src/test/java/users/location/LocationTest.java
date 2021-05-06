package users.location;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import users.location.event.AddressChangedEvent;
import users.location.event.LocationCreatedEvent;
import users.location.event.LocationEvent;
import users.location.vo.Address;
import users.location.vo.Country;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    private final UUID LOCATION_ID = UUID.randomUUID();

    private final String STREET = "Street";
    private final String CITY = "City";
    private final String REGION = "Region";
    private final Country US = new Country(Locale.US);
    private final Address ADDRESS = new Address(STREET, CITY, REGION, US);

    private final Country UK = new Country(Locale.UK);

    private final Class<LocationCreatedEvent> LOCATION_CREATED_EVENT = LocationCreatedEvent.class;
    private final Class<AddressChangedEvent> ADDRESS_CHANGED_EVENT = AddressChangedEvent.class;

    @Test
    @DisplayName("Location Should Create Properly And Generate LocationCreatedEvent")
    void locationShouldCreateProperlyAndGenerateEvent() {
        final Location location = Location.create(LOCATION_ID, ADDRESS);
        Optional<LocationEvent> locationEvent = location.findLatestEvent();

        assertTrue(locationEvent.isPresent());
        assertEquals(LOCATION_CREATED_EVENT, locationEvent.get().getClass());
    }

    @Test
    @DisplayName("Location Should Restore Properly And Not Generate Event")
    void locationShouldRestoreProperlyAndNotGenerateEvent() {
        final Location location = Location.restore(LOCATION_ID, ADDRESS);
        final Optional<LocationEvent> productStockEvent = location.findLatestEvent();

        assertFalse(productStockEvent.isPresent());
    }

    @Test
    @DisplayName("Location's Address Should Change And Generate Event")
    void locationAddressShouldChangeProperlyAndGenerateEvent() {
        final Location location = Location.restore(LOCATION_ID, ADDRESS);
        final Address anotherAddress = ADDRESS.changeAddress(ADDRESS.street(), ADDRESS.city(), ADDRESS.region(), UK);
        location.changeAddress(anotherAddress);

        final Optional<LocationEvent> locationEvent = location.findLatestEvent();
        assertTrue(locationEvent.isPresent());
        assertEquals(ADDRESS_CHANGED_EVENT, locationEvent.get().getClass());

        final AddressChangedEvent addressChangedEvent = (AddressChangedEvent) locationEvent.get();
        assertEquals(LOCATION_ID, addressChangedEvent.aggregateId());
        assertEquals(anotherAddress, addressChangedEvent.address());
    }

    @Test
    @DisplayName("Location's Address Should Not Change And Not Generate Event When Same Address")
    void locationAddressShouldNotChangeAndNotGenerateEvent() {
        final Location location = Location.restore(LOCATION_ID, ADDRESS);
        location.changeAddress(ADDRESS);

        final Optional<LocationEvent> locationEvent = location.findLatestEvent();
        assertFalse(locationEvent.isPresent());
    }
}
