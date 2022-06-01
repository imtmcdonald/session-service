package edu.psu.sweng894.chewsy.session;

import java.util.UUID;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.psu.sweng894.chewsy.session.domain.Attendee;
import edu.psu.sweng894.chewsy.session.domain.repository.ConciergeRepository;
import edu.psu.sweng894.chewsy.session.domain.repository.SessionRepository;
import edu.psu.sweng894.chewsy.session.domain.service.DomainSessionService;
import edu.psu.sweng894.chewsy.session.domain.service.SessionService;
import edu.psu.sweng894.chewsy.session.infrastructure.repository.ConciergeAPI.ConciergeAPIRespository;
import edu.psu.sweng894.chewsy.session.infrastructure.repository.MockPostgreSQLDB.MockPostgreSQLDBRepository;

@SpringBootTest
public class DomainSessionServiceTests {
    
    @Test
    public void testCreateSession() {
        Attendee attendee = new Attendee("tam6190@psu.edu");
        SessionRepository sessionRepository = new MockPostgreSQLDBRepository();
        ConciergeRepository conciergeRepository = new ConciergeAPIRespository();
        SessionService session = new DomainSessionService(sessionRepository, conciergeRepository);
        UUID id = session.createSession(attendee);
        System.out.println(session.getStatus(id));
        session.addRestaurantList(id, "23666", 5);
        System.out.println(session.getRestaurantList(id));
        session.addAttendee(id, attendee);
        System.out.println(session.getAttendees(id));
        session.removeAttendee(id, "tam6190@psu.edu");
        System.out.println(session.getAttendees(id));
        session.completeSession(id);
        System.out.println(session.getStatus(id));
    }
}
