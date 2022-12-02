package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.ExpenseDto;
import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.ExpenseForm;
import br.com.beautystyle.agendamento.model.entity.Category;
import br.com.beautystyle.agendamento.model.entity.Expense;
import br.com.beautystyle.agendamento.repository.CategoryRepository;
import br.com.beautystyle.agendamento.repository.ExpenseRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static br.com.beautystyle.agendamento.controller.ConstantsController.ENTITY_NOT_FOUND;
import static br.com.beautystyle.agendamento.controller.ConstantsController.TENANT_NOT_EQUALS;
import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_ID;
import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_TENANT;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ExpenseControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mock;
    @Autowired
    private ExpenseRepository expenseRepository;
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
        uri = "/expense/";
        expenseRepository.deleteAll();
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository, mapper, mock);
        mockMvc = new MockMvcUtil(mock, mapper);
        registerUserUtil.saveUserProfessional();
        TokenDto tokenDto = registerUserUtil.authUserProfessional();
        token = tokenDto.getToken();
        tenant = tokenDto.getTenant();
    }

    @Test
    public void shouldReturnExpenseListByPeriod() throws Exception {
        saveExpense(tenant);
        ExpenseForm expenseForm = initExpenseForm(tenant);
        expenseForm.setTenant(tenant);
        Expense expense2 = new Expense(expenseForm);
        expense2.setDate(LocalDate.now().plusDays(2));
        expenseRepository.save(expense2);
        String uri = this.uri + LocalDate.now() + "/" + LocalDate.now().plusDays(1);
        MvcResult mvcResult = mockMvc.GET(uri, token);
        String responseAsString = mvcResult.getResponse().getContentAsString();
        List<ExpenseDto> savedCategories = Arrays.asList(mapper.readValue(responseAsString, ExpenseDto[].class));
        Assert.assertEquals(1, savedCategories.size());
    }

    @Test
    public void shouldReturnYearsList() throws Exception {
        saveExpense(tenant);
        ExpenseForm expenseForm = initExpenseForm(tenant);
        expenseForm.setTenant(tenant);
        Expense expense2 = new Expense(expenseForm);
        expense2.setDate(LocalDate.now().plusYears(1));
        expenseRepository.save(expense2);
        MvcResult mvcResult = mockMvc.GET(uri, token);
        String responseAsString = mvcResult.getResponse().getContentAsString();
        List<String> yearsList = Arrays.asList(mapper.readValue(responseAsString, String[].class));
        Assert.assertEquals(2, yearsList.size());
    }

    @Test
    public void givenExpenseFormObjectWhenSavedThenReturnStatusCreatedAndExpenseDtoObject() throws Exception {
        ExpenseForm expenseForm = initExpenseForm(tenant);
        mockMvc.POST(uri, expenseForm, token)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tenant").value(tenant));
    }

    @Test
    public void givenExpenseFormObjectWhenUpdatedThenReturnStatusOkAndExpenseDtoObject() throws Exception {
        Expense savedExpense = saveExpense(tenant);
        ExpenseForm expenseForm = initExpenseForm(tenant);
        String uri = this.uri + savedExpense.getId();
        mockMvc.PUT(uri, expenseForm, token)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expenseForm.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.value").value(expenseForm.getValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(expenseForm.getCategoryId()));
    }

    @Test
    public void givenInvalidExpenseIdThenReturnStatusNotFound() throws Exception {
        ExpenseForm expenseForm = initExpenseForm(tenant);
        String uri = this.uri + INVALID_ID;
        mockMvc.PUT(uri, expenseForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ENTITY_NOT_FOUND));
    }

    @Test
    public void givenExpenseIdFromAnotherUserThenReturnStatusBadRequest() throws Exception {
        Expense savedExpense = saveExpense(INVALID_TENANT);
        ExpenseForm expenseForm = initExpenseForm(tenant);
        String uri = this.uri + savedExpense.getId();
        mockMvc.PUT(uri, expenseForm, token)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(TENANT_NOT_EQUALS));
    }

    @Test
    public void givenExpenseIdWhenDeletedThenReturnStatusOk() throws Exception {
        Expense savedExpense = saveExpense(tenant);
        String uri = this.uri + savedExpense.getId();
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenInvalidExpenseIdWhenTryDeleteThenReturnStatusNotFound() throws Exception {
        String uri = this.uri + INVALID_ID;
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ENTITY_NOT_FOUND));
    }

    @Test
    public void givenExpenseIdFromAnotherUserWhenTryDeleteThenReturnStatusBadRequest() throws Exception {
        Expense savedExpense = saveExpense(INVALID_TENANT);
        String uri = this.uri + savedExpense.getId();
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(TENANT_NOT_EQUALS));
    }

    private Expense saveExpense(Long tenant) {
        ExpenseForm expenseForm = initExpenseForm(tenant);
        expenseForm.setTenant(tenant);
        Expense expense = new Expense(expenseForm);
        return expenseRepository.save(expense);
    }

    private ExpenseForm initExpenseForm(Long tenant) {
        ExpenseForm expenseForm = new ExpenseForm();
        expenseForm.setExpenseDate(LocalDate.now());
        expenseForm.setValue(new BigDecimal(50));
        expenseForm.setCategoryId(saveCategory(tenant));
        expenseForm.setDescription("Test Description");
        expenseForm.setRepeatOrNot(false);
        return expenseForm;
    }

    private Long saveCategory(Long tenant) {
        Category category = new Category();
        category.setName("Test Name");
        category.setTenant(tenant);
        return categoryRepository.save(category).getId();
    }
}
