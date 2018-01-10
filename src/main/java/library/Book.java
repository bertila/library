package library;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Book {
	@javax.persistence.Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	Long id;
	
	String author;
	String genre;
	String isbn;
	int pages;
	String status;
	String title;
	@Column(length=700) 
	String info;
	String image;
	
	String position;
	String createdyear;
		
	@OneToOne
	private Loan loan;
	
	
	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	public Book() {
		super();
	}

	public String getGenre() {
		return genre;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @param author
	 * @param genre
	 * @param isbn
	 * @param pages
	 * @param status
	 * @param title
	 * @param info
	 * @param image
	 * @param position
	 * @param createdyear
	 */
	public Book(String author, String genre, String isbn, int pages, String status, String title, String info,
			String image, String position, String createdyear) {
		super();
		this.author = author;
		this.genre = genre;
		this.isbn = isbn;
		this.pages = pages;
		this.status = status;
		this.title = title;
		this.info = info;
		this.image = image;
		this.position = position;
		this.createdyear = createdyear;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	

	public String getCreatedyear() {
		return createdyear;
	}

	public void setCreatedyear(String createdyear) {
		this.createdyear = createdyear;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public Long getId() {
		return id;
	}
	

}
