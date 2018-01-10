package library.REST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.tools.examples.data.MemberRepository;

import library.Book;
import library.Loan;
import library.Member;
import library.tools.BookRegistration;
import library.tools.BookRepository;
import library.tools.LoanRepository;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/loan")
@RequestScoped
public class LoanResourceREST {

	@Inject
	private Logger log;

	@Inject
	private Validator validator;

	@Inject
	private LoanRepository loanRepository;

	@Inject
	private BookRepository bookRepository;
	
	@Inject
	private MemberRepository memberRepository;


	@Inject
	BookRegistration registration;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Loan lookupLoanById(@PathParam("id") Long id) {
		Loan loan = loanRepository.findById(id);
		if (loan == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return loan;
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Loan> listAllLoans() {
		// TODO LibraryRepository.this.
		return loanRepository.findAllLoanOrderedByName();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createLoanPOST(@Context UriInfo info) {

		Long memberid = Long.parseLong(info.getQueryParameters().getFirst("memberid"));
		Long bookid = Long.parseLong(info.getQueryParameters().getFirst("bookid"));

		Book book = bookRepository.findById(bookid);
		Member member=memberRepository.findById(memberid);
		
		Response.ResponseBuilder builder = null;

		Book bookLoan = bookRepository.borrowBook(member,book );
		if (bookLoan != null) {
			builder = Response.ok();
		}
		else
		{
			Map<String, String> responseObj = new HashMap<>();
            responseObj.put("book", "Book on loan");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		}
		return builder.build();
	}
	
	@POST
	@Path("/return/{loanid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnLoanPOST(@PathParam("loanid") Long id) {
		
		Loan loan = loanRepository.findById(id);
		System.out.println(loan);
		loan = loanRepository.returnLoan(loan);
		
		Response.ResponseBuilder builder = null;
		
		if (loan != null) {
			builder = Response.ok();
		}
		else
		{
			Map<String, String> responseObj = new HashMap<>();
            responseObj.put("loan", "Return failed");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		}
		return builder.build();
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