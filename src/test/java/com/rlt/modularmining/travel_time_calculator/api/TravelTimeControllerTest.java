package com.rlt.modularmining.travel_time_calculator.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rlt.modularmining.travel_time_calculator.dto.FromToData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TravelTimeControllerTest {
    /*
     * `MockMvc` comes from Spring Test and lets you, through a set of convenient builder classes,
     * send HTTP requests into the `DispatcherServlet` and make assertions about the result.
     */
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Opcional: inyectar el controlador si necesitas limpiar su estado
    @Autowired
    private TravelTimeController travelTimeController;

    // Limpiamos el grafo antes de cada test para que sean independientes
    @BeforeEach
    void setUp() {
        travelTimeController.graph.clear(); // Asumiendo que Graph tiene un método clear()
    }

    @Test
    void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hello, World! From TravelTimeController Spring Boot Application")));
    }

    @Test
    void whenUploadValidCsv_thenReturnsSuccess() throws Exception {
        // 1. Contenido del CSV de prueba
        String csvContent = """
                            loc_start;loc_end;time
                            R11;R12;20
                            R12;R13;9
                            R13;R12;11
                            R13;R20;9
                            R20;R13;11
                            CP1;R11;84
                            R11;CP1;92
                            CP1;CP2;7
                            CP2;CP1;10
                            CP2;R20;67
                            R20;CP2;60;
                            """;

        // 2. Crear el archivo simulado (MockMultipartFile)
        MockMultipartFile file = new MockMultipartFile(
                "file",               // El nombre del parámetro en el @RequestParam
                "test-data.csv",      // Nombre del archivo original
                "text/csv",           // Content Type
                csvContent.getBytes() // El contenido del archivo
        );

        // 3. Realizar la petición POST multipart y verificar la respuesta
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload-csv").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("CSV file uploaded and processed successfully."));
    }

    @Test
    void whenUploadEmptyFile_thenReturnsBadRequest() throws Exception {
        // 1. Crear un archivo simulado pero vacío
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.csv",
                "text/csv",
                new byte[0] // Contenido vacío
        );

        // 2. Realizar la petición y verificar que responde con un 400 Bad Request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload-csv").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to process the CSV file"));
    }

    @Test
    void whenPathExists_thenReturnsFastestPath() throws Exception {
        // 1. Pre-condición: Poblar el grafo con datos conocidos.
        //    Esto se puede hacer subiendo un archivo como en el test anterior,
        //    o llamando directamente a los métodos del grafo si es posible.
        //    Aquí lo hacemos con una llamada al endpoint de subida para preparar el estado.
        String csvContent = """
                            loc_start;loc_end;time
                            R11;R12;20
                            R12;R13;9
                            R13;R12;11
                            R13;R20;9
                            R20;R13;11
                            CP1;R11;84
                            R11;CP1;92
                            CP1;CP2;7
                            CP2;CP1;10
                            CP2;R20;67
                            R20;CP2;60;
                            """;
        MockMultipartFile file = new MockMultipartFile("file", "graph.csv", "text/csv", csvContent.getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload-csv").file(file));

        // 2. Crear el objeto de la petición (Request Body)
        FromToData requestData = new FromToData();
        requestData.setFrom("R12");
        requestData.setTo("R11");

        // 3. Realizar la petición POST con el body en formato JSON
        mockMvc.perform(post("/fastest-path")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ruta[0]").value("R12")) // jsonPath para verificar el contenido del JSON
                .andExpect(jsonPath("$.path[1]").value("R13"))
                .andExpect(jsonPath("$.path[2]").value("R20"))
                .andExpect(jsonPath("$.path[3]").value("RCP2"))
                .andExpect(jsonPath("$.path[4]").value("CP1"))
                .andExpect(jsonPath("$.path[5]").value("R11"))
                .andExpect(jsonPath("$.tiempoTotal").value(172));
    }
}
