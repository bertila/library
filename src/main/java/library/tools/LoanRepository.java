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

@Stateless
public class LoanRepository {

    @Inject
    private EntityManager em;
    
    public Loan findById(Long id) {
        return em.find(Loan.class, id);
    }

    public Loan findByISBN(String isbn) {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Loan> criteria = cb.createQuery(Loan.class);
        Root<Loan> book = criteria.from(Loan.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.name), email));
        criteria.select(book).where(cb.equal(book.get("isbn"), isbn));
        
       if(em.createQuery(criteria).getResultList()!= null)
    	   return em.createQuery(criteria).getResultList().get(0);
       else {
        	return null;
        }
   	
    }
    public Loan findByTitle(String title) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Loan> criteria = cb.createQuery(Loan.class);
        Root<Loan> book = criteria.from(Loan.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.name), email));
        criteria.select(book).where(cb.equal(book.get("title"), title));
        return em.createQuery(criteria).getSingleResult();
    }
    
    public List<Loan> findAllLoanOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Loan> criteria = cb.createQuery(Loan.class);
        Root<Loan> loan = criteria.from(Loan.class);
        criteria.select(loan).orderBy(cb.asc(loan.get("id")));
        return em.createQuery(criteria).getResultList();
    }
   
    // TODO Find by Apache public Member findByTitle(String title) {
    // Class Metaphone
    // https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/language/Metaphone.html

    public List<Loan> findByLoanTitleByThatSpellsSimilarTo(String title) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Loan> criteria = cb.createQuery(Loan.class);
        Root<Loan> book = criteria.from(Loan.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.name), email));
        criteria.select(book).where(cb.like(book.get("title").as(String.class),"%" +title +"%"));
        return em.createQuery(criteria).getResultList();
    }

	public Loan findLoan(String isbn, String title) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unused")
	public Loan returnLoan(Loan loan) {
		
		System.out.println(loan.getStatus());
		if (loan.getStatus().equals("Active")) {
			
			loan.setReturndate(LocalDateTime.now());
			loan.setStatus("Returned");
			
			Book book = loan.getBook();
			book.setStatus("Available");
			
			loan.setBook(book);
			book.setLoan(loan);
			
			em.merge(book);
			em.merge(loan);
			
			if (book == null)
			{
				return null;
			}
			
			return loan;
		}
		else {
			return null;
		}
	}
}
