package com.himanshu.departmentalStore;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Departmental Store Application",
				description = "Spring boot Project to create Api (CRUD operations) for departmental store.",
				version = "1.0",
				contact = @Contact(name = "Himanshu Kumar",
						email = "himanshu.kumar@geminisolutions.com",
						url = "https://github.com/GEM-himanshu-kumar/departmentalStore.git")
		)
)
public class DepartmentalStoreApplication {

	public static void main(final String[] args) {
		SpringApplication.run(DepartmentalStoreApplication.class, args);
	}

}
