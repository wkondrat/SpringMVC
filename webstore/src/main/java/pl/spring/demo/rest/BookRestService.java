package pl.spring.demo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.spring.demo.enumerations.BookStatus;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@Controller
@ResponseBody
public class BookRestService {

	@Autowired
	BookService bookService;

	@RequestMapping(value = "/rest/books", method = RequestMethod.POST)
	public ResponseEntity<Void> createBook(@RequestBody BookTo book) {
		if (bookService.findBooksById(book.getId())!=null) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		bookService.saveBook(book);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/rest/books/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookTo> getBookById(@PathVariable("id") Long id) {
		BookTo book = bookService.findBooksById(id);
		if (book == null) {
			return new ResponseEntity<BookTo>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<BookTo>(book, HttpStatus.OK);
	}

	@RequestMapping(value = "/rest/books", method = RequestMethod.GET)
	public ResponseEntity<List<BookTo>> getBooks() {
		List<BookTo> books = bookService.findAllBooks();
		if (books.isEmpty()) {
			return new ResponseEntity<List<BookTo>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<BookTo>>(books, HttpStatus.OK);
	}

	@RequestMapping(value = "/rest/books", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookTo> updateBook(@RequestParam("id") Long id, @RequestParam("status") BookStatus status) {
		BookTo currentBook = bookService.findBooksById(id);
		if (currentBook == null) {
			return new ResponseEntity<BookTo>(HttpStatus.NOT_FOUND);
		}
		currentBook.setStatus(status);
		bookService.saveBook(currentBook);
		return new ResponseEntity<BookTo>(currentBook, HttpStatus.OK);
	}

	@RequestMapping(value = "/rest/books/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<BookTo> deleteBook(@PathVariable("id") Long id) {
		BookTo user = bookService.findBooksById(id);
		if (user == null) {
			return new ResponseEntity<BookTo>(HttpStatus.NOT_FOUND);
		}
		bookService.deleteBook(id);
		return new ResponseEntity<BookTo>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/rest/search", method = RequestMethod.GET)
	public ResponseEntity<List<BookTo>> retrieveBooks(@RequestParam("title") String title,
			@RequestParam("authors") String authors) {
		if (bookService.findBooksByTitleAndAuthor(title, authors).isEmpty()) {
			return new ResponseEntity<List<BookTo>>(HttpStatus.NOT_FOUND);
		}
		List<BookTo> book = bookService.findBooksByTitleAndAuthor(title, authors);
		return new ResponseEntity<List<BookTo>>(book, HttpStatus.OK);
	}
	// TODO: Inject properly book service

	// TODO: implement all necessary CRUD operations as a rest service

	// TODO: implement some search methods considering single request parameters
	// / multiple request parameters / array request parameters

}
