package library.REST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
		String msg ="";
		boolean error = false;

		Long memberid = Long.parseLong(info.getQueryParameters().getFirst("memberid"));
		Long bookid = Long.parseLong(info.getQueryParameters().getFirst("bookid"));

		Response.ResponseBuilder builder = null;
		Map<String, String> responseObj = new HashMap<>();
		
		Book book = bookRepository.findById(bookid);
		if (book == null) {
			msg = "Book could not be found!";
			error =true;
		}
		
		Member member=memberRepository.findById(memberid);
		if (member == null) {
			if (msg !="")
				msg =msg +", ";
			
			msg = msg + "Member could not be found!";
			error =true;
		}
		
		Book bookLoan = null;
		
		if (error ==false)
			bookLoan = bookRepository.borrowBook(member,book );
		
		if (bookLoan != null) {
			builder = Response.ok();
		}
		else
		{
            responseObj.put("Loan", msg);
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
		if (loan !=null)
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
	
}
