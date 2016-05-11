package pl.spring.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pl.spring.demo.constants.ViewNames;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@Controller
@RequestMapping("/search")
public class SearchController {
	
	@Autowired
	BookService bookService;

	/**
	 * Method collects info about all books
	 */
	
	@RequestMapping(method = RequestMethod.GET)
	public String findBook(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("searchBook", new BookTo());
		model.addAttribute("searchBook", new BookTo());
		return ViewNames.SEARCH;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView findBook(@ModelAttribute("searchBook") BookTo searchBook) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("bookList",
				bookService.findBooksByTitleAndAuthor(searchBook.getTitle(), searchBook.getAuthors()));
		modelAndView.setViewName(ViewNames.FOUNDED);
		return modelAndView;	
	}
	
	@InitBinder
	public void initialiseBinder(WebDataBinder binder) {
		binder.setAllowedFields("id", "title", "authors", "status");
	}

}