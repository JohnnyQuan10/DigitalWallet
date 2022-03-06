package au.com.api.wallet.integration;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import au.com.api.wallet.TestApplication;
import au.com.api.wallet.dto.request.PaymentNotificationRequest;
import au.com.api.wallet.dto.request.TransactionRequest;
import au.com.api.wallet.dto.response.AccountResponse;
import au.com.api.wallet.dto.response.TransactionResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = TestApplication.class)
public class PaymentIntegrationTest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() { mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    public static final String URL_BASE = "/payment";
    public static final String GET_ACCOUNT_URL = URL_BASE + "/account/user/{userId}";
    public static final String GET_TRANSACTIONS_URL = URL_BASE + "/transactions/user/{userId}";

    public static final String TOKEN = "HmacSHA256 7252322798a42f4005596e78ab602e300acf51c13628bb4bff6b5aa929cadec4";

    public static final TransactionRequest transactionRequest = TransactionRequest.builder()
            .id(UUID.randomUUID())
            .userId(UUID.fromString("449416d8-ec3c-4c0b-a326-e2cfaadaa3a6"))
            .userName("John Smith")
            .amount("100.08")
            .build();

    public static final TransactionRequest transactionRequest1 = TransactionRequest.builder()
            .id(UUID.randomUUID())
            .userId(UUID.fromString("449416d8-ec3c-4c0b-a326-e2cfaadaa3a6"))
            .userName("John Smith")
            .amount("50.9")
            .build();

    public static final TransactionRequest transactionRequest2 = TransactionRequest.builder()
            .id(UUID.randomUUID())
            .userId(UUID.fromString("80cb89ac-9ce3-11ec-b909-0242ac120002"))
            .userName("Nick Smith")
            .amount("220.00")
            .build();

    public static final TransactionRequest transactionRequest3 = TransactionRequest.builder()
            .id(UUID.randomUUID())
            .userId(UUID.fromString("a5a55c94-9ce3-11ec-b909-0242ac120002"))
            .userName("Tom Smith")
            .amount("220.00")
            .build();

    public static final PaymentNotificationRequest paymentNotificationRequest = PaymentNotificationRequest.builder()
            .transactions(transactionRequest).build();

    public static final PaymentNotificationRequest paymentNotificationRequest1 = PaymentNotificationRequest.builder()
            .transactions(transactionRequest1).build();

    public static final PaymentNotificationRequest paymentNotificationRequest2 = PaymentNotificationRequest.builder()
            .transactions(transactionRequest2).build();

    public static final PaymentNotificationRequest paymentNotificationRequest3 = PaymentNotificationRequest.builder()
            .transactions(transactionRequest3).build();


    @Test
    public void post_givenEmptyRequest_shouldReturn400() throws Exception {

        mockMvc.perform(post(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EMPTY))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void post_givenDuplicatedTransactionRequest_shouldReturn400() throws Exception {

        mockMvc.perform(post(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentNotificationRequest))
                .header("Authorization", TOKEN))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        final MvcResult result = mockMvc.perform(post(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentNotificationRequest))
                .header("Authorization", TOKEN))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertThat(result.getResponse().getContentAsString(), is("The transaction " + transactionRequest.getId() + " has been recorded before."));
    }

    @Test
    public void post_givenValidRequest_shouldReturn201() throws Exception {

        final MvcResult result = mockMvc.perform(post(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentNotificationRequest1))
                .header("Authorization", TOKEN))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        assertThat(result.getResponse().getContentAsString(), is("The transaction has been saved."));
    }

    @Test
    public void getAccount_givenValidRequest_shouldReturn200() throws Exception {
        mockMvc.perform(post(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentNotificationRequest2))
                .header("Authorization", TOKEN))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        final MvcResult mvcResult = mockMvc.perform(get(GET_ACCOUNT_URL, transactionRequest2.getUserId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        final AccountResponse response =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountResponse.class);

        assertThat(response.getUserName(), is("Nick Smith"));
    }

    @Test
    public void getTransactions_givenValidRequest_shouldReturn200() throws Exception {

        mockMvc.perform(post(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentNotificationRequest3))
                .header("Authorization", TOKEN))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        final MvcResult mvcResult = mockMvc.perform(get(GET_TRANSACTIONS_URL, transactionRequest3.getUserId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        final List<TransactionResponse> transactionResponses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<TransactionResponse>>() {
                });

        assertThat(transactionResponses.size(), is(1));
    }

}
