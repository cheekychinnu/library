package com.foo.library;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.foo.library.service.LibraryService;

@RunWith(SpringRunner.class)
@WebMvcTest
public class BaseMvcTest {
	
	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected LibraryService libraryService;

}
