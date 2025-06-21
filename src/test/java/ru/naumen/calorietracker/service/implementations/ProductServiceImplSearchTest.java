package ru.naumen.calorietracker.service.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.naumen.calorietracker.dto.ProductResponse;
import ru.naumen.calorietracker.mapper.ProductMapper;
import ru.naumen.calorietracker.model.elastic.ProductSearchDocument;
import ru.naumen.calorietracker.repository.search.ProductSearchRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplSearchTest {

    @Mock
    private ProductSearchRepository searchRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductSearchDocument doc1, doc2;
    private ProductResponse resp1, resp2;

    @BeforeEach
    void setUp() {
        doc1 = new ProductSearchDocument(1, "Яблоко", new BigDecimal("52"), new BigDecimal("0.3"), new BigDecimal("0.2"), new BigDecimal("13.8"), "Фрукты", 1);
        doc2 = new ProductSearchDocument(2, "Апельсин", new BigDecimal("47"), new BigDecimal("0.9"), new BigDecimal("0.1"), new BigDecimal("11.8"), "Фрукты", 1);
        resp1 = new ProductResponse(1, "Яблоко", new BigDecimal("52"), new BigDecimal("0.3"), new BigDecimal("0.2"), new BigDecimal("13.8"), null, "Фрукты");
        resp2 = new ProductResponse(2, "Апельсин", new BigDecimal("47"), new BigDecimal("0.9"), new BigDecimal("0.1"), new BigDecimal("11.8"), null, "Фрукты");
    }

    @Test
    void searchByName_FuzzyNotEmpty_ShouldReturnFuzzyResults() {
        when(searchRepository.findByNameFuzzy("ябл")).thenReturn(List.of(doc1, doc2));
        when(productMapper.toProductResponseList(List.of(doc1, doc2))).thenReturn(List.of(resp1, resp2));

        List<ProductResponse> result = productService.searchByName("ябл");

        verify(searchRepository).findByNameFuzzy("ябл");
        verify(searchRepository, never()).findByNamePrefix(any());
        assertEquals(List.of(resp1, resp2), result);
    }

    @Test
    void searchByName_FuzzyEmpty_ShouldFallbackToPrefix() {
        when(searchRepository.findByNameFuzzy("апе")).thenReturn(List.of());
        when(searchRepository.findByNamePrefix("апе")).thenReturn(List.of(doc2));
        when(productMapper.toProductResponseList(List.of(doc2))).thenReturn(List.of(resp2));

        List<ProductResponse> result = productService.searchByName("апе");

        verify(searchRepository).findByNameFuzzy("апе");
        verify(searchRepository).findByNamePrefix("апе");
        assertEquals(List.of(resp2), result);
    }

    @Test
    void searchByName_NoMatches_ShouldReturnEmptyList() {
        // 1) оба метода репозитория возвращают пустой список
        when(searchRepository.findByNameFuzzy("грум")).thenReturn(List.of());
        when(searchRepository.findByNamePrefix("грум")).thenReturn(List.of());
        // 2) маппер должен получить пустой список
        when(productMapper.toProductResponseList(List.of())).thenReturn(List.of());

        // 3) вызываем сервис
        List<ProductResponse> result = productService.searchByName("грум");

        // 4) проверяем вызовы и результат
        verify(searchRepository).findByNameFuzzy("грум");
        verify(searchRepository).findByNamePrefix("грум");
        verify(productMapper).toProductResponseList(List.of());
        assertTrue(result.isEmpty(), "Ожидается пустой список при отсутствии совпадений");
    }

}

