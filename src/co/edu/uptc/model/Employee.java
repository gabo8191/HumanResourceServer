package co.edu.uptc.model;

import co.edu.uptc.structures.MyQueue;
import co.edu.uptc.structures.MyStack;

public class Employee {
	private String name;
	private String lastName;
	private long id;
	private String gender;
	private String email;
	private long phone;
	private String city;
	private int yearBirth;
	private int monthBirth;
	private int dayBirth;
	private JobEmployee jobEmployee;
	private VacationsEmployee vacationsEmployee;
	private SalaryEmployee salaryEmployee;
	private MyStack<Notification> notificationsEmployee;
	private MyQueue<Request> requestsEmployee;

	public Employee() {
		this.jobEmployee = new JobEmployee();
		this.vacationsEmployee = new VacationsEmployee();
		this.salaryEmployee = new SalaryEmployee();
		this.notificationsEmployee = new MyStack<>();
		this.requestsEmployee = new MyQueue<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getYearBirth() {
		return yearBirth;
	}

	public void setYearBirth(int yearBirth) {
		this.yearBirth = yearBirth;
	}

	public int getMonthBirth() {
		return monthBirth;
	}

	public void setMonthBirth(int monthBirth) {
		this.monthBirth = monthBirth;
	}

	public int getDayBirth() {
		return dayBirth;
	}

	public void setDayBirth(int dayBirth) {
		this.dayBirth = dayBirth;
	}

	public JobEmployee getJobEmployee() {
		return jobEmployee;
	}

	public void setJobEmployee(JobEmployee jobEmployee) {
		this.jobEmployee = jobEmployee;
	}

	public VacationsEmployee getVacationsEmployee() {
		return vacationsEmployee;
	}

	public void setVacationsEmployee(VacationsEmployee vacationsEmployee) {
		this.vacationsEmployee = vacationsEmployee;
	}

	public SalaryEmployee getSalaryEmployee() {
		return salaryEmployee;
	}

	public void setSalaryEmployee(SalaryEmployee salaryEmployee) {
		this.salaryEmployee = salaryEmployee;
	}

	public MyStack<Notification> getNotificationsEmployee() {
		return notificationsEmployee;
	}

	public void setNotificationsEmployee(MyStack<Notification> notificationsEmployee) {
		this.notificationsEmployee = notificationsEmployee;
	}

	public MyQueue<Request> getRequestsEmployee() {
		return requestsEmployee;
	}

	public void setRequestsEmployee(MyQueue<Request> requestsEmployee) {
		this.requestsEmployee = requestsEmployee;
	}

	public void addRequest(Request request) {
		requestsEmployee.push(request);
	}

	public void addNotification(Notification notification) {
		notificationsEmployee.push(notification);
	}

}
