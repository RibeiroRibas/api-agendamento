package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Category;
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

import java.util.ArrayList;
import java.util.List;

import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_TENANT;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;

    private Long tenant;

    @Before
    public void setup() {
        //    eventRepository.deleteAll();
        categoryRepository.deleteAll();
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository);
        tenant = registerUserUtil.saveUserProfessional().getCompany().getId();
    }

    @Test
    public void shouldReturnCategoriesListByTenant() {
        List<Category> categories = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            categories.add(new Category("Category " + i, tenant));
        }
        categoryRepository.saveAll(categories);
        List<Category> savedCategories = categoryRepository.findByTenant(tenant);
        Assert.assertNotNull(categories);
        Assert.assertEquals(categories.size(), savedCategories.size());
    }

    @Test
    public void givenInvalidTenantWhenFindByTenantTheReturnEmptyList() {
        List<Category> categories = categoryRepository.findByTenant(INVALID_TENANT);
        Assert.assertTrue(categories.isEmpty());
    }

}
