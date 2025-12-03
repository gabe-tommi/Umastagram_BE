package com.c11.umastagram.service;

import com.c11.umastagram.model.Uma;
import com.c11.umastagram.repository.UmaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
// removed unused imports
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

 
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UmaServiceTest {

    @Mock
    private UmaRepository umaRepository;
    
    @Test
    public void saveUma_successfulSave() {
        UmaService service = new UmaService(umaRepository);

        Uma uma = new Uma();
        uma.setUmaName("Test Uma");
        uma.setUmaImageLink("http://example.com/test.jpg");
        uma.setUmaIconLink("http://example.com/icon.png");
        uma.setFunFact("Fast horse");

        when(umaRepository.getUmaByName("Test Uma")).thenReturn(Optional.empty());
        when(umaRepository.save(any(Uma.class))).thenAnswer(invocation -> {
            Uma u = invocation.getArgument(0);
            u.setUmaId(1L);
            return u;
        });

        Uma saved = service.saveUma(uma);

        assertThat(saved).isNotNull();
        assertThat(saved.getUmaId()).isEqualTo(1L);
        verify(umaRepository).getUmaByName("Test Uma");
        verify(umaRepository).save(any(Uma.class));
    }

    @Test
    public void saveUma_missingName_throws() {
        UmaService service = new UmaService(umaRepository);

        Uma uma = new Uma();
        uma.setUmaImageLink("http://example.com/test.jpg");

        assertThatThrownBy(() -> service.saveUma(uma))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Uma name is required");

        verify(umaRepository, never()).save(any());
    }

    @Test
    public void saveUma_duplicateName_throws() {
        UmaService service = new UmaService(umaRepository);

        Uma uma = new Uma();
        uma.setUmaName("Duplicate");
        uma.setUmaImageLink("http://example.com/test.jpg");
        uma.setUmaIconLink("http://example.com/icon.png");

        when(umaRepository.getUmaByName("Duplicate")).thenReturn(Optional.of(new Uma()));

        assertThatThrownBy(() -> service.saveUma(uma))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(umaRepository).getUmaByName("Duplicate");
        verify(umaRepository, never()).save(any());
    }

    @Test
    public void findUmaById_returnsUma() {
        UmaService service = new UmaService(umaRepository);

        Uma uma = new Uma();
        uma.setUmaId(5L);
        uma.setUmaName("Lookup Uma");

        when(umaRepository.getUmaById(5L)).thenReturn(Optional.of(uma));

        Optional<Uma> found = service.findUmaById(5L);

        assertThat(found).isPresent();
        assertThat(found.get().getUmaName()).isEqualTo("Lookup Uma");
    }

    @Test
    public void getAllUmas_filtersEmptyList() {
        UmaService service = new UmaService(umaRepository);

        when(umaRepository.getAllUmas()).thenReturn(Optional.of(new ArrayList<>()));

        Optional<List<Uma>> result = service.getAllUmas();

        assertThat(result).isEmpty();
    }

    @Test
    public void deleteUmaById_successfulDelete() {
        UmaService service = new UmaService(umaRepository);

        Uma uma = new Uma();
        uma.setUmaId(10L);
        uma.setUmaName("ToDelete");

        when(umaRepository.getUmaById(10L)).thenReturn(Optional.of(uma));
        doNothing().when(umaRepository).delete(uma);

        String msg = service.deleteUmaById(10L);

        assertThat(msg).contains("ToDelete");
        verify(umaRepository).delete(uma);
    }

    @Test
    public void getUmaIconLinkById_returnsIconLink() {
        UmaService service = new UmaService(umaRepository);

        when(umaRepository.getUmaIconLinkById(3L)).thenReturn(Optional.of("http://example.com/icon.png"));

        Optional<String> iconLink = service.getUmaIconLinkById(3L);

        assertThat(iconLink).isPresent();
        assertThat(iconLink.get()).isEqualTo("http://example.com/icon.png");
        verify(umaRepository).getUmaIconLinkById(3L);
    }

    @Test
    public void getUmaIconLinkById_withNullId_returnsEmpty() {
        UmaService service = new UmaService(umaRepository);

        Optional<String> iconLink = service.getUmaIconLinkById(null);

        assertThat(iconLink).isEmpty();
        verify(umaRepository, never()).getUmaIconLinkById(any());
    }

    @Test
    public void getUmaImageLinkById_returnsImageLink() {
        UmaService service = new UmaService(umaRepository);

        when(umaRepository.getUmaImageLinkById(7L)).thenReturn(Optional.of("http://example.com/uma-image.jpg"));

        Optional<String> imageLink = service.getUmaImageLinkById(7L);

        assertThat(imageLink).isPresent();
        assertThat(imageLink.get()).isEqualTo("http://example.com/uma-image.jpg");
        verify(umaRepository).getUmaImageLinkById(7L);
    }

    @Test
    public void getUmaImageLinkById_withNonExistentId_returnsEmpty() {
        UmaService service = new UmaService(umaRepository);

        when(umaRepository.getUmaImageLinkById(999L)).thenReturn(Optional.empty());

        Optional<String> imageLink = service.getUmaImageLinkById(999L);

        assertThat(imageLink).isEmpty();
        verify(umaRepository).getUmaImageLinkById(999L);
    }

    @Test
    public void getUmaImageLinkById_withNullId_returnsEmpty() {
        UmaService service = new UmaService(umaRepository);

        Optional<String> imageLink = service.getUmaImageLinkById(null);

        assertThat(imageLink).isEmpty();
        verify(umaRepository, never()).getUmaImageLinkById(any());
    }
}
