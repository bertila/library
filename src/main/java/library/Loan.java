package library;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Loan {
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;	
	
	LocalDateTime loandate;
	LocalDateTime returndate;
	String Status;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Book book;
	
	public String getStatus() {
		return Status;
	}


	public void setStatus(String status) {
		Status = status;
	}
	
	@OneToOne
	private Member member;
	
	@JsonIgnore
	public Book getBook() {
		return book;
	}


	public void setBook(Book book) {
		this.book = book;
	}


	public Member getMember() {
		return member;
	}


	public void setMember(Member member) {
		this.member = member;
	}


	public Loan() {
		
	}	
	

	public LocalDateTime getLoandate() {
		return loandate;
	}	


	public void setLoandate(LocalDateTime loandate) {
		this.loandate = loandate;
	}


	public LocalDateTime getReturndate() {
		return returndate;
	}


	public void setReturndate(LocalDateTime returndate) {
		this.returndate = returndate;
	}


	public LocalDateTime getLoanDate() {
		return loandate;
	}
	public void setLoanDate(LocalDateTime loandate) {
		this.loandate = loandate;
	}
	public LocalDateTime getReturnDate() {
		return returndate;
	}
	public void setReturnDate(LocalDateTime returndate) {
		this.returndate = returndate;
	}
	public Long getId() {
		return id;
	}
	
	
	// TODO int reLoan=3;

}
