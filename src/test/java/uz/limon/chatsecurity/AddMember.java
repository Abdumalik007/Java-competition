package uz.limon.chatsecurity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uz.limon.chatsecurity.dto.ResponseDTO;
import uz.limon.chatsecurity.dto.UserDTO;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.param;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddMember {

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();


    public static String token;
    @Autowired
    private MockMvc mockMvc;


    @Test
    @Order(1)
    void login() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("Ab3kmems");
        userDTO.setUsername("malik1210");

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/token")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);

        String responseBody = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectReader reader = objectMapper.readerFor(new TypeReference<ResponseDTO<String>>() {});
        ResponseDTO<String> responseDTO = reader.readValue(responseBody);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertNotNull(responseDTO.getData());
        Assertions.assertEquals(0, responseDTO.getCode());
        Assertions.assertEquals("OK", responseDTO.getMessage());
        Assertions.assertTrue(responseDTO.isSuccess());

        token = responseDTO.getData();
    }


    @Test
    @Order(2)
    public void addMemberChats() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/resource/add-member")
                .param("chatId", String.valueOf(1))
                .param("userId", String.valueOf(1));

        String response = mockMvc.perform(request)
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectReader reader = mapper.readerFor(new TypeReference<ResponseDTO<String>>(){});

        ResponseDTO<String> responseDTO = reader.readValue(response);

        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(responseDTO.getCode(), 0);
        Assertions.assertEquals(responseDTO.getMessage(), "OK");
        Assertions.assertEquals(responseDTO.getData(), "Successfully saved!");
    }

}