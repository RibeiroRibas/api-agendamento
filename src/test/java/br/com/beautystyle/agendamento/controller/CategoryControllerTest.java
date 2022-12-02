package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.CategoryForm;
import br.com.beautystyle.agendamento.model.entity.Category;
import br.com.beautystyle.agendamento.repository.CategoryRepository;
import br.com.beautystyle.agendamento.repository.ProfileRepository;
import br.com.beautystyle.agendamento.repository.UserRepository;
import br.com.beautystyle.agendamento.util.MockMvcUtil;
import br.com.beautystyle.agendamento.util.RegisterUserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static br.com.beautystyle.agendamento.controller.ConstantsController.CATEGORY_NOT_FOUND;
import static br.com.beautystyle.agendamento.controller.ConstantsController.TENANT_NOT_EQUALS;
import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_ID;
import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_TENANT;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CategoryControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mock;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;

    private String token;
    private Long tenant;
    private String uri;
    private MockMvcUtil mockMvc;

    @Before
    public void setup() throws Exception {
        uri = "/category/";
       // eventRepository.deleteAll();
        categoryRepository.deleteAll();
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository, mapper, mock);
        mockMvc = new MockMvcUtil(mock, mapper);
        registerUserUtil.saveUserProfessional();
        TokenDto tokenDto = registerUserUtil.authUserProfessional();
        token = tokenDto.getToken();
        tenant = tokenDto.getTenant();
    }

    @Test
    public void shouldReturnStatusOkCategoriesListObject() throws Exception {
        List<Category> categories = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            if (i == 4)
                categories.add(new Category("Category " + i, INVALID_TENANT));
            categories.add(new Category("Category " + i, tenant));
        }
        categoryRepository.saveAll(categories);
        MvcResult mvcResult = mockMvc.GET(uri, token);
        String responseAsString = mvcResult.getResponse().getContentAsString();
        List<Category> savedCategories = Arrays.asList(mapper.readValue(responseAsString, Category[].class));
        Assert.assertEquals(categories.size()-1, savedCategories.size());
    }

    @Test
    public void givenCategoryObjectWhenCreateCategoryThenReturnStatusCreatedAndCategoryObject() throws Exception {
        CategoryForm categoryForm = new CategoryForm("Test Category");
        mockMvc.POST(uri, categoryForm, token)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(categoryForm.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tenant").value(tenant));
    }

    @Test
    public void givenCategoryIdAndCategoryObjectWhenUpdateCategoryThenReturnStatusOkAndCategoryObject() throws Exception {
        Category category = new Category("Category To Update", tenant);
        Category savedCategory = categoryRepository.save(category);
        CategoryForm categoryForm = new CategoryForm("Updated Category");
        String uri = this.uri + savedCategory.getId();
        mockMvc.PUT(uri, categoryForm, token)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(categoryForm.getName()));
    }

    @Test
    public void givenInvalidIdWhenTryUpdateThenReturnStatusNotFound() throws Exception {
        String uri = this.uri + INVALID_ID;
        CategoryForm categoryForm = new CategoryForm("Updated Category");
        mockMvc.PUT(uri, categoryForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CATEGORY_NOT_FOUND));
    }

    @Test
    public void givenCategoryIdFromAnotherUserWhenTryUpdateThenReturnStatusBadRequest() throws Exception {
        Category category = new Category("Category To Update", INVALID_TENANT);
        Category savedCategory = categoryRepository.save(category);
        CategoryForm categoryForm = new CategoryForm("Updated Category");
        String uri = this.uri + savedCategory.getId();
        mockMvc.PUT(uri, categoryForm, token)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(TENANT_NOT_EQUALS));
    }

    @Test
    public void givenCategoryIdWhenDeletedThenReturnStatusOk() throws Exception {
        Category category = new Category("Category To Delete", tenant);
        Category savedCategory = categoryRepository.save(category);
        String uri = this.uri + savedCategory.getId();
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenInvalidIdWhenTryDeleteCategoryThenReturnStatusNotFound() throws Exception {
        String uri = this.uri + INVALID_ID;
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(CATEGORY_NOT_FOUND));
    }

    @Test
    public void givenCategoryIdFromAnotherUserThenReturnStatusBadRequest() throws Exception {
        Category category = new Category("Category To Delete", INVALID_TENANT);
        Category savedCategory = categoryRepository.save(category);
        String uri = this.uri + savedCategory.getId();
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(TENANT_NOT_EQUALS));
    }

}
