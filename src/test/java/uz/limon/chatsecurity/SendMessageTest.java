package uz.limon.chatsecurity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uz.limon.chatsecurity.dto.MessageDTO;
import uz.limon.chatsecurity.dto.ResponseDTO;
import uz.limon.chatsecurity.dto.UserDTO;

import java.io.File;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SendMessageTest {


    static String token;
    @Autowired
    private MockMvc mockMvc;


    @Test
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



    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter writer = objectMapper.writer();
    @Test
    public void sendMessageText() throws Exception {

        MessageDTO messageDTO = MessageDTO.builder()
                .content("My message")
                .ext("TEXT")
                .chat(1)
                .createdAt(new Date().toString())
                .build();


        String content = writer.writeValueAsString(messageDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content("Message");

        String response = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectReader reader = objectMapper.readerFor(new TypeReference<ResponseDTO<Integer>>() {});

        ResponseDTO<Integer> responseDTO = reader.readValue(response);

        Assertions.assertTrue(responseDTO.isSuccess());
        Assertions.assertEquals("OK", responseDTO.getMessage());
        Assertions.assertEquals(0, responseDTO.getCode());
        Assertions.assertNotNull(responseDTO.getData());

    }


    @Test
    public void sendMessageImage() throws Exception {

        MessageDTO messageDTO = MessageDTO.builder()
                .content(FileUtils.readFileToString(new File("src\\test\\java\\uz\\limon\\chatsecurity\\files\\file.txt")))
                .ext("IMAGE")
                .chat(1)
                .createdAt(new Date().toString())
                .build();


        String content = writer.writeValueAsString(messageDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization",  "Bearer ".concat(token));

        String response = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectReader reader = objectMapper.readerFor(new TypeReference<ResponseDTO<Integer>>() {});

        ResponseDTO<Integer> responseDTO = reader.readValue(response);

        Assertions.assertTrue(responseDTO.isSuccess());
        Assertions.assertEquals("OK", responseDTO.getMessage());
        Assertions.assertEquals(0, responseDTO.getCode());
        Assertions.assertNotNull(responseDTO.getData());

    }

}














