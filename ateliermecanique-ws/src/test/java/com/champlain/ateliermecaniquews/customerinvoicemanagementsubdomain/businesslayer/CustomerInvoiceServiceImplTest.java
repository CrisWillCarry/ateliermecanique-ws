package com.champlain.ateliermecaniquews.customerinvoicemanagementsubdomain.businesslayer;

import com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.datalayer.Appointment;
import com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.datalayer.Status;
import com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.presentationlayer.AppointmentRequestModel;
import com.champlain.ateliermecaniquews.appointmentmanagementsubdomain.presentationlayer.AppointmentResponseModel;
import com.champlain.ateliermecaniquews.authenticationsubdomain.dataLayer.User;
import com.champlain.ateliermecaniquews.authenticationsubdomain.dataLayer.repositories.UserRepository;
import com.champlain.ateliermecaniquews.customerinvoicemanagementsubdomain.datalayer.CustomerInvoice;
import com.champlain.ateliermecaniquews.customerinvoicemanagementsubdomain.datalayer.CustomerInvoiceRepository;
import com.champlain.ateliermecaniquews.customerinvoicemanagementsubdomain.datamapperlayer.CustomerInvoiceResponseMapper;
import com.champlain.ateliermecaniquews.customerinvoicemanagementsubdomain.presentationlayer.CustomerInvoiceRequestModel;
import com.champlain.ateliermecaniquews.customerinvoicemanagementsubdomain.presentationlayer.CustomerInvoiceResponseModel;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerInvoiceServiceImplTest {


    @Mock
    private CustomerInvoiceRepository customerInvoiceRepository;

    @Mock
    private CustomerInvoiceResponseMapper customerInvoiceResponseMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomerInvoiceServiceImpl customerInvoiceService;

    @Captor
    private ArgumentCaptor<CustomerInvoice> customerInvoiceArgumentCaptor;

    @Test
    void getAllCustomerInvoices_shouldSucceed() {
        // Arrange
        CustomerInvoice invoice = new CustomerInvoice(

                "yzab8cd5-3e6f-8796-2abi-96330c6bw164",
                "1008dc5c-d460-443f-8f37-a174284f868l",
                "2024-02-04 19:00",
                "Muffler was fixed",
                118.95
        );

        List<CustomerInvoice> invoices = Collections.singletonList(invoice);
        when(customerInvoiceRepository.findAll()).thenReturn(invoices);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2024-02-04 19:00", formatter);

        CustomerInvoiceResponseModel responseModel = CustomerInvoiceResponseModel.builder()
                .invoiceId(invoice.getCustomerInvoiceIdentifier().getInvoiceId().toString())
                .customerId(invoice.getCustomerId())
                .appointmentId(invoice.getAppointmentId())
                .invoiceDate(dateTime)
                .mechanicNotes(invoice.getMechanicNotes())
                .sumOfServices(invoice.getSumOfServices())
                .build();

        List<CustomerInvoiceResponseModel> responseModels = Collections.singletonList(responseModel);

        when(customerInvoiceResponseMapper.entityToResponseModelList(invoices)).thenReturn(responseModels);

        // Act
        List<CustomerInvoiceResponseModel> result = customerInvoiceService.getAllInvoices();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(responseModels.size(), result.size());
        assertEquals(responseModels.get(0).getInvoiceId(), result.get(0).getInvoiceId());

        verify(customerInvoiceRepository, times(1)).findAll();
        verify(customerInvoiceResponseMapper, times(1)).entityToResponseModelList(invoices);
    }


    @Test
    void getAllInvoicesByCustomerId_shouldReturnInvoiceResponseModelList() {
        // Arrange
        String customerId = "testCustomerId";
        CustomerInvoice invoice = new CustomerInvoice();
        List<CustomerInvoice> invoices = Arrays.asList(invoice);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2024-03-24 11:00", formatter);


        CustomerInvoiceResponseModel responseModel = CustomerInvoiceResponseModel.builder()
                .invoiceId(invoice.getCustomerInvoiceIdentifier().getInvoiceId().toString())
                .customerId(invoice.getCustomerId())
                .appointmentId(invoice.getAppointmentId())
                .invoiceDate(dateTime)
                .mechanicNotes(invoice.getMechanicNotes())
                .sumOfServices(invoice.getSumOfServices())
                .build();


        when(customerInvoiceRepository.findAllInvoicesByCustomerId(customerId)).thenReturn(invoices);
        when(customerInvoiceResponseMapper.entityToResponseModelList(invoices)).thenReturn(Arrays.asList(responseModel));

        // Act
        List<CustomerInvoiceResponseModel> result = customerInvoiceService.getAllInvoicesByCustomerId(customerId);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(customerInvoiceRepository).findAllInvoicesByCustomerId(customerId);
        verify(customerInvoiceResponseMapper).entityToResponseModelList(invoices);
    }



    @Test
    void getAllInvoicesByCustomerId_noAppointments_shouldReturnEmptyList() {
        String customerId = "someCustomerId";
        when(customerInvoiceRepository.findAllInvoicesByCustomerId(customerId)).thenReturn(Collections.emptyList());

        List<CustomerInvoiceResponseModel> result = customerInvoiceService.getAllInvoicesByCustomerId(customerId);

        assertTrue(result.isEmpty());
        verify(customerInvoiceRepository).findAllInvoicesByCustomerId(customerId);
        verify(customerInvoiceResponseMapper, never()).entityToResponseModelList(any());
    }


    @Test
    void getAllInvoicesByCustomerId_shouldReturnNull() {
        String customerId = "testCustomerId";
        List<CustomerInvoice> invoices = Arrays.asList(new CustomerInvoice());
        when(customerInvoiceRepository.findAllInvoicesByCustomerId(customerId)).thenReturn(invoices);
        when(customerInvoiceResponseMapper.entityToResponseModelList(invoices)).thenReturn(null);

        List<CustomerInvoiceResponseModel> result = customerInvoiceService.getAllInvoicesByCustomerId(customerId);

        assertNull(result);
        verify(customerInvoiceRepository).findAllInvoicesByCustomerId(customerId);
        verify(customerInvoiceResponseMapper).entityToResponseModelList(invoices);
    }


    @Test
    void addInvoiceToCustomerAccount_shouldSucceed() {
        // Arrange
        String customerId = "testCustomerId";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2024-03-24 11:00", formatter);
        CustomerInvoiceRequestModel requestModel = CustomerInvoiceRequestModel.builder()
                .customerId(customerId)
                .appointmentId("testAppointmentId")
                .invoiceDate(dateTime)
                .mechanicNotes("Test mechanic notes")
                .sumOfServices(100.00)
                .build();

        User customerAccount = new User();

        when(userRepository.findUserByUserIdentifier_UserId(customerId))
                .thenReturn(customerAccount);

        CustomerInvoice savedInvoice = new CustomerInvoice();
        when(customerInvoiceRepository.save(any(CustomerInvoice.class)))
                .thenReturn(savedInvoice);

        CustomerInvoiceResponseModel expectedResponse = CustomerInvoiceResponseModel.builder()
                .invoiceId("1")
                .customerId(customerId)
                .appointmentId("testAppointmentId")
                .invoiceDate(dateTime)
                .mechanicNotes("Test mechanic notes")
                .sumOfServices(100.00)
                .build();

        when(customerInvoiceResponseMapper.entityToResponseModel(savedInvoice))
                .thenReturn(expectedResponse);


        // Act
        CustomerInvoiceResponseModel result = customerInvoiceService.addInvoiceToCustomerAccount(customerId, requestModel);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getCustomerId(), result.getCustomerId());
        assertEquals(expectedResponse.getSumOfServices(), result.getSumOfServices());
        verify(customerInvoiceRepository, times(1)).save(any(CustomerInvoice.class));
    }

    @Test
    void addInvoiceToCustomerAccount_CustomerNotFound_shouldReturnNull() {
        // Arrange
        String nonExistentCustomerId = "nonExistentCustomerId";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2024-03-24 11:00", formatter);

        CustomerInvoiceRequestModel requestModel = CustomerInvoiceRequestModel.builder()
                .customerId(nonExistentCustomerId)
                .appointmentId("testAppointmentId")
                .invoiceDate(dateTime)
                .mechanicNotes("Test mechanic notes")
                .sumOfServices(100.00)
                .build();

        when(userRepository.findUserByUserIdentifier_UserId(nonExistentCustomerId))
                .thenReturn(null); // Simulate that the user is not found

        // Act
        CustomerInvoiceResponseModel result = customerInvoiceService.addInvoiceToCustomerAccount(nonExistentCustomerId, requestModel);

        // Assert
        assertNull(result, "No customer found with corresponding customerId");
    }


}