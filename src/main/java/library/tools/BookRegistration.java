package library.tools;

import library.Book;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Logger;

@Stateless
public class BookRegistration {
	 @Inject
	    private Logger log;

	    @Inject
	    private EntityManager em;

	    @Inject
	    private Event<Book> bookEventSrc;

	    public void register(Book book) throws Exception {
	        log.info("Registering " + book.getTitle());
	        book.setStatus("Available");
	        em.persist(book);
	        bookEventSrc.fire(book);
	    }
}
