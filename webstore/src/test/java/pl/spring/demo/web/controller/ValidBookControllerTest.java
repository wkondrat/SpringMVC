package pl.spring.demo.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.spring.demo.constants.ModelConstants;
import pl.spring.demo.constants.ViewNames;
import pl.spring.demo.controller.BookController;
import pl.spring.demo.enumerations.BookStatus;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "controller-test-configuration.xml")
@WebAppConfiguration
public class ValidBookControllerTest {

	@Autowired
	private BookService bookService;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		Mockito.reset(bookService);

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		BookController bookController = new BookController();
		mockMvc = MockMvcBuilders.standaloneSetup(bookController).setViewResolvers(viewResolver).build();
		// Due to fact, that We are trying to construct real Bean - Book
		// Controller, we have to use reflection to mock existing field book
		// service
		ReflectionTestUtils.setField(bookController, "bookService", bookService);
	}

	@Test
	public void testAddBookPage() throws Exception {
		// given
		BookTo testBook = new BookTo(1L, "Test title", "Test Author", BookStatus.FREE);
		Mockito.when(bookService.saveBook(Mockito.any())).thenReturn(testBook);

		// TODO: please take a look how we pass @ModelAttribute as a request
		// attribute
		ResultActions resultActions = mockMvc.perform(post("/books/add").flashAttr("newBook", testBook));
		// then
		resultActions.andExpect(view().name("books2"))
				.andExpect(model().attribute("newBook", new ArgumentMatcher<Object>() {
					@Override
					public boolean matches(Object argument) {
						BookTo book = (BookTo) argument;
						return null != book && testBook.getTitle().equals(book.getTitle());
					}
				}));

	}
	
	@Test
    public void testShouldFindAllBooks() throws Exception {
          // given
          BookTo testBook = new BookTo(1L, "Test title", "Test Author", BookStatus.FREE);
           Mockito.when(bookService.findAllBooks()).thenReturn(Arrays.asList(testBook));
          // when
          ResultActions resultActions = mockMvc.perform(get("/books"));
          // then
          resultActions.andExpect(view().name(ViewNames.BOOKS2))
                        .andExpect(model().attribute(ModelConstants.BOOK_LIST, new ArgumentMatcher<Object>() {
                              @Override
                              public boolean matches(Object argument) {
                                     List<BookTo> books = (List<BookTo>) argument;
                                     return books.isEmpty() != true && testBook.getTitle().equals(books.get(0).getTitle());
                              }
                        }));

    }
	
	@Test
	public void testFindBookById() throws Exception {
		// given
		BookTo testBook = new BookTo(1L, "Test title", "Test Author", BookStatus.FREE);
		// when
		Mockito.when(bookService.findBooksById(Mockito.any(Long.class))).thenReturn(testBook);
		ResultActions resultActions = mockMvc.perform(get("/books/book?id=1"));
		// then
		resultActions.andExpect(view().name(ViewNames.BOOK))
		.andExpect(model().attribute("book" ,new ArgumentMatcher<Object>() {
			@Override
			public boolean matches(Object argument) {
				BookTo book = (BookTo) argument;
				return null != book && testBook.getId().equals(book.getId());
			}
		}));

	}
	

	/**
	 * Sample method which convert's any object from Java to String
	 */
	private static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
