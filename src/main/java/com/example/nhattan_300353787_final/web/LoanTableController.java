package com.example.nhattan_300353787_final.web;

import com.example.nhattan_300353787_final.entities.AmortizationScheduleEntry;
import com.example.nhattan_300353787_final.entities.LoanTable;
import com.example.nhattan_300353787_final.repositories.LoanTableRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

// GitHub URL: https://github.com/NhatTanVu/NhatTan_300353787_Final
@SessionAttributes({"AddError"})
@Controller
@AllArgsConstructor
public class LoanTableController {
    @Autowired
    private LoanTableRepository loanTableRepository;

    @GetMapping(path = "/")
    public String loantableList(Model model) {
        List<LoanTable> loanTables = loanTableRepository.findAll();
        model.addAttribute("loanTables", loanTables);
        return "loanTables";
    }

    @GetMapping("/add")
    public String addLoanTable(Model model) {
        model.addAttribute("loanTable", new LoanTable());
        return "add";
    }

    @GetMapping("/edit")
    public String editLoanTable(Model model, String id) {
        LoanTable loanTable = loanTableRepository.findById(id).orElse(null);
        if (loanTable == null) throw new RuntimeException("Loan table does not exist");
        model.addAttribute("loanTable", loanTable);
        return "edit";
    }

    // https://www.investopedia.com/terms/a/amortization.asp
    // https://www.wikihow.com/Calculate-Amortization
    private List<AmortizationScheduleEntry> calculateAmortization(LoanTable loanTable) {
        double annuallyInterest;
        if (loanTable.getLoantype() == "Business")
            annuallyInterest = 0.09;
        else
            annuallyInterest = 0.06;
        double monthlyInterest = annuallyInterest / 12;
        int numOfPayment = loanTable.getYears() * 12;
        double monthlyPayment = loanTable.getLoanamount() * monthlyInterest * Math.pow(1 + monthlyInterest, numOfPayment) / (Math.pow(1 + monthlyInterest, numOfPayment) - 1);
        List<AmortizationScheduleEntry> schedule = new ArrayList<>(numOfPayment);
        double principalAmount = loanTable.getLoanamount();
        for (int i = 1; i <= numOfPayment; i++) {
            AmortizationScheduleEntry entry = new AmortizationScheduleEntry();
            entry.setEntryNo(i);
            entry.setInterestPayment(principalAmount * monthlyInterest);
            entry.setPrincipalPayment(monthlyPayment - entry.getInterestPayment());
            principalAmount = principalAmount - entry.getPrincipalPayment();
            entry.setRemainingPrincipalAmount(principalAmount);
            schedule.add(entry);
        }
        if (numOfPayment > 0)
            schedule.get(numOfPayment - 1).setRemainingPrincipalAmount(0);
        return schedule;
    }

    @GetMapping("/amortizationTable")
    public String amortizationTable(Model model, String id) {
        LoanTable loanTable = loanTableRepository.findById(id).orElse(null);
        if (loanTable == null) throw new RuntimeException("Loan table does not exist");
        List<AmortizationScheduleEntry> amortizationSchedule = calculateAmortization(loanTable);
        model.addAttribute("amortizationSchedule", amortizationSchedule);
        model.addAttribute("clientNumber", loanTable.getClientno());
        model.addAttribute("clientName", loanTable.getClientname());
        return "amortizationTable";
    }

    @GetMapping("/delete")
    public String delete(String id) {
        loanTableRepository.deleteById(id);
        return "redirect:";
    }

    @PostMapping(path = "/add")
    public String add(Model model, LoanTable loanTable, BindingResult bindingResult,
                      ModelMap mm, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "add";
        } else {
            if (loanTableRepository.findById(loanTable.getClientno()).isPresent()) {
                mm.put("AddError", "The record you are trying to add is already existing. Choose a different customer number");
            } else {
                mm.put("AddError", "");
                loanTableRepository.save(loanTable);
            }

            return "redirect:";
        }
    }

    @PostMapping(path = "/save")
    public String save(Model model, LoanTable loanTable, BindingResult bindingResult,
                       ModelMap mm, HttpSession session) {
        mm.put("AddError", "");
        if (bindingResult.hasErrors()) {
            return "add";
        } else {
            loanTableRepository.save(loanTable);

            return "redirect:";
        }
    }


}
