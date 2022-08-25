package com.example.nhattan_300353787_final;

import com.example.nhattan_300353787_final.entities.LoanTable;
import com.example.nhattan_300353787_final.repositories.LoanTableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NhatTan300353787FinalApplication {

    public static void main(String[] args) {
        SpringApplication.run(NhatTan300353787FinalApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(LoanTableRepository loanTableRepository) {
        return args -> {
            if(loanTableRepository.count() == 0) {
                loanTableRepository.save(new LoanTable("1157", "Joy Ramirez", 100000, 5, "Business"));
                loanTableRepository.save(new LoanTable("1005", "Josaphat Dee", 5000, 5, "Business"));
                loanTableRepository.save(new LoanTable("1006", "Jessica Bane", 15000, 1, "Personal"));
                loanTableRepository.save(new LoanTable("9999", "Ravleen test", 1000, 1, "Business"));
                loanTableRepository.save(new LoanTable("1012", "Johnny Jacobi", 8000, 5, "Business"));
            }
        };
    }

}
