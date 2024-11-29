package org.example.lab4;

import org.example.lab4.service.MemoryManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lab4Application {

	public static void main(String[] args) {
		SpringApplication.run(Lab4Application.class, args);
		MemoryManager manager = new MemoryManager(50);

		int addr1 = manager.allocate(10);
		int addr2 = manager.allocate(35);

		manager.printMemoryState();

		manager.free(addr1);
		manager.printMemoryState();
		manager.allocate(5);
		manager.defragment();

		manager.printMemoryState();
		manager.allocate(5);
		manager.allocate(5);

		manager.printMemoryState();
	}

}
