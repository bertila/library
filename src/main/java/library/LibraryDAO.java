package library;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Stateless
public class LibraryDAO  {

	@Inject
	EntityManager em;
	
	/* (non-Javadoc)
	 * @see lexicon.PersonDAOInterface#persistPerson(lexicon.Person)
	 */
	/* (non-Javadoc)
	 * @see library.LibraryDAOInterface#persistPerson(library.User)
	 */
	public void persistMember(Member member) {
		em.persist(member);
	}
	
	/* (non-Javadoc)
	 * @see library.LibraryDAOInterface#persistPerson(library.Book)
	 */
	public void persistBook(Book book) {
		em.persist(book);
	}
	
	/* (non-Javadoc)
	 * @see library.LibraryDAOInterface#persistLoan(library.Loan)
	 */
	public void persistLoan(Loan loan) {
		em.persist(loan);
	}
	
	
	/* (non-Javadoc)
	 * @see lexicon.PersonDAOInterface#getAllPerson()
	 */
	/* (non-Javadoc)
	 * @see library.LibraryDAOInterface#getAllUser()
	 */
	public List<Member> getAllUser() {
		TypedQuery<Member> query = em.createQuery("SELECT p FROM Member p", Member.class);
		return query.getResultList();
	}
	/* (non-Javadoc)
	 * @see lexicon.PersonDAOInterface#addNoteToPerson(java.lang.Long, java.lang.String)
	 */
	
	
	
	
	
	
//	@Override
//	public void addNoteToPerson(Long id, String text) {
//		Person person = em.find(Person.class, id);
//		person.getNotes().add(new Note(text));
//		em.merge(person);
//	}
	
	
}