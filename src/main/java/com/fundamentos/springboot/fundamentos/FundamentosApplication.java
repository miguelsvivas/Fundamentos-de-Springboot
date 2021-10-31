package com.fundamentos.springboot.fundamentos;

import com.fundamentos.springboot.fundamentos.bean.MyBean;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentos.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentos.springboot.fundamentos.component.ComponentDependency;
import com.fundamentos.springboot.fundamentos.entity.User;
import com.fundamentos.springboot.fundamentos.pojo.UserPojo;
import com.fundamentos.springboot.fundamentos.repository.UserRepository;
import com.fundamentos.springboot.fundamentos.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	//inyectar dependencia
	private ComponentDependency componentDependency;
	private MyBean myBean;
	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;
	private UserService userService;




	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency,MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository,UserService userService){
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
       // ejemplosAnteriores();
		saveUsersInDataBase();
        getInformationJpqlFromUser();
		saveWithErrorTransactional();

	}

	private void saveWithErrorTransactional(){
		User test1 = new User("TestTransactional1", "TestTransactional1@domain.com", LocalDate.now());
		User test2 = new User("TestTransactional2", "TestTransactional2@domain.com", LocalDate.now());
		User test3 = new User("TestTransactional3", "TestTransactional3@domain.com", LocalDate.now());
		User test4 = new User("TestTransactional4", "TestTransactional4@domain.com", LocalDate.now());

		List<User> users = Arrays.asList(test1, test2, test3 , test4);

		try {
			userService.saveTransactional(users);
		}catch(Exception e) {
			LOGGER.info("Esta es una exception dentro del metodo transaccional"+ e);
		}
		userService.getAllUsers().stream()
				.forEach(user -> LOGGER.info("Este es el usuario dentro del metodo transacctional" + user));
		}

    private void getInformationJpqlFromUser(){
        LOGGER.info("Usuario con el metodo findbyusermeail " +
                userRepository.findByUserEmail("santi@gmail.com").orElseThrow(() -> new RuntimeException("no se encontro el usuario")));

		userRepository.findAndSort("miguel", Sort.by("id").descending()).stream().forEach(user -> LOGGER.info("Usuario con metodo sort" +user));

        userRepository.findByName("Miguel").stream().forEach(user -> LOGGER.info("Usuario con query method" + user));

		//LOGGER.info("Usuario con el metodo findByEmailAndName " +
		//		userRepository.findByEmailAndName("vivasmiguel96@gmail.com", "Miguel").orElseThrow(() -> new RuntimeException("no se encontro el usuario")));

		userRepository.findByNameLike("%u%")
				.stream()
				.forEach(user ->LOGGER.info("Usuario findByNameLike" + user));

		userRepository.findByNameOrEmail(null, "user1@gmail.com")
				.stream()
				.forEach(user ->LOGGER.info("Usuario findByNameOrEmail" + user));

		userRepository.findByBirthDateBetween(LocalDate.of(2021,10,30), LocalDate.of(2021,11,30))
				.stream()
				.forEach(user ->LOGGER.info("Usuario findByBirthDate" + user));

		userRepository.findByNameLikeOrderByIdDesc("%user%")
				.stream()
				.forEach(user -> LOGGER.info("Usario encontrado con like y ordenados" +user));


		LOGGER.info("Usuario con el metodo getAllByBirthDateAndEmail " + userRepository.getAllByBirthDateAndEmail(LocalDate.of(1996,9,13),
						"santi@gmail.com")
				.orElseThrow(() -> new RuntimeException("no se encontro el usuario getallbybirthdateandemail")));

	}

	private void saveUsersInDataBase(){
		User user1 = new User("Miguel", "vivasmiguel96@gmail.com", LocalDate.of(2021,10,30));
		User user2 = new User("santi", "santi@gmail.com", LocalDate.of(1996,9,13));
		User user3 = new User("Juan", "juanjo@gmail.com", LocalDate.of(1944,9,9));
		User user4 = new User("user1", "user1@gmail.com", LocalDate.of(1944,9,9));
		User user5 = new User("user2", "user2o@gmail.com", LocalDate.of(1944,9,9));

		List<User> list = Arrays.asList(user1,user2,user3,user4,user5);

		list.stream().forEach(userRepository::save);

	}

    private void ejemplosAnteriores(){
        componentDependency.saludar();
        myBean.print();
        myBeanWithDependency.printWithDependency();
        System.out.println(myBeanWithProperties.function());
        System.out.println(userPojo.getEmail() + "-" + userPojo.getPassword());
        LOGGER.error("Esto es un error del aplicativo");
    }
}
