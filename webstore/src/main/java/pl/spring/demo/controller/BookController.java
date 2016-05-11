package pl.spring.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.spring.demo.constants.ViewNames;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

/**
 * Book controller
 * 
 * @author mmotowid
 *
 */
@Controller
@RequestMapping("/books")
public class BookController {
	
	@Autowired
	BookService bookService;
//	@RequestMapping
//	public String list(Model model) {
//		// TODO: implement default method
//		return ViewNames.BOOKS;
//	}

	/**
	 * Method collects info about all books
	 */
	@RequestMapping//("/all")
	public ModelAndView allBooks(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("bookList",bookService.findAllBooks());
		modelAndView.setViewName(ViewNames.BOOKS2);
		// TODO: implement method gathering and displaying all books
		return modelAndView;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView findBook(@ModelAttribute("searchBook") BookTo searchBook) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("bookList",
				bookService.findBooksByTitleAndAuthor(searchBook.getTitle(), searchBook.getAuthors()));
		modelAndView.setViewName(ViewNames.FOUNDED);
		return modelAndView;	
	}
	
	@RequestMapping("/book")
	public ModelAndView detail(@RequestParam("id") Long id) {
		ModelAndView modelAndView = new ModelAndView();	
		modelAndView.addObject("book",bookService.findBooksById(id));
		modelAndView.setViewName(ViewNames.BOOK);
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addNewBook(Model model) {
		model.addAttribute("newBook", new BookTo());
		return ViewNames.ADD_BOOK;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addBook(@ModelAttribute("newBook") BookTo newBook) {
		ModelAndView modelAndView = new ModelAndView();
		if (newBook.getTitle() != null && newBook.getAuthors() != null) {
			modelAndView.addObject("newBook", bookService.saveBook(newBook));
			modelAndView.addObject("bookList",bookService.findAllBooks());
			modelAndView.setViewName(ViewNames.BOOKS2);
			return modelAndView;
		}
		modelAndView.setViewName(ViewNames.ADD_BOOK);
		return modelAndView;
	}	
	
	@RequestMapping("/remove")
	public String remove(@RequestParam("id") Long id) {
		bookService.deleteBook(id);
		return ViewNames.REMOVE;
	}
	
//	@RequestMapping(value = "/search", method = RequestMethod.GET)
//	public String findBook(Model model) {
//		model.addAttribute("searchBook", new BookTo());
//		return ViewNames.FOUNDED;
//	}
	
	
	
	// TODO: here implement methods which displays book info based on query
	// arguments

	// TODO: Implement GET / POST methods for "add book" functionality

	/**
	 * Binder initialization
	 */
	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields("id", "title", "authors", "status");
	}

}