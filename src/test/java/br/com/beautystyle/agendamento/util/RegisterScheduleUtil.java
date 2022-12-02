package br.com.beautystyle.agendamento.util;

import br.com.beautystyle.agendamento.controller.form.*;
import br.com.beautystyle.agendamento.model.entity.*;
import br.com.beautystyle.agendamento.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_TENANT;

@Service
public class RegisterScheduleUtil {

    private BlockTimeOnDayRepository blockTimeOnDayRepository;
    private CustomerRepository customerRepository;
    private JobRepository jobRepository;
    private ScheduleRepository scheduleRepository;
    private ExpenseRepository expenseRepository;
    private CategoryRepository categoryRepository;

    public RegisterScheduleUtil() {

    }

    public RegisterScheduleUtil(ScheduleRepository scheduleRepository,
                                JobRepository jobRepository,
                                CustomerRepository customerRepository) {
        this.scheduleRepository = scheduleRepository;
        this.jobRepository = jobRepository;
        this.customerRepository = customerRepository;
        this.scheduleRepository.deleteAll();
        this.customerRepository.deleteAll();
        this.jobRepository.deleteAll();
    }

    public RegisterScheduleUtil(ScheduleRepository scheduleRepository,
                                JobRepository jobRepository,
                                CustomerRepository customerRepository,
                                BlockTimeOnDayRepository blockTimeOnDayRepository) {
        this.scheduleRepository = scheduleRepository;
        this.jobRepository = jobRepository;
        this.customerRepository = customerRepository;
        this.blockTimeOnDayRepository = blockTimeOnDayRepository;
        this.scheduleRepository.deleteAll();
        this.customerRepository.deleteAll();
        this.jobRepository.deleteAll();
        this.blockTimeOnDayRepository.deleteAll();
    }

    public RegisterScheduleUtil(ScheduleRepository scheduleRepository,
                                JobRepository jobRepository,
                                CustomerRepository customerRepository,
                                ExpenseRepository expenseRepository) {
        this.scheduleRepository = scheduleRepository;
        this.jobRepository = jobRepository;
        this.customerRepository = customerRepository;
        this.expenseRepository = expenseRepository;
        this.scheduleRepository.deleteAll();
        this.customerRepository.deleteAll();
        this.jobRepository.deleteAll();
        this.expenseRepository.deleteAll();
    }

    public LocalDate getEventDateEqualsWednesday() {
        LocalDate eventDate = LocalDate.now();
        while (eventDate.getDayOfWeek().getValue() != 3)
            eventDate = eventDate.plusDays(1);
        return eventDate.plusDays(7);
    }

    public Customer initCostumerTest(Long tenant) {
        Customer customer = new Customer();
        customer.setPhone("Test Phone");
        customer.setName("Test Name");
        customer.setTenant(tenant);
        return customer;
    }

    public Set<Job> saveJobTest(Long tenant) {
        Set<Job> jobs = new HashSet<>();
        Job job = initJobTest(tenant);
        Job savedJob = jobRepository.save(job);
        jobs.add(savedJob);
        return jobs;
    }

    public Job initJobTest(Long tenant) {
        Job job = new Job();
        job.setTenant(tenant);
        job.setPrice(new BigDecimal(40));
        job.setName("Test Name");
        job.setDurationTime(LocalTime.of(1, 0));
        return job;
    }

    public Customer saveCostumerTest(Long tenant) {
        Customer customer = initCostumerTest(tenant);
        return customerRepository.save(customer);
    }

    public Schedule saveScheduleWithUserCostumer(UserCustomer userCustomer, Long tenant) {
        Schedule schedule = new Schedule();
        schedule.setTenant(tenant);
        schedule.setPrice(new BigDecimal(10));
        schedule.setDate(getEventDateEqualsWednesday());
        schedule.setHasPaymentReceived(true);
        schedule.setJobs(saveJobTest(tenant));
        schedule.setStartTime(LocalTime.of(11, 0));
        schedule.setEndTime(LocalTime.of(12, 0));
        schedule.setCustomer(userCustomer.getCustomer());
        return scheduleRepository.save(schedule);
    }

