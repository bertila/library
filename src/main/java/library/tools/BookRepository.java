package library.tools;

import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import library.Book;
import library.Loan;
import library.Member;

@Stateless
public class BookRepository {

	@Inject
	private EntityManager em;
	
//	@Inject
//	private Loan loan;
	
	public Book findById(Long id) {
		return em.find(Book.class, id);
	}
	
	public Book findAndDeleteBook(Long id) {
		Book rBook = findById(id);
		rBook.setStatus("Deleted");
		em.merge(rBook);
		return rBook;
	}
	
	public Book findLendableBookById(String id) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
		Root<Book> book = criteria.from(Book.class);


		criteria.select(book).where(cb.equal(book.get("id"), id)).getSelection();
		Book result = em.createQuery(criteria).getSingleResult();


		if (result.getStatus().equals("Available")) {
			// result.setStatus("Loan");
			return result;
		}
		else
		{
			return null;
		}
	}

	public Book findByISBN(String isbn) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
		Root<Book> book = criteria.from(Book.class);
		criteria.select(book).where(cb.equal(book.get("isbn"), isbn));

		if(em.createQuery(criteria).getResultList()!= null)
			return em.createQuery(criteria).getResultList().get(0);
		else {
			return null;
		}

	}
	public Book findByTitle(String title) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
		Root<Book> book = criteria.from(Book.class);
		criteria.select(book).where(cb.equal(book.get("title"), title));
		return em.createQuery(criteria).getSingleResult();
	}

	public List<Book> findAllBookOrderedByName() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
		Root<Book> book = criteria.from(Book.class);
		// criteria.select(book).orderBy(cb.asc(book.get("title")));
		criteria.select(book).where(cb.like(book.get("title").as(String.class),"%%"));
		return em.createQuery(criteria).getResultList();
	}
	
	public List<Book> findByBookTitleByThatSpellsSimilarTo(String title) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
		Root<Book> book = criteria.from(Book.class);
		criteria.select(book).where(cb.like(book.get("title").as(String.class),"%" +title +"%"));
		return em.createQuery(criteria).getResultList();
	}

	public Book findBookbyIsbn(String isbn) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
		Root<Book> book = criteria.from(Book.class);
		criteria.select(book).where(cb.equal(book.get("isbn"),isbn));
		
		Book returnBook =null;
		try {
			returnBook= em.createQuery(criteria).getSingleResult();
		}
		catch(Exception e)
		{
			return null;
		}
		return returnBook; 
	}

	public Book borrowBook(Member member, Book book) {
		if (book.getStatus().equals("Available")) {
			Loan loan = new Loan();
			em.persist(loan);
			
			loan.setLoanDate(LocalDateTime.now());
			loan.setReturnDate(LocalDateTime.now().plusMonths(2));
			loan.setMember(member);
			loan.setStatus("Active");
			book.setStatus("OnLoan");
			book.setLoan(loan);
			loan.setBook(book);
			// book.setLoan(loan);
			em.merge(book);
			em.merge(loan);
			
			// book.setLoan(loan);
			// em.merge(book);
			
			return book;
		}
		else {
			return null;
		}
	}
	
	public Book returnBook(Book book) {
		System.out.println(book.getStatus());
		if (book.getStatus().equals("OnLoan")) {
			Loan loan = book.getLoan();
			loan.setReturndate(LocalDateTime.now());
			loan.setStatus("Returned");
			book.setStatus("Available");
			em.merge(loan);
			em.merge(book);
			
			return book;
		}
		else {
			return null;
		}
	}



}
