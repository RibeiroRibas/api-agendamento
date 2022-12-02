package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Category;
import br.com.beautystyle.agendamento.model.entity.Expense;
import br.com.beautystyle.agendamento.util.RegisterScheduleUtil;
import br.com.beautystyle.agendamento.util.RegisterUserUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProfileRepository profileRepository;
    private Long tenant;
    private RegisterScheduleUtil registerScheduleUtil;

    @Before
    public void setup() {
     //   eventRepository.deleteAll();
        expenseRepository.deleteAll();
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository);
        registerScheduleUtil = new RegisterScheduleUtil();
        tenant = registerUserUtil.saveUserProfessional().getCompany().getId();
    }

    @Test
    public void shouldReturnLocalDateListByTenant() {
        Expense expense = initExpense(tenant);
        Expense expense2 = initExpense(tenant);
        expense2.setDate(LocalDate.now().plusYears(1));
        expenseRepository.save(expense);
        expenseRepository.save(expense2);
        List<LocalDate> yearsList = expenseRepository.getYearsListByTenant(tenant);
        Assert.assertEquals(2, yearsList.size());
    }

    @Test
    public void shouldReturnExpenseListByTenantAndPeriod() {
        Expense expense = initExpense(tenant);
        Expense expense2 = initExpense(tenant);
        expense2.setDate(registerScheduleUtil.getEventDateEqualsWednesday().plusDays(2));
        expenseRepository.save(expense);
        expenseRepository.save(expense2);
        List<Expense> expenses =
                expenseRepository.findByTenantEqualsAndDateGreaterThanEqualAndDateLessThanEqual(
                        tenant, registerScheduleUtil.getEventDateEqualsWednesday(),
                        registerScheduleUtil.getEventDateEqualsWednesday().plusDays(1));
        Assert.assertEquals(1, expenses.size());
    }

    @Test
    public void shouldReturnExpenseListByTenantAndExpenseDate() {
        Expense expense = initExpense(tenant);
        Expense expense2 = initExpense(tenant);
        expense2.setDate(registerScheduleUtil.getEventDateEqualsWednesday().plusDays(2));
        expenseRepository.save(expense);
        expenseRepository.save(expense2);
        List<Expense> expenses =
                expenseRepository.findByTenantAndDate(
                        tenant, registerScheduleUtil.getEventDateEqualsWednesday());
        Assert.assertEquals(1, expenses.size());
    }

    private Expense initExpense(Long tenant) {
        Expense expense = new Expense();
        expense.setDate(registerScheduleUtil.getEventDateEqualsWednesday());
        expense.setTenant(tenant);
        expense.setValue(new BigDecimal(50));
        expense.setCategory(new Category(saveCategory(tenant)));
        expense.setDescription("Test Description");
        expense.setRepeat(false);
        return expense;
    }

    private Long saveCategory(Long tenant) {
        Category category = new Category();
        category.setName("Test Name");
        category.setTenant(tenant);
        return categoryRepository.save(category).getId();
    }
}