    public EventAvailableTimesForm initEventAvailableTimesForm(Long tenant) {
        Set<Job> jobs = saveJobTest(tenant);
        EventAvailableTimesForm eventForm = new EventAvailableTimesForm();
        eventForm.setEventDate(getEventDateEqualsWednesday());
        eventForm.setJobIds(jobs.stream().map(Job::getId).collect(Collectors.toSet()));
        return eventForm;
    }

    public ScheduleByCostumerForm initEventByCostumerForm(Long tenant) {
        Set<Job> jobs = saveJobTest(tenant);
        ScheduleByCostumerForm eventForm = new ScheduleByCostumerForm();
        eventForm.setEventDate(getEventDateEqualsWednesday());
        eventForm.setStartTime(LocalTime.of(6, 0));
        eventForm.setJobIds(jobs.stream().map(Job::getId).collect(Collectors.toSet()));
        return eventForm;
    }



    public ScheduleByProfessionalForm initEventByProfessionalFormTest(Long tenant) {
        ScheduleByProfessionalForm scheduleForm = new ScheduleByProfessionalForm();
        scheduleForm.setEventDate(getEventDateEqualsWednesday());
        scheduleForm.setStartTime(LocalTime.of(13, 0));
        scheduleForm.setValue(new BigDecimal(40));
        scheduleForm.setHasPaymentReceived(false);
        Customer savedCustomer = saveCostumerTest(tenant);
        scheduleForm.setCustomerId(savedCustomer.getId());
        Set<Job> jobs = saveJobTest(tenant);
        scheduleForm.setJobIds(jobs.stream().map(Job::getId).collect(Collectors.toList()));
        return scheduleForm;
    }

    public JobForm initJobForm() {
        JobForm jobForm = new JobForm();
        jobForm.setPrice(new BigDecimal(50));
        jobForm.setName("Test Name 2");
        jobForm.setDurationTime(LocalTime.of(1, 30));
        return jobForm;
    }

    public void saveExpenses(int i, Long tenant) {
        Expense expense = initExpense(tenant);
        expense.setValue(new BigDecimal(50 * i));
        expense.setDate(getEventDateEqualsWednesday().plusDays(i));
        expenseRepository.save(expense);
    }

    public Expense initExpense(Long tenant) {
        Expense expense = new Expense();
        expense.setCategory(new Category(saveCategory(tenant)));
        expense.setDescription("Test Description");
        expense.setRepeat(false);
        expense.setTenant(tenant);
        return expense;
    }

    private Long saveCategory(Long tenant) {
        Category category = new Category();
        category.setName("Test Name");
        category.setTenant(tenant);
        return categoryRepository.save(category).getId();
    }

    public List<Schedule> saveSchedules(Long tenant) {
        List<Schedule> schedules = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Schedule schedule = initSchedule(tenant);
            schedule.setStartTime(LocalTime.of(6 + i, 0));
            schedule.setEndTime(LocalTime.of(7 + i, 0));
            if (i == 3)
                schedule.setDate(getEventDateEqualsWednesday().plusDays(2));
            if (i == 4)
                schedule.setTenant(INVALID_TENANT);
            schedules.add(schedule);
        }
        return scheduleRepository.saveAll(schedules);
    }

    public Schedule initSchedule(Long tenant) {
        Schedule schedule = new Schedule();
        schedule.setTenant(tenant);
        schedule.setPrice(new BigDecimal(10));
        schedule.setDate(getEventDateEqualsWednesday());
        schedule.setHasPaymentReceived(true);
        schedule.setJobs(saveJobTest(tenant));
        Customer customer = initCostumerTest(tenant);
        Customer savedCustomer = customerRepository.save(customer);
        schedule.setCustomer(savedCustomer);
        return schedule;
    }
}
