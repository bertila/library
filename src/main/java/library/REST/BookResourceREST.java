package library.REST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import library.Book;
import library.tools.BookRegistration;
import library.tools.BookRepository;


/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/book")
@RequestScoped
public class BookResourceREST {

	@Inject
	private Logger log;

	@Inject
	private Validator validator;

	@Inject
	private BookRepository bookRepository;

	@Inject
	BookRegistration registration;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Book> listAllBooks() {
		// TODO LibraryRepository.this.
		return bookRepository.findAllBookOrderedByName();

	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Book lookupBookById(@PathParam("id") long id) {
		Book book = bookRepository.findById(id);
		
		Response.ResponseBuilder builder = null;
		if (book != null) {
			builder = Response.ok();
			return book;
		}
		else
		{
            throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
	
	@GET
	@Path("/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Book> lookupBookByTitleSpellsLike(@PathParam("title") String title) {
		List<Book> books = bookRepository.findByBookTitleByThatSpellsSimilarTo(title);
		if (books != null) {
			return books;
		}
		else 
		{
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
	
	@GET
	@Path("/isbn/{isbn}")
	@Produces(MediaType.APPLICATION_JSON)
	public Book lookupBookByIsbn(@PathParam("isbn") String isbn) {
		Book book = bookRepository.findBookbyIsbn(isbn);
		System.out.println(book);
		if (book!= null) {
			return book;
		}
		else
		{
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
	
	
	@POST
	@Path("/return/{bookid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnLoanPOST(@PathParam("bookid") Long id) {
		Book book = bookRepository.findById(id);
		
		Response.ResponseBuilder builder = null;
		Book bookLoan = bookRepository.returnBook(book);
		
		if (bookLoan != null) {
			builder = Response.ok();
		}
		else
		{
			Map<String, String> responseObj = new HashMap<>();
            responseObj.put("book", "Return failed");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		}
		return builder.build();
	}
	
	

	/**
	 * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
	 * or with a map of fields, and related errors.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createBook(Book book) {

		Response.ResponseBuilder builder = null;

		try {
			// Validates member using bean validation
			// TODO validateMember(member);

			registration.register(book);

			// Create an "ok" response
			builder = Response.ok();
		} catch (ConstraintViolationException ce) {
			// Handle bean validation issues
			builder = createViolationResponse(ce.getConstraintViolations());
		} catch (ValidationException e) {
			// Handle the unique constrain violation
			//            Map<String, String> responseObj = new HashMap<>();
			//            responseObj.put("email", "Email taken");
			//            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}

		return builder.build();
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Book updateStatusOnId(@PathParam("id") Long id) {
		// System.out.println(id);
		Book book = bookRepository.findAndDeleteBook(id);
		// System.out.println(book);
		// System.out.println(book.getStatus());
		
		if (book== null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return book;
	}

	/**
	 * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
	 * by clients to show violations.
	 * 
	 * @param violations A set of violations that needs to be reported
	 * @return JAX-RS response containing all violations
	 */
	
	private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
		log.fine("Validation completed. violations found: " + violations.size());

		Map<String, String> responseObj = new HashMap<>();

		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	}
}
