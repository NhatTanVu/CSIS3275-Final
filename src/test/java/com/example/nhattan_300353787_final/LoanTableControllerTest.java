package com.example.nhattan_300353787_final;

import com.example.nhattan_300353787_final.entities.LoanTable;
import com.example.nhattan_300353787_final.repositories.LoanTableRepository;
import com.example.nhattan_300353787_final.web.LoanTableController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;/**/
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.View;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
class LoanTableControllerTest {

    LoanTable loanTable;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    LoanTableRepository loanTableRepository;


    @Mock
    View mockView;


    @InjectMocks
    LoanTableController loanTableController;


    @BeforeEach
    void setup() throws ParseException {
        loanTable = new LoanTable();
        loanTable.setClientno("300353787");
        loanTable.setClientname("Nhat Tan Vu");
        loanTable.setLoanamount(400);
        loanTable.setYears(4);
        loanTable.setLoantype("Business");

        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(loanTableController).setSingleView(mockView).build();
    }

    @Test
    public void showList() throws Exception {
        List<LoanTable> list = new ArrayList<LoanTable>();
        list.add(loanTable);
        list.add(loanTable);

        when(loanTableRepository.findAll()).thenReturn(list);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("loanTables", list))
                .andExpect(view().name("loanTables"))
                .andExpect(model().attribute("loanTables", hasSize(2)));

        verify(loanTableRepository, times(1)).findAll();
        verifyNoMoreInteractions(loanTableRepository);
    }

    @Test
    void performDelete() throws Exception {
        ArgumentCaptor<String> idCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(loanTableRepository).deleteById(idCapture.capture());

        mockMvc.perform(get("/delete").param("id", "53787"))
                .andExpect(status().isOk())
                .andExpect(view().name("redirect:"));

        assertEquals("53787", idCapture.getValue());
        verify(loanTableRepository, times(1)).deleteById("53787");
        verifyNoMoreInteractions(loanTableRepository);
    }

    @Test
    void showEdit() throws Exception {
        LoanTable s2 = new LoanTable();
        String iid = "53787";
        when(loanTableRepository.findById(iid)).thenReturn(Optional.of(s2));

        mockMvc.perform(get("/edit").param("id", iid))
                .andExpect(status().isOk())
                .andExpect(model().attribute("loanTable", s2))
                .andExpect(view().name("edit"));

        verify(loanTableRepository, times(1)).findById(anyString());
        verifyNoMoreInteractions(loanTableRepository);
    }

    @Test
    void performEdit() throws Exception {
        LoanTable s2 = new LoanTable();
        when(loanTableRepository.save(s2)).thenReturn(s2);

        mockMvc.perform(post("/save").param("loanTable", String.valueOf(s2)))
                .andExpect(status().isOk())
                .andExpect(view().name("redirect:"));

        verify(loanTableRepository, times(1)).save(s2);
    }

    @Test
    void showAdd() throws Exception {
        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("loanTable", new LoanTable()))
                .andExpect(view().name("add"));
    }

    @Test
    void performAdd() throws Exception {
        LoanTable s2 = new LoanTable();
        when(loanTableRepository.save(s2)).thenReturn(s2);

        mockMvc.perform(post("/add").param("loanTable", String.valueOf(s2)))
                .andExpect(status().isOk())
                .andExpect(view().name("redirect:"));

        verify(loanTableRepository, times(1)).save(s2);
    }

    @Test
    void showAmortizationTable() throws Exception {
        LoanTable s2 = new LoanTable();
        String iid = "53787";
        when(loanTableRepository.findById(iid)).thenReturn(Optional.of(s2));

        mockMvc.perform(get("/amortizationTable").param("id", iid))
                .andExpect(status().isOk())
                .andExpect(model().attribute("clientNumber", s2.getClientno()))
                .andExpect(model().attribute("clientName", s2.getClientname()))
                .andExpect(model().attributeExists("amortizationSchedule"))
                .andExpect(view().name("amortizationTable"));
    }
}