package com.quickpik;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.quickpik.entities.Role;
import com.quickpik.repositories.RoleRepository;

@SpringBootApplication
public class QuickPikApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Value("${role.admin.id}")
	private String roleAdminId;

	@Value("${role.normal.id}")
	private String roleNormalId;

	public static void main(String[] args) {
		SpringApplication.run(QuickPikApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			Role roleAdmin = Role.builder().roleId(this.roleAdminId).roleName("ROLE_ADMIN").build();
			Role roleNormal = Role.builder().roleId(this.roleNormalId).roleName("ROLE_NORMAL").build();
			this.roleRepository.save(roleAdmin);
			this.roleRepository.save(roleNormal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}